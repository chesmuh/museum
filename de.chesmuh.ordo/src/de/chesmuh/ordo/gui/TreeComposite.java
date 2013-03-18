package de.chesmuh.ordo.gui;

import java.io.InputStream;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entity.Museum;
import de.chesmuh.ordo.entity.Section;

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

		Collection<Museum> allMuseum = DataAccess.getInstance().getAllMuseum();
		for (Museum museum : allMuseum) {
			TreeItem museumItem = new TreeItem(tree, SWT.NONE);
			museumItem.setData(museum);
			museumItem.setText(museum.getName());
			InputStream museumStream = TreeComposite.class.getResourceAsStream("home.png");
			Image museumImage = new Image(getDisplay(), museumStream);
			museumItem.setImage(museumImage);
			
			Collection<Section> allSection = DataAccess.getInstance()
					.getAllSectionByMuseumWithNoParent(museum);
			for (Section section : allSection) {
				TreeItem sectionItem = new TreeItem(museumItem, SWT.NONE);
				sectionItem.setData(section);
				sectionItem.setText(section.getName());
				InputStream sectionStream = TreeComposite.class.getResourceAsStream("book.png");
				Image sectionImage = new Image(getDisplay(), sectionStream);
				sectionItem.setImage(sectionImage);
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
			InputStream resourceAsStream = TreeComposite.class.getResourceAsStream("book.png");
			Image image = new Image(getDisplay(), resourceAsStream);
			subItem.setImage(image);
			addSubSection(subItem);
		}
	}
}
