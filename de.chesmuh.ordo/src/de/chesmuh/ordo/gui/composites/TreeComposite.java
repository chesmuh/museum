package de.chesmuh.ordo.gui.composites;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.UiEventType;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;

public class TreeComposite extends Composite {

	private Tree tree;

	public TreeComposite(Composite parent, int style) {
		super(parent, style);
		this.initialize();
	}

	private void initialize() {
		// ----- Layout -----
		this.setLayout(new GridLayout(1, true));

		tree = new Tree(this, SWT.V_SCROLL);
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
					.getAllSectionByMuseumWithNoParent(museum);
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
				.getAllSectionBySection(section);
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
