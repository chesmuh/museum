package de.chesmuh.ordo.gui.composites;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
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
import de.chesmuh.ordo.entitys.Tag;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;
import de.chesmuh.ordo.logic.LogicAccess;

public class TableComposite extends Composite implements IUiListener {

	private Table table;
	private Group group;
	private Object criterion;

	public TableComposite(Composite parent, int style) {
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
				OrdoUI.IMAGES_REMOVE));
		toolItemRemove.addSelectionListener(new RemoveExhibitAdapter());

		// ----- Table -----
		table = new Table(group, SWT.MULTI | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);
		table.addSelectionListener(new TableSelectionAdapter());

		// ----- Listener -----
		MainFrame.addObserver(UiEventType.SectionSelected, this);
		MainFrame.addObserver(UiEventType.ExhibitAdded, this);
		MainFrame.addObserver(UiEventType.LabelSelected, this);
		MainFrame.addObserver(UiEventType.ExhibitDeleted, this);
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
				showExhibitsBySection(((Exhibit) event.getData()).getSection());
			}
			break;
		case LabelSelected:
			if (event.getData() instanceof Tag) {
				showExhibitsByLabel((Tag) event.getData());
			}
			break;
		case ExhibitDeleted:
			refreshTable();
			break;
		default:
			break;
		}
	}

	private void showExhibitsByLabel(Tag label) {
		criterion = label;
		table.setRedraw(false);
		group.setText(ResourceManager.getText(OrdoUI.TABLE_GROUP_EXHIBIT));
		deleteAllColumn();
		deleteAllItems();
		addColumns(new String[] { ResourceManager.getText(OrdoUI.TABLE_HEADER_NAME),
				ResourceManager.getText(OrdoUI.TABLE_HEADER_SECTION),
				ResourceManager.getText(OrdoUI.TABLE_HEADER_DESCRIPTION) });
		Collection<Exhibit> exhibits = label.getExhibits();
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
		addColumns(new String[] { ResourceManager.getText(OrdoUI.TABLE_HEADER_NAME),
				ResourceManager.getText(OrdoUI.TABLE_HEADER_SECTION),
				ResourceManager.getText(OrdoUI.TABLE_HEADER_DESCRIPTION) });
		Collection<Exhibit> exhibits = DataAccess.getInstance().getExhibitBySection(section);
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
		if(null == criterion) {
			table.setRedraw(false);
			deleteAllColumn();
			deleteAllItems();
			table.setRedraw(true);
		} else if(criterion instanceof Section) {
			this.showExhibitsBySection((Section) criterion);
		} else if(criterion instanceof Tag) {
			this.showExhibitsByLabel((Tag) criterion);
		}
	}

	private class TableSelectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (table.getSelection().length > 0) {
				Object data = table.getSelection()[0].getData();
				if (data instanceof Exhibit) {
					UiEvent event = new UiEvent(TableComposite.this, data,
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
			event = new UiEvent(null, null, UiEventType.AddExhibit);
			MainFrame.handleEvent(event);
		}

	}
	
	private class RemoveExhibitAdapter extends SelectionAdapter {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			ArrayList<Exhibit> toDelete = new ArrayList<Exhibit>();
			TableItem[] selection = table.getSelection();
			String name = "";
			for(int i = 0; i < selection.length; i++) {
				TableItem item = selection[i];
				if(item.getData() instanceof Exhibit) {
					Exhibit exh = (Exhibit) item.getData();
					toDelete.add(exh);
					if(i < 5) {
						name = name + exh.getName() + "\n";	
					}
				}
			}			
			int many = selection.length - 5;
			String msg = "";
			if(many > 0) {
				msg = String.format(ResourceManager.getText(OrdoUI.MSG_DELETE_MANY_EXHIBIT), name, many); 
			} else if(selection.length > 1) {
				msg = String.format(ResourceManager.getText(OrdoUI.MSG_DELETE_EXHIBITS), name);
			} else {
				msg = String.format(ResourceManager.getText(OrdoUI.MSG_DELETE_EXHIBIT), name);
			}
			MessageBox messageBox = new MessageBox(getShell(), SWT.YES | SWT.NO);
			messageBox.setText(ResourceManager.getText(OrdoUI.MSG_DELETE_EXHIBIT_TITLE));
			messageBox.setMessage(msg);
			int result = messageBox.open();
			if(SWT.YES == result) {
				LogicAccess.deleteExhibits(toDelete);
				UiEvent event = new UiEvent(table, toDelete, UiEventType.ExhibitDeleted);
				MainFrame.handleEvent(event);
			}
		}
		
	}
}
