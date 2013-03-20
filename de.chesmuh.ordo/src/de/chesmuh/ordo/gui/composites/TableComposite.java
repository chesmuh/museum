package de.chesmuh.ordo.gui.composites;

import java.util.Collection;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;

public class TableComposite extends Composite implements IUiListener {

	private Table table;
	private ResourceBundle bundle;
	private Group group;

	public TableComposite(Composite parent, int style) {
		super(parent, style);
		bundle = Config.getInstance().getUIBundle();
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
		ToolItem toolItemRemove = new ToolItem(toolBar, SWT.PUSH);
		toolItemRemove.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_REMOVE));

		// ----- Table -----
		table = new Table(group, SWT.MULTI | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		// ----- Listener -----
		MainFrame.addObserver(UiEventType.MuseumChoose, this);
		MainFrame.addObserver(UiEventType.SectionChoose, this);
	}

	@Override
	public void handleEvent(UiEvent event) {
		UiEventType eventType = event.getType();
		switch (eventType) {
		case MuseumChoose:
			if (event.getData() instanceof Museum) {
				Museum museum = (Museum) event.getData();
				showSectionByMuseum(museum);
			}
			break;
		case SectionChoose:
			if (event.getData() instanceof Section) {
				Section section = (Section) event.getData();
				showExhibitsBySection(section);
			}
			break;
		default:
			break;
		}
	}

	private void showExhibitsBySection(Section section) {
		table.setRedraw(false);
		group.setText(bundle.getString(OrdoUI.TABLE_GROUP_EXHIBIT));
		deleteAllColumn();
		deleteAllItems();
		addColumns(new String[] { bundle.getString(OrdoUI.TABLE_HEADERS_NAME),
				bundle.getString(OrdoUI.TABLE_HEADERS_SECTION),
				bundle.getString(OrdoUI.TABLE_HEADERS_DESCRIPTION) });
		Collection<Exhibit> exhibitBySection = DataAccess.getInstance()
				.getExhibitBySection(section);
		for (Exhibit exhibit : exhibitBySection) {
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

	private void showSectionByMuseum(Museum museum) {
		table.setRedraw(false);
		group.setText(bundle.getString(OrdoUI.TABLE_GROUP_SECTION));
		deleteAllColumn();
		deleteAllItems();
		addColumns(new String[] { bundle.getString(OrdoUI.TABLE_HEADERS_NAME),
				bundle.getString(OrdoUI.TABLE_HEADERS_PARENT_SECTION),
				bundle.getString(OrdoUI.TABLE_HEADERS_MUSEUM) });
		Collection<Section> sectionsByMuseum = DataAccess.getInstance()
				.getSectionsByMuseum(museum);
		for (Section section : sectionsByMuseum) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, section.getName());
			item.setData(section);
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

}
