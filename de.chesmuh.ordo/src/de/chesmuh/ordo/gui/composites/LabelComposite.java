package de.chesmuh.ordo.gui.composites;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.gui.resources.OrdoUI;

public class LabelComposite extends Composite {

	private Tree tree;
	private ResourceBundle bundle;
	
	public LabelComposite(Composite parent, int style) {
		super(parent, style);
		bundle = Config.getInstance().getUIBundle();
		initilaize();
	}

	private void initilaize() {
		// ----- Layout -----
		this.setLayout(new GridLayout(1, true));
		
		Group group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setLayout(new GridLayout(1,true));
		group.setText(bundle.getString(OrdoUI.LABELS_GROUP_TITLE));
		
		tree = new Tree(group, SWT.V_SCROLL);
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
