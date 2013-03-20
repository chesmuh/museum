package de.chesmuh.ordo.gui.composites;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;

public class DetailComposite extends Composite implements IUiListener {

	private ResourceBundle bundle;
	private Group group;
	private GridData gridData;

	public DetailComposite(Composite parent, int style) {
		super(parent, style);
		bundle = Config.getInstance().getUIBundle();
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
		group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_TITLE));

		// ----- Listener -----
		MainFrame.addObserver(UiEventType.AddSection, this);
	}

	@Override
	public void handleEvent(UiEvent event) {
		switch (event.getType()) {
		case AddExhibit:
			break;
		case AddLabel:
			break;
		case AddMuseum:
			break;
		case AddSection:
			showNewSection(event.getData());
			break;
		case ExhibitChoose:
			break;
		case MuseumChoose:
			break;
		case RemoveLabel:
			break;
		case RemoveMuseum:
			break;
		case RemoveSection:
			break;
		case SectionChoose:
			break;
		default:
			break;
		}
	}

	private void showNewSection(Object data) {
		group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_ADD_SECTION));
		while(group.getChildren().length > 0) {
			group.getChildren()[0].dispose();
		}
		new AddSectionComposite(group, SWT.NONE);
		group.layout();
	}

}
