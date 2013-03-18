package de.chesmuh.ordo.gui;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.gui.resources.OrdoUI;

public class TableComposite extends Composite {

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

		String name = bundle.getString(OrdoUI.TABLE_HEADERS_NAME);
		String section = bundle.getString(OrdoUI.TABLE_HEADERS_SECTION);
		String category = bundle.getString(OrdoUI.TABLE_HEADERS_CATEGORY);
		
		String[] titles = { name, section, category};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
			table.getColumn(i).pack();
		}

		for (int i = 0; i <= 50; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "Person " + i);
			item.setText(1, "LastName " + i);
			item.setText(2, String.valueOf(i));
		}

		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}
	}

}
