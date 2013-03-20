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
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;

public class SectionInformationComposite extends Composite {

	private ResourceBundle bundle;
	private Text textName;
	private Text textParent;
	private Section section;
	private Text textDescription;

	public SectionInformationComposite(Composite parent, Section section) {
		super(parent, SWT.NONE);
		this.bundle = Config.getInstance().getUIBundle();
		this.section = section;
		initilalize();
	}

	private void initilalize() {
		// ----- Layout -----
		GridData gridData = new GridData(SWT.FILL, SWT.UP, true, true);
		this.setLayoutData(gridData);
		this.setLayout(new GridLayout(2, false));

		// ----- Name -----
		Label label = new Label(this, SWT.NONE);
		label.setText(bundle.getString(OrdoUI.DETAIL_SECTION_NAME));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textName = new Text(this, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		textName.setLayoutData(gridData);
		textName.setEditable(false);
		textName.setText(section.getName());

		// ----- Parent -----
		label = new Label(this, SWT.NONE);
		label.setText(bundle.getString(OrdoUI.DETAIL_EXHIBIT_SECTION));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textParent = new Text(this, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		textParent.setLayoutData(gridData);
		textParent.setEditable(false);
		if(null != section.getMuseum()) {
			textParent.setText(section.getMuseum().getName());
			textParent.setData(section.getMuseum());
		}
		if(null != section.getParent()) {
			textParent.setText(section.getParent().getName());
			textParent.setData(section.getParent());
		}
		
		// ----- Description -----
		label = new Label(this, SWT.NONE);
		label.setText(bundle.getString(OrdoUI.DETAIL_MUSEUM_DESCRIPTION));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textDescription = new Text(this, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		textDescription.setLayoutData(gridData);
		textDescription.setEditable(false);
		textDescription.setText(section.getDescription());

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
