package de.chesmuh.ordo.gui.composites.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.exceptions.EmptyNameException;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;
import de.chesmuh.ordo.logic.LogicAccess;

public class MuseumInformationComposite extends Composite {

	private Text textName;
	private Museum museum;
	private Text textDescription;
	private Composite buttonComposite;

	public MuseumInformationComposite(Composite parent, Museum museum) {
		super(parent, SWT.NONE);
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
		label.setText(ResourceManager.getText(OrdoUI.DETAIL_MUSEUM_NAME));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textName = new Text(this, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		textName.setLayoutData(gridData);
		textName.setEditable(false);
		textName.setText(museum.getName());

		// ----- Description -----
		label = new Label(this, SWT.NONE);
		label.setText(ResourceManager.getText(OrdoUI.DETAIL_MUSEUM_DESCRIPTION));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textDescription = new Text(this, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		textDescription.setLayoutData(gridData);
		textDescription.setEditable(false);
		textDescription.setText(museum.getDescription());

		// ----- Buttons -----
		buttonComposite = new Composite(this, SWT.NONE);
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gridData.horizontalSpan = 2;
		buttonComposite.setLayoutData(gridData);
		buttonComposite.setLayout(new GridLayout(2, false));

		Button button = new Button(buttonComposite, SWT.NONE);
		button.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_EDIT));
		button.setText(ResourceManager.getText(OrdoUI.BUTTON_EDIT));
		button.addSelectionListener(new EditSelectionAdapter());

	}

	private class EditSelectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			while (buttonComposite.getChildren().length > 0) {
				buttonComposite.getChildren()[0].dispose();
			}
			Button button = new Button(buttonComposite, SWT.NONE);
			button.setImage(ResourceManager.getImage(getDisplay(),
					OrdoUI.IMAGES_OK));
			button.setText(ResourceManager.getText(OrdoUI.BUTTON_SAVE));
			button.addSelectionListener(new SaveSelectionAdapter());

			button = new Button(buttonComposite, SWT.NONE);
			button.setImage(ResourceManager.getImage(getDisplay(),
					OrdoUI.IMAGES_CANCEL));
			button.setText(ResourceManager.getText(OrdoUI.BUTTON_CANCEL));
			button.addSelectionListener(new CancelSelectionAdapter());

			textName.setEditable(true);
			textDescription.setEditable(true);
			
			buttonComposite.layout();
			MuseumInformationComposite.this.layout();

			UiEvent event = new UiEvent(null, UiEventType.EditMuseum);
			MainFrame.handleEvent(event);
		}

	}

	private class SaveSelectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (null == textName.getText() || textName.getText().isEmpty()) {
				MessageBox messageBox = new MessageBox(getShell(),
						SWT.ICON_ERROR);
				messageBox.setText(ResourceManager
						.getText(OrdoUI.ERROR_NAME_EMPTY_TITLE));
				messageBox.setMessage(ResourceManager
						.getText(OrdoUI.ERROR_NAME_EMPTY));
				messageBox.open();
			} else {
				try {
					Museum museum = LogicAccess.updateMuseum(textName.getText(), textDescription.getText(), MuseumInformationComposite.this.museum);
					UiEvent event = new UiEvent(museum, UiEventType.MuseumAdded);
					MainFrame.handleEvent(event);
				} catch (EmptyNameException exn) {
					MessageBox messageBox = new MessageBox(getShell(),
							SWT.ICON_ERROR);
					messageBox.setText(ResourceManager
							.getText(OrdoUI.ERROR_NAME_EMPTY_TITLE));
					messageBox.setMessage(ResourceManager
							.getText(OrdoUI.ERROR_NAME_EMPTY));
					messageBox.open();
				}
			}
		}

	}

	private class CancelSelectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			UiEvent event = new UiEvent(null, UiEventType.EditMuseumCanceled);
			MainFrame.handleEvent(event);
		}

	}
}
