package de.chesmuh.ordo.gui.composites;

import java.util.Collection;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;
import de.chesmuh.ordo.logic.LogicAccess;

public class SectionComposite extends Composite implements IUiListener {

	private Tree tree;

	public SectionComposite(Composite parent, int style) {
		super(parent, style);
		this.initialize();
	}

	private void initialize() {
		// ----- Layout -----
		this.setLayout(new GridLayout(1, true));

		Group group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setLayout(new GridLayout(1, true));
		group.setText(ResourceManager.getText(OrdoUI.SECTION_TITLE));

		// ----- Buttons -----
		ToolBar toolBar = new ToolBar(group, SWT.NONE);
		ToolItem toolItemAdd = new ToolItem(toolBar, SWT.PUSH);
		toolItemAdd.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_ADD));
		toolItemAdd.addSelectionListener(new AddSectionAdapter());
		ToolItem toolItemRemove = new ToolItem(toolBar, SWT.PUSH);
		toolItemRemove.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_DELETE));
		toolItemRemove.addSelectionListener(new RemoveSectionAdapter());

		// ----- Tree -----
		tree = new Tree(group, SWT.V_SCROLL);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tree.addListener(SWT.Selection, new TreeListener());

		// ----- DragSource -----
		DragSource dragSource = new DragSource(tree, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new TreeDragSourceAdapter());

		// ----- Listener -----
		MainFrame.addObserver(UiEventType.SectionAdded, this);
		MainFrame.addObserver(UiEventType.SectionDeleted, this);
		MainFrame.addObserver(UiEventType.RemoveMuseum, this);
		MainFrame.addObserver(UiEventType.MuseumDeleted, this);
		MainFrame.addObserver(UiEventType.MuseumAdded, this);

		refreshTree();
	}

	@Override
	public void handleEvent(UiEvent event) {
		switch (event.getType()) {
		case RemoveMuseum:
			this.removeMuseum();
			break;
		case SectionDeleted:
		case SectionAdded:
		case MuseumDeleted:
		case MuseumAdded:
			this.refreshTree();
			break;
		default:
			break;
		}
	}

	private void removeMuseum() {
		TreeItem[] selection = tree.getSelection();
		if (selection.length != 1) {
			MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR);
			messageBox.setMessage(ResourceManager
					.getText(OrdoUI.ERROR_SELECTONEMUSEUM));
			messageBox.setText(ResourceManager
					.getText(OrdoUI.ERROR_SELECTONEMUSEUM_TITLE));
			messageBox.open();
			return;
		}
		if (!(selection[0].getData() instanceof Museum)) {
			MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR);
			messageBox.setMessage(ResourceManager
					.getText(OrdoUI.ERROR_NOMUSEUMSELECTED));
			messageBox.setText(ResourceManager
					.getText(OrdoUI.ERROR_NOMUSEUMSELECTED_TITLE));
			messageBox.open();
			return;
		}
		Museum museum = (Museum) selection[0].getData();
		MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
		messageBox.setMessage(String.format(
				ResourceManager.getText(OrdoUI.MSG_DELETE_MUSEUM),
				museum.getName()));
		messageBox.setText(ResourceManager
				.getText(OrdoUI.MSG_DELETE_MUSEUM_TITLE));
		int result = messageBox.open();
		if (SWT.YES == result) {
			messageBox = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES
					| SWT.NO);
			messageBox.setMessage(ResourceManager
					.getText(OrdoUI.MSG_DELETE_ALL_EXHIBITS));
			messageBox.setText(ResourceManager
					.getText(OrdoUI.MSG_DELETE_ALL_EXHIBITS_TITLE));
			result = messageBox.open();
			if (SWT.YES == result) {
				LogicAccess.deleteMuseum(museum);
				UiEvent event = new UiEvent(museum, UiEventType.MuseumDeleted);
				MainFrame.handleEvent(event);
			}
		}
	}

	private void refreshTree() {
		while (tree.getItemCount() > 0) {
			tree.getItems()[0].dispose();
		}

		Collection<Museum> allMuseum = DataAccess.getInstance().getAllMuseum();
		for (Museum museum : allMuseum) {
			TreeItem museumItem = new TreeItem(tree, SWT.NONE);
			museumItem.setData(museum);
			museumItem.setText(museum.getName());
			museumItem.setImage(ResourceManager.getImage(getDisplay(),
					OrdoUI.IMAGES_MUSEUM));

			Collection<Section> allSection = DataAccess.getInstance()
					.getSectionByMuseumWithNoParent(museum);
			for (Section section : allSection) {
				TreeItem sectionItem = new TreeItem(museumItem, SWT.NONE);
				sectionItem.setData(section);
				sectionItem.setText(section.getName());
				sectionItem.setImage(ResourceManager.getImage(getDisplay(),
						OrdoUI.IMAGES_SECTION));
				addSubSection(sectionItem);
			}
		}
		expandAll();
	}

	public void expandAll() {
		for (TreeItem item : tree.getItems()) {
			item.setExpanded(true);
			expand(item.getItems());
		}
	}

	public void expand(TreeItem[] items) {
		for (TreeItem item : items) {
			item.setExpanded(true);
			expand(item.getItems());
		}
	}

	private void addSubSection(TreeItem item) {
		Section section = (Section) item.getData();
		Collection<Section> allSection = DataAccess.getInstance()
				.getSectionBySection(section);
		for (Section subSection : allSection) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setData(subSection);
			subItem.setText(subSection.getName());
			subItem.setImage(ResourceManager.getImage(getDisplay(),
					OrdoUI.IMAGES_SECTION));
			addSubSection(subItem);
		}
	}

	private class TreeListener implements Listener {

		@Override
		public void handleEvent(Event event) {
			TreeItem selection = tree.getSelection()[0];
			if (selection.getData() instanceof Section) {
				UiEvent uiEvent = new UiEvent(selection.getData(),
						UiEventType.SectionSelected);
				MainFrame.handleEvent(uiEvent);
			} else if (selection.getData() instanceof Museum) {
				UiEvent uiEvent = new UiEvent(selection.getData(),
						UiEventType.MuseumSelected);
				MainFrame.handleEvent(uiEvent);
			}
		}

	}

	private class AddSectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			UiEvent event;
			if (tree.getSelection().length > 0) {
				TreeItem treeItem = tree.getSelection()[0];
				event = new UiEvent(treeItem.getData(), UiEventType.AddSection);
			} else {
				event = new UiEvent(null, UiEventType.AddSection);
			}
			MainFrame.handleEvent(event);
		}

	}

	private class RemoveSectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			TreeItem[] selection = tree.getSelection();
			if (selection.length != 1) {
				return;
			}
			TreeItem item = selection[0];
			Section toDelete = (Section) item.getData();
			MessageBox messageBox = new MessageBox(getShell(), SWT.YES | SWT.NO);
			messageBox.setText(ResourceManager
					.getText(OrdoUI.MSG_DELETE_SECTION_TITLE));
			messageBox.setMessage(ResourceManager.getText(OrdoUI.MSG_DELETE_SECTION));
			int result = messageBox.open();
			if (SWT.YES == result) {
				LogicAccess.deleteSection(toDelete);
				UiEvent event = new UiEvent(toDelete, UiEventType.SectionDeleted);
				MainFrame.handleEvent(event);
			}
		}

	}

	private class TreeDragSourceAdapter extends DragSourceAdapter {

		@Override
		public void dragSetData(DragSourceEvent event) {
			StringBuilder stringBuilder = new StringBuilder();
			Object object = tree.getSelection()[0].getData();
			if (object instanceof Section) {
				stringBuilder.append("section/");
				stringBuilder.append(Long.toString(((Section) object).getId()));
			} else if (object instanceof Museum) {
				stringBuilder.append("museum/");
				stringBuilder.append(Long.toString(((Museum) object).getId()));
			} else {
				return;
			}
			event.data = stringBuilder.toString();
		}

	}

}
