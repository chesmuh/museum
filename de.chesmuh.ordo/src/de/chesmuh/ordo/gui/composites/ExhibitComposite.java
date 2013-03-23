package de.chesmuh.ordo.gui.composites;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.entitys.Tag;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;
import de.chesmuh.ordo.logic.LogicAccess;

public class ExhibitComposite extends Composite implements IUiListener {

	private Table table;
	private Group group;
	private Object criterion;

	public ExhibitComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		// ----- Layout -----
		this.setLayout(new GridLayout(1, true));

		group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setLayout(new GridLayout(1, true));

		// ----- Buttons -----
		ToolBar toolBar = new ToolBar(group, SWT.NONE);
		ToolItem toolItemAdd = new ToolItem(toolBar, SWT.PUSH);
		toolItemAdd.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_ADD));
		toolItemAdd.addSelectionListener(new AddExhibitAdapter());
		ToolItem toolItemRemove = new ToolItem(toolBar, SWT.PUSH);
		toolItemRemove.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_DELETE));
		toolItemRemove.addSelectionListener(new RemoveExhibitAdapter());

		// ----- Table -----
		table = new Table(group, SWT.MULTI | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);
		table.addSelectionListener(new TableSelectionAdapter());

		// ----- DragSource -----
		DragSource dragSource = new DragSource(table, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new TableDragSourceAdapter());

		// ----- Listener -----
		MainFrame.addObserver(UiEventType.SectionSelected, this);
		MainFrame.addObserver(UiEventType.ExhibitAdded, this);
		MainFrame.addObserver(UiEventType.TagSelected, this);
		MainFrame.addObserver(UiEventType.TagDeleted, this);
		MainFrame.addObserver(UiEventType.ExhibitDeleted, this);
		MainFrame.addObserver(UiEventType.MuseumSelected, this);
	}

	@Override
	public void handleEvent(UiEvent event) {
		UiEventType eventType = event.getType();
		switch (eventType) {
		case SectionSelected:
			if (event.getData() instanceof Section) {
				Section section = (Section) event.getData();
				showExhibitsBySection(section);
			}
			break;
		case ExhibitAdded:
			if (event.getData() instanceof Exhibit) {
				Exhibit exhibit = (Exhibit) event.getData();
				if (null != exhibit.getSection()) {
					showExhibitsBySection(((Exhibit) event.getData())
							.getSection());
				}
			}
			break;
		case TagSelected:
			if (event.getData() instanceof Tag) {
				showExhibitsByTag((Tag) event.getData());
			}
			break;
		case MuseumSelected:
			if (event.getData() instanceof Museum) {
				showExhibitsByMuseum((Museum) event.getData());
			}
			break;
		case ExhibitDeleted:
		case TagDeleted:
			refreshTable();
			break;
		default:
			break;
		}
	}

	private void showExhibitsByMuseum(Museum museum) {
		criterion = museum;
		table.setRedraw(false);
		group.setText(ResourceManager.getText(OrdoUI.TABLE_GROUP_EXHIBIT));
		deleteAllColumn();
		deleteAllItems();
		addColumns(new String[] {
				ResourceManager.getText(OrdoUI.TABLE_HEADER_NAME),
				ResourceManager.getText(OrdoUI.TABLE_HEADER_SECTION),
				ResourceManager.getText(OrdoUI.TABLE_HEADER_DESCRIPTION) });
		Collection<Exhibit> exhibits = DataAccess.getInstance()
				.getExhibitsByMuseumWithSectionNull(museum);
		for (Exhibit exhibit : exhibits) {
			TableItem item = new TableItem(table, SWT.NONE);
			Section exhibitSection = exhibit.getSection();
			item.setText(0, exhibit.getName());
			if (exhibitSection != null) {
				item.setText(1, exhibitSection.getName());
			}
			item.setText(2, exhibit.getDescription());
			item.setData(exhibit);
		}
		table.setRedraw(true);
	}

	private void showExhibitsByTag(Tag tag) {
		criterion = tag;
		table.setRedraw(false);
		group.setText(ResourceManager.getText(OrdoUI.TABLE_GROUP_EXHIBIT));
		deleteAllColumn();
		deleteAllItems();
		addColumns(new String[] {
				ResourceManager.getText(OrdoUI.TABLE_HEADER_NAME),
				ResourceManager.getText(OrdoUI.TABLE_HEADER_SECTION),
				ResourceManager.getText(OrdoUI.TABLE_HEADER_DESCRIPTION) });
		Collection<Exhibit> exhibits = tag.getExhibits();
		for (Exhibit exhibit : exhibits) {
			TableItem item = new TableItem(table, SWT.NONE);
			Section exhibitSection = exhibit.getSection();
			item.setText(0, exhibit.getName());
			if (exhibitSection != null) {
				item.setText(1, exhibitSection.getName());
			}
			item.setText(2, exhibit.getDescription());
			item.setData(exhibit);
		}
		table.setRedraw(true);
	}

	private void showExhibitsBySection(Section section) {
		criterion = section;
		table.setRedraw(false);
		group.setText(ResourceManager.getText(OrdoUI.TABLE_GROUP_EXHIBIT));
		deleteAllColumn();
		deleteAllItems();
		addColumns(new String[] {
				ResourceManager.getText(OrdoUI.TABLE_HEADER_NAME),
				ResourceManager.getText(OrdoUI.TABLE_HEADER_SECTION),
				ResourceManager.getText(OrdoUI.TABLE_HEADER_DESCRIPTION) });
		Collection<Exhibit> exhibits = DataAccess.getInstance()
				.getExhibitBySection(section);
		for (Exhibit exhibit : exhibits) {
			TableItem item = new TableItem(table, SWT.NONE);
			Section exhibitSection = exhibit.getSection();
			item.setText(0, exhibit.getName());
			if (exhibitSection != null) {
				item.setText(1, exhibitSection.getName());
			}
			item.setText(2, exhibit.getDescription());
			item.setData(exhibit);
		}
		table.setRedraw(true);
	}

	private void deleteAllItems() {
		while (table.getItemCount() > 0) {
			table.remove(0);
		}

	}

	private void deleteAllColumn() {
		while (table.getColumnCount() > 0) {
			table.getColumns()[0].dispose();
		}
	}

	private void addColumns(String[] columnNames) {
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(columnNames[i]);
			column.pack();
		}
	}

	private void refreshTable() {
		if (criterion instanceof Section) {
			Section section = (Section) criterion;
			if (!section.isDeleted()) {
				this.showExhibitsBySection(section);
			} else {
				criterion = null;
				refreshTable();
			}
		} else if (criterion instanceof Tag) {
			this.showExhibitsByTag((Tag) criterion);
			Tag tag = (Tag) criterion;
			if (!tag.isDeleted()) {
				this.showExhibitsByTag(tag);
			} else {
				criterion = null;
				refreshTable();
			}
		} else if (criterion instanceof Museum) {
			Museum museum = (Museum) criterion;
			if (!museum.isDeleted()) {
				this.showExhibitsByTag((Tag) criterion);
			} else {
				criterion = null;
				refreshTable();
			}
		} else {
			table.setRedraw(false);
			deleteAllColumn();
			deleteAllItems();
			table.setRedraw(true);
		}
	}

	private class TableSelectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (table.getSelection().length > 0) {
				Object data = table.getSelection()[0].getData();
				if (data instanceof Exhibit) {
					UiEvent event = new UiEvent(data,
							UiEventType.ExhibitSelected);
					MainFrame.handleEvent(event);
				}
			}
		}

	}

	private class AddExhibitAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			UiEvent event;
			event = new UiEvent(null, UiEventType.AddExhibit);
			MainFrame.handleEvent(event);
		}

	}

	private class RemoveExhibitAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			ArrayList<Exhibit> toDelete = new ArrayList<Exhibit>();
			TableItem[] selection = table.getSelection();
			String name = "";
			if (selection.length == 0) {
				return;
			}
			for (int i = 0; i < selection.length; i++) {
				TableItem item = selection[i];
				if (item.getData() instanceof Exhibit) {
					Exhibit exh = (Exhibit) item.getData();
					toDelete.add(exh);
					if (i < 5) {
						name = name + exh.getName() + "\n";
					}
				}
			}
			int many = selection.length - 5;
			String msg = "";
			if (many > 0) {
				msg = String
						.format(ResourceManager
								.getText(OrdoUI.MSG_DELETE_MANY_EXHIBIT), name,
								many);
			} else if (selection.length > 1) {
				msg = String.format(
						ResourceManager.getText(OrdoUI.MSG_DELETE_EXHIBITS),
						name);
			} else {
				msg = String.format(
						ResourceManager.getText(OrdoUI.MSG_DELETE_EXHIBIT),
						name);
			}
			MessageBox messageBox = new MessageBox(getShell(), SWT.YES | SWT.NO);
			messageBox.setText(ResourceManager
					.getText(OrdoUI.MSG_DELETE_EXHIBIT_TITLE));
			messageBox.setMessage(msg);
			int result = messageBox.open();
			if (SWT.YES == result) {
				LogicAccess.deleteExhibits(toDelete);
				UiEvent event = new UiEvent(toDelete,
						UiEventType.ExhibitDeleted);
				MainFrame.handleEvent(event);
			}
		}

	}

	private class TableDragSourceAdapter extends DragSourceAdapter {

		@Override
		public void dragSetData(DragSourceEvent event) {
			StringBuilder stringBuilder = new StringBuilder();
			TableItem[] selection = table.getSelection();
			for (TableItem item : selection) {
				Object data = item.getData();
				if (data instanceof Exhibit) {
					Exhibit exhibit = (Exhibit) item.getData();
					stringBuilder.append("exhibit/");
					stringBuilder.append(exhibit.getId());
				}
				stringBuilder.append(";");
			}
			event.data = stringBuilder.toString();
		}

	}
}
