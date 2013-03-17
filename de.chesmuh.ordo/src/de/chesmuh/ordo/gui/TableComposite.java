package de.chesmuh.ordo.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class TableComposite extends Composite {

	public TableComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		final Table table = new Table(this, SWT.MULTI
				| SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		String[] titles = { "First Name", "Last Name", "Age" };
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
