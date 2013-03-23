package de.chesmuh.ordo.gui.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.entitys.Tag;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.composites.dialogs.CreateExhibitComposite;
import de.chesmuh.ordo.gui.composites.dialogs.CreateMuseumComposite;
import de.chesmuh.ordo.gui.composites.dialogs.CreateSectionComposite;
import de.chesmuh.ordo.gui.composites.dialogs.CreateTagComposite;
import de.chesmuh.ordo.gui.composites.dialogs.ExhibitInformationComposite;
import de.chesmuh.ordo.gui.composites.dialogs.MuseumInformationComposite;
import de.chesmuh.ordo.gui.composites.dialogs.SectionInformationComposite;
import de.chesmuh.ordo.gui.composites.dialogs.TagInformationComposite;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;

public class DetailComposite extends Composite implements IUiListener {

	private Group group;
	private GridData gridData;
	private boolean saveState;
	private UiEventType eventTypeThatLocked;

	public DetailComposite(Composite parent, int style) {
		super(parent, style);
		saveState = false;
		initialize();
	}

	private void initialize() {
		// ----- Layout -----
		this.setLayout(new GridLayout(1, true));
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 4;
		this.setLayoutData(gridData);

		group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setLayout(new GridLayout(1, false));
		group.setText(ResourceManager.getText(OrdoUI.DETAIL_TITLE_DEFAULT));

		// ----- Listener -----
		MainFrame.addObserver(UiEventType.MuseumSelected, this);
		MainFrame.addObserver(UiEventType.SectionSelected, this);
		MainFrame.addObserver(UiEventType.ExhibitSelected, this);
		MainFrame.addObserver(UiEventType.TagSelected, this);
		MainFrame.addObserver(UiEventType.AddSection, this);
		MainFrame.addObserver(UiEventType.AddTag, this);
		MainFrame.addObserver(UiEventType.AddExhibit, this);
		MainFrame.addObserver(UiEventType.AddSectionCanceled, this);
		MainFrame.addObserver(UiEventType.AddExhibitCanceled, this);
		MainFrame.addObserver(UiEventType.AddTagCanceled, this);
		MainFrame.addObserver(UiEventType.TagAdded, this);
		MainFrame.addObserver(UiEventType.SectionAdded, this);
		MainFrame.addObserver(UiEventType.ExhibitAdded, this);
		MainFrame.addObserver(UiEventType.TagDeleted, this);
		MainFrame.addObserver(UiEventType.MuseumDeleted, this);
		MainFrame.addObserver(UiEventType.SectionDeleted, this);
		MainFrame.addObserver(UiEventType.AddMuseum, this);
		MainFrame.addObserver(UiEventType.MuseumAdded, this);
		MainFrame.addObserver(UiEventType.AddMuseumCanceled, this);
	}

	@Override
	public void handleEvent(UiEvent event) {
		if (saveState) { // Lock and Unlock the Detail-Composite
			switch (event.getType()) {
			case AddSectionCanceled:
			case SectionAdded:
				if (eventTypeThatLocked == UiEventType.AddSection) {
					saveState = false;
				}
				break;
			case AddExhibitCanceled:
			case ExhibitAdded:
				if (eventTypeThatLocked == UiEventType.AddExhibit) {
					saveState = false;
				}
			case AddTagCanceled:
			case TagAdded:
				if (eventTypeThatLocked == UiEventType.AddTag) {
					saveState = false;
				}
			case MuseumAdded:
			case AddMuseumCanceled:
				if(eventTypeThatLocked == UiEventType.AddMuseum) {
					saveState = false;
				}
			case MuseumDeleted:
				saveState = false;
			default:
				break;
			}
		}

		if (!saveState) {
			switch (event.getType()) {
			// ----- Add -----
			case AddExhibit:
				saveState = true;
				eventTypeThatLocked = event.getType();
				showExhibitCreate(event.getData());
				break;
			case AddMuseum:
				saveState = true;
				eventTypeThatLocked = event.getType();
				showMuseumCreate();
				break;
			case AddSection:
				saveState = true;
				eventTypeThatLocked = event.getType();
				showSectionCreate(event.getData());
				break;
			case AddTag:
				saveState = true;
				eventTypeThatLocked = event.getType();
				showTagCreate();
				break;
			// ----- Select -----
			case ExhibitSelected:
				showExhibitInfos(event.getData());
				break;
			case MuseumSelected:
				showMuseumInfos(event.getData());
				break;
			case SectionSelected:
				showSectionInfos(event.getData());
				break;
			case TagSelected:
				showTagInfos(event.getData());
				break;
			// ----- Remove -----
			case RemoveLabel:
				break;
			case RemoveMuseum:
				break;
			case RemoveSection:
				break;
			// ----- Added / Deleted -----
			case SectionAdded:
			case MuseumAdded:
			case AddSectionCanceled:
			case ExhibitAdded:
			case AddExhibitCanceled:
			case TagAdded:
			case AddTagCanceled:
			case TagDeleted:
			case MuseumDeleted:
			case SectionDeleted:
				showNothing();
				break;
			default:
				break;
			}
		}
	}

	private void showMuseumCreate() {
		group.setText(ResourceManager.getText(OrdoUI.ADD_MUSEUM_TITLE));
		clearGroup();
		new CreateMuseumComposite(group);
		group.layout();
	}

	private void showTagInfos(Object data) {
		if (data instanceof Tag) {
			group.setText(ResourceManager.getText(OrdoUI.DETAIL_GROUP_TAG));
			clearGroup();
			new TagInformationComposite(group, (Tag) data);
			group.layout();
		}
	}

	private void showTagCreate() {
		group.setText(ResourceManager.getText(OrdoUI.ADD_TAG_TITLE));
		clearGroup();
		new CreateTagComposite(group);
		group.layout();
	}

	private void showSectionInfos(Object data) {
		if (data instanceof Section) {
			group.setText(ResourceManager.getText(OrdoUI.DETAIL_GROUP_SECTION));
			clearGroup();
			new SectionInformationComposite(group, (Section) data);
			group.layout();
		}
	}

	private void showMuseumInfos(Object data) {
		if (data instanceof Museum) {
			group.setText(ResourceManager.getText(OrdoUI.DETAIL_GROUP_MUSEUM));
			clearGroup();
			new MuseumInformationComposite(group, (Museum) data);
			group.layout();
		}
	}

	private void showExhibitInfos(Object data) {
		if (data instanceof Exhibit) {
			group.setText(ResourceManager.getText(OrdoUI.DETAIL_GROUP_EXHIBIT));
			clearGroup();
			new ExhibitInformationComposite(group, (Exhibit) data);
			group.layout();
		}
	}

	private void showSectionCreate(Object data) {
		group.setText(ResourceManager.getText(OrdoUI.ADD_SECTION_TITLE));
		clearGroup();
		new CreateSectionComposite(group, data);
		group.layout();
	}

	private void showExhibitCreate(Object data) {
		group.setText(ResourceManager.getText(OrdoUI.ADD_EXHIBIT_TITLE));
		clearGroup();
		new CreateExhibitComposite(group, data);
		group.layout();
	}

	private void showNothing() {
		group.setText(ResourceManager.getText(OrdoUI.DETAIL_TITLE_DEFAULT));
		clearGroup();
		group.layout();
	}

	private void clearGroup() {
		while (group.getChildren().length > 0) {
			group.getChildren()[0].dispose();
		}
	}

}
