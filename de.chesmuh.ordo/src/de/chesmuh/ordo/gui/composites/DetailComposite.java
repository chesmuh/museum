package de.chesmuh.ordo.gui.composites;


import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.gui.resources.OrdoUI;

public class DetailComposite extends Composite {

	private ResourceBundle bundle;
	
	public DetailComposite(Composite parent, int style) {
		super(parent, style);
		bundle = Config.getInstance().getUIBundle();
		initialize();
	}

	private void initialize() {
		// ----- Layout -----
		this.setLayout(new GridLayout(4, false));
		
		Group group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setLayout(new GridLayout(1,true));
		group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_TITLE));
	}

}
