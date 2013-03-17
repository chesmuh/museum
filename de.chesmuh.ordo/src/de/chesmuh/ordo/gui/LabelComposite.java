package de.chesmuh.ordo.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class LabelComposite extends Composite {

	private Tree tree;
	
	public LabelComposite(Composite parent, int style) {
		super(parent, style);
		initilaize();
	}

	private void initilaize() {
		tree = new Tree(this, SWT.V_SCROLL);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		for (int i = 0; i < 5; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(String.valueOf(i));
			for (int j = 0; j < 3; j++) {
				TreeItem subItem = new TreeItem(item, SWT.NONE);
				subItem.setText(String.valueOf(i) + " " + String.valueOf(j));
			}
		}
		tree.pack();		
	}
}
