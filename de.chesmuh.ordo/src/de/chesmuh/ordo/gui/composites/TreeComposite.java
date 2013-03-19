package de.chesmuh.ordo.gui.composites;

import java.util.Collection;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;

public class TreeComposite extends Composite {

	private Tree tree;
	private ResourceBundle bundle;

	public TreeComposite(Composite parent, int style) {
		super(parent, style);
		bundle = Config.getInstance().getUIBundle();
		this.initialize();
	}

	private void initialize() {
		// ----- Layout -----
		this.setLayout(new GridLayout(1, true));
		
		Group group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setLayout(new GridLayout(1,true));
		group.setText(bundle.getString(OrdoUI.SECTION_GROUP_TITLE));
		
		// ----- Buttons -----
		ToolBar toolBar = new ToolBar(group, SWT.NONE);
		ToolItem toolItemAdd = new ToolItem(toolBar, SWT.PUSH);
		toolItemAdd.setImage(ResourceManager.getImage(getDisplay(), OrdoUI.IMAGES_ADD));
		ToolItem toolItemRemove = new ToolItem(toolBar, SWT.PUSH);
		toolItemRemove.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_REMOVE));
		ToolItem toolItemSwap = new ToolItem(toolBar, SWT.PUSH);
		toolItemSwap.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_SWAP));
		
		// ----- Tree -----
		tree = new Tree(group, SWT.V_SCROLL);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tree.addListener(SWT.Selection, new TreeListener());

		refreshTree();
	}

	private void refreshTree() {
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

		tree.pack();
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
			if(selection.getData() instanceof Section) {
				UiEvent uiEvent = new UiEvent(tree, selection.getData(), UiEventType.SectionChoose);
				MainFrame.handleEvent(uiEvent);
			} else if(selection.getData() instanceof Museum) {
				UiEvent uiEvent = new UiEvent(tree, selection.getData(), UiEventType.MuseumChoose);
				MainFrame.handleEvent(uiEvent);
			}
		}

	}
}
