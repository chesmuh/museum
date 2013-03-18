package de.chesmuh.ordo.gui.composites;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.UiEventType;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.resources.OrdoUI;

public class TableComposite extends Composite implements IUiListener {

	private Table table;
	private ResourceBundle bundle;

	public TableComposite(Composite parent, int style) {
		super(parent, style);
		bundle = Config.getInstance().getUIBundle();
		initialize();
	}

	private void initialize() {
		// ----- Layout -----
		this.setLayout(new GridLayout(1, true));

		table = new Table(this, SWT.MULTI | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);
		
		MainFrame.addObserver(UiEventType.MuseumChoose, this);
	}

	@Override
	public void handleEvent(UiEvent event) {
		UiEventType eventType = event.getType();
		switch (eventType) {
		case ExhibitChoose:
			break;
		case MuseumChoose:
			if (event.getData() instanceof Museum) {
				Museum museum = (Museum) event.getData();
				showSectionByMuseum(museum);
			}
			break;
		case SectionChoose:
			break;
		default:
			break;

		}
	}

	private void showSectionByMuseum(Museum museum) {
		String headerName = bundle.getString(OrdoUI.TABLE_HEADERS_NAME);
		String headerMuseum = bundle.getString(OrdoUI.TABLE_HEADERS_MUSEUM);

		Collection<Section> allSectionsByMuseum = DataAccess.getInstance().getAllSectionsByMuseum(museum);
		Collections.sort((List<Section>) allSectionsByMuseum);

		String[] titles = { headerName, headerMuseum };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
			table.getColumn(i).pack();
		}
		
		for(Section section : allSectionsByMuseum) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, section.getName());
			item.setText(1, section.getMuseum().getName());
			item.setData(section);
		}

		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}
	}
}
