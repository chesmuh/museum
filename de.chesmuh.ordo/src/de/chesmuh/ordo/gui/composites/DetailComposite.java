package de.chesmuh.ordo.gui.composites;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.composites.dialogs.CreateExhibitComposite;
import de.chesmuh.ordo.gui.composites.dialogs.CreateSectionComposite;
import de.chesmuh.ordo.gui.composites.dialogs.ExhibitInformationComposite;
import de.chesmuh.ordo.gui.composites.dialogs.MuseumInformationComposite;
import de.chesmuh.ordo.gui.composites.dialogs.SectionInformationComposite;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;

public class DetailComposite extends Composite implements IUiListener {

	private ResourceBundle bundle;
	private Group group;
	private GridData gridData;
	private boolean saveState;
	private UiEventType eventTypeThatLocked;

	public DetailComposite(Composite parent, int style) {
		super(parent, style);
		bundle = Config.getInstance().getUIBundle();
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
		group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_DEFAULT_TITLE));

		// ----- Listener -----
		MainFrame.addObserver(UiEventType.AddSection, this);
		MainFrame.addObserver(UiEventType.SectionAdded, this);
		MainFrame.addObserver(UiEventType.AddSectionCanceled, this);
		MainFrame.addObserver(UiEventType.ExhibitSelected, this);
		MainFrame.addObserver(UiEventType.MuseumSelected, this);
		MainFrame.addObserver(UiEventType.SectionSelected, this);
		MainFrame.addObserver(UiEventType.AddExhibit, this);
		MainFrame.addObserver(UiEventType.ExhibitAdded, this);
		MainFrame.addObserver(UiEventType.AddExhibitCanceled, this);
	}

	@Override
	public void handleEvent(UiEvent event) {
		if(saveState) { // Lock and Unlock the Detail-Composite
			switch(event.getType()) {
			case AddSectionCanceled:
			case SectionAdded:
				if(eventTypeThatLocked == UiEventType.AddSection) {
					saveState = false;
				}
				break;
			case AddExhibitCanceled:
			case ExhibitAdded:
				if(eventTypeThatLocked == UiEventType.AddExhibit) {
					saveState = false;
				}
			default:
				break;
			}
		}
		
		if (!saveState) {
			switch (event.getType()) {
			case AddExhibit:
				saveState = true;
				eventTypeThatLocked = event.getType();
				showExhibitCreate(event.getData());
				break;
			case AddLabel:
				break;
			case AddMuseum:
				break;
			case AddSection:
				saveState = true;
				eventTypeThatLocked = event.getType();
				showSectionCreate(event.getData());
				break;
			case ExhibitSelected:
				showExhibitInfos(event.getData());
				break;
			case MuseumSelected:
				showMuseumInfos(event.getData());
				break;
			case RemoveLabel:
				break;
			case RemoveMuseum:
				break;
			case RemoveSection:
				break;
			case SectionSelected:
				showSectionInfos(event.getData());
				break;
			case SectionAdded:
			case AddSectionCanceled:
			case ExhibitAdded:
			case AddExhibitCanceled:
				showNothing();
				break;
			default:
				break;
			}
		}
	}


	private void showSectionInfos(Object data) {
		if (data instanceof Section) {
			group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_SECTION));
			clearGroup();
			new SectionInformationComposite(group, (Section) data);
			group.layout();
		}
	}

	private void showMuseumInfos(Object data) {
		if (data instanceof Museum) {
			group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_MUSEUM));
			clearGroup();
			new MuseumInformationComposite(group, (Museum) data);
			group.layout();
		}
	}

	private void showExhibitInfos(Object data) {
		if (data instanceof Exhibit) {
			group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_EXHIBIT));
			clearGroup();
			new ExhibitInformationComposite(group, (Exhibit) data);
			group.layout();
		}
	}

	private void showSectionCreate(Object data) {
		group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_ADD_SECTION));
		clearGroup();
		new CreateSectionComposite(group, data);
		group.layout();
	}

	private void showExhibitCreate(Object data) {
		group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_ADD_EXHIBIT));
		clearGroup();
		new CreateExhibitComposite(group, data);
		group.layout();
	}

	private void showNothing() {
		group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_DEFAULT_TITLE));
		clearGroup();
		group.layout();
	}

	private void clearGroup() {
		while (group.getChildren().length > 0) {
			group.getChildren()[0].dispose();
		}
	}

}
