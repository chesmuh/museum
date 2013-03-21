package de.chesmuh.ordo.gui.composites;

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
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Label;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;

public class LabelComposite extends Composite {

	private Tree tree;

	public LabelComposite(Composite parent, int style) {
		super(parent, style);
		initilaize();
	}

	private void initilaize() {
		// ----- Layout -----
		this.setLayout(new GridLayout(1, true));

		Group group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setLayout(new GridLayout(1, true));
		group.setText(ResourceManager.getText(OrdoUI.LABELS_GROUP_TITLE));

		// ----- Buttons -----
		ToolBar toolBar = new ToolBar(group, SWT.NONE);
		ToolItem toolItemAdd = new ToolItem(toolBar, SWT.PUSH);
		toolItemAdd.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_ADD));
		ToolItem toolItemRemove = new ToolItem(toolBar, SWT.PUSH);
		toolItemRemove.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_REMOVE));

		// ----- Tree -----
		tree = new Tree(group, SWT.V_SCROLL | SWT.MULTI);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		refreshTree();
		tree.addSelectionListener(new TreeSelectionListener());
		tree.pack();

		// ----- DragSouce -----
		DragSource dragSource = new DragSource(tree, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new TreeDragSourceAdapter());

	}

	private void refreshTree() {
		for(Label label : DataAccess.getInstance().getAllLabels()) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(label.getName());
			item.setData(label);
		}
	}

	private class TreeDragSourceAdapter extends DragSourceAdapter {

		@Override
		public void dragSetData(DragSourceEvent event) {
			event.data = tree.getSelection()[0].getText();
		}

	}
	
	
	private class TreeSelectionListener extends SelectionAdapter {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			TreeItem selection = tree.getSelection()[0];
			if (selection.getData() instanceof Label) {
				UiEvent uiEvent = new UiEvent(tree, selection.getData(),
						UiEventType.LabelSelected);
				MainFrame.handleEvent(uiEvent);
			}
		}
		
	}
}
