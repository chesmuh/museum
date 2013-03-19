package de.chesmuh.ordo.gui.composites;


import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.gui.resources.OrdoUI;

public class DetailComposite extends Composite {

	private ResourceBundle bundle;
	private List list;
	
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
		
		list = new List(group, SWT.NONE);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		DropTarget dropTarget = new DropTarget(list, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new ListDropAdapter());
	}
	
	private class ListDropAdapter extends DropTargetAdapter {
		@Override
		public void drop(DropTargetEvent event) {
			list.add((String) event.data);
		}
	}

}
