package de.chesmuh.ordo.gui.composites;

import java.util.ArrayList;

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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Tag;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;
import de.chesmuh.ordo.logic.LogicAccess;

public class TagComposite extends Composite implements IUiListener {

	private Tree tree;

	public TagComposite(Composite parent, int style) {
		super(parent, style);
		initilaize();
	}

	private void initilaize() {
		// ----- Layout -----
		this.setLayout(new GridLayout(1, true));

		Group group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setLayout(new GridLayout(1, true));
		group.setText(ResourceManager.getText(OrdoUI.TAGS_GROUP_TITLE));

		// ----- Buttons -----
		ToolBar toolBar = new ToolBar(group, SWT.NONE);
		ToolItem toolItemAdd = new ToolItem(toolBar, SWT.PUSH);
		toolItemAdd.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_ADD));
		toolItemAdd.addSelectionListener(new AddTagSelectionAdapter());
		ToolItem toolItemRemove = new ToolItem(toolBar, SWT.PUSH);
		toolItemRemove.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_DELETE));
		toolItemRemove.addSelectionListener(new RemoveTagSelectionAdapter());

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

		// ----- Listener -----
		MainFrame.addObserver(UiEventType.TagAdded, this);
		MainFrame.addObserver(UiEventType.TagDeleted, this);

	}

	@Override
	public void handleEvent(UiEvent event) {
		switch (event.getType()) {
		case TagAdded:
		case TagDeleted:
			refreshTree();
			break;
		default:
			break;
		}
	}

	private void refreshTree() {
		tree.removeAll();
		for (Tag label : DataAccess.getInstance().getAllLabels()) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(label.getName());
			item.setData(label);
		}
	}

	private class TreeDragSourceAdapter extends DragSourceAdapter {

		@Override
		public void dragSetData(DragSourceEvent event) {
			StringBuilder stringBuilder = new StringBuilder();
			TreeItem[] selection = tree.getSelection();
			for (TreeItem item : selection) {
				Object object = item.getData();
				if (object instanceof Tag) {
					de.chesmuh.ordo.entitys.Tag label = (Tag) object;
					stringBuilder.append("tag/");
					stringBuilder.append(Long.toString(label.getId()));
				}
				stringBuilder.append(";");
			}
			event.data = stringBuilder.toString();

		}

	}

	private class TreeSelectionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			TreeItem selection = tree.getSelection()[0];
			if (selection.getData() instanceof Tag) {
				UiEvent uiEvent = new UiEvent(selection.getData(),
						UiEventType.TagSelected);
				MainFrame.handleEvent(uiEvent);
			}
		}

	}

	private class AddTagSelectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			UiEvent event = new UiEvent(null, UiEventType.AddTag);
			MainFrame.handleEvent(event);
		}

	}

	private class RemoveTagSelectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			ArrayList<Tag> toDelete = new ArrayList<Tag>();
			TreeItem[] selection = tree.getSelection();
			String name = "";
			if (selection.length == 0) {
				return;
			}
			for (int i = 0; i < selection.length; i++) {
				TreeItem item = selection[i];
				if (item.getData() instanceof Tag) {
					Tag tag = (Tag) item.getData();
					toDelete.add(tag);
					if (i < 5) {
						name = name + tag.getName() + "\n";
					}
				}
			}
			int many = selection.length - 5;
			String msg = "";
			if (many > 0) {
				msg = String.format(
						ResourceManager.getText(OrdoUI.MSG_DELETE_MANY_TAG),
						name, many);
			} else if (selection.length > 1) {
				msg = String.format(
						ResourceManager.getText(OrdoUI.MSG_DELETE_TAG), name);
			} else {
				msg = String.format(
						ResourceManager.getText(OrdoUI.MSG_DELETE_TAG), name);
			}
			MessageBox messageBox = new MessageBox(getShell(), SWT.YES | SWT.NO);
			messageBox.setText(ResourceManager
					.getText(OrdoUI.MSG_DELETE_TAG_TITLE));
			messageBox.setMessage(msg);
			int result = messageBox.open();
			if (SWT.YES == result) {
				LogicAccess.deleteTags(toDelete);
				UiEvent event = new UiEvent(toDelete, UiEventType.TagDeleted);
				MainFrame.handleEvent(event);
			}
		}
	}

}
