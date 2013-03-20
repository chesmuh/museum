package de.chesmuh.ordo.gui.composites.dialogs;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;

public class MuseumInformationComposite extends Composite {

	private ResourceBundle bundle;
	private Text textName;
	private Museum museum;
	private Text textDescription;

	public MuseumInformationComposite(Composite parent, Museum museum) {
		super(parent, SWT.NONE);
		this.bundle = Config.getInstance().getUIBundle();
		this.museum = museum;
		initilalize();
	}

	private void initilalize() {
		// ----- Layout -----
		GridData gridData = new GridData(SWT.FILL, SWT.UP, true, true);
		this.setLayoutData(gridData);
		this.setLayout(new GridLayout(2, false));

		// ----- Name -----
		Label label = new Label(this, SWT.NONE);
		label.setText(bundle.getString(OrdoUI.DETAIL_MUSEUM_NAME));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textName = new Text(this, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		textName.setLayoutData(gridData);
		textName.setEditable(false);
		textName.setText(museum.getName());

		// ----- Description -----
		label = new Label(this, SWT.NONE);
		label.setText(bundle.getString(OrdoUI.DETAIL_MUSEUM_DESCRIPTION));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textDescription = new Text(this, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		textDescription.setLayoutData(gridData);
		textDescription.setEditable(false);
		textDescription.setText(museum.getDescription());

		// ----- Buttons -----
		Composite buttonComposite = new Composite(this, SWT.NONE);
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gridData.horizontalSpan = 2;
		buttonComposite.setLayoutData(gridData);
		buttonComposite.setLayout(new GridLayout(2, false));

		Button button = new Button(buttonComposite, SWT.NONE);
		button.setImage(ResourceManager
				.getImage(getDisplay(), OrdoUI.IMAGES_EDIT));
		button.setText(bundle.getString(OrdoUI.DETAIL_MUSEUM_EDIT));
		button.addSelectionListener(new EditSelectionListener());

	}

	private class EditSelectionListener extends SelectionAdapter {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
		}
		
	}
}
