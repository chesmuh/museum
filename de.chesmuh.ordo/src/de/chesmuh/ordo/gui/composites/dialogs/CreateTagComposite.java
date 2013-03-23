package de.chesmuh.ordo.gui.composites.dialogs;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Tag;
import de.chesmuh.ordo.exceptions.EmptyNameException;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;
import de.chesmuh.ordo.logic.LogicAccess;

public class CreateTagComposite extends Composite {

	private Text textName;
	private List listExhibits;
	private ArrayList<Exhibit> exhibits;

	public CreateTagComposite(Composite parent) {
		super(parent, SWT.NONE);
		this.exhibits = new ArrayList<Exhibit>();
		initialize();
	}

	private void initialize() {
		// ----- Layout -----
		GridData gridData = new GridData(SWT.FILL, SWT.UP, true, true);
		this.setLayoutData(gridData);
		this.setLayout(new GridLayout(3, false));

		// ----- Name -----
		Label label = new Label(this, SWT.NONE);
		label.setText(ResourceManager.getText(OrdoUI.DETAIL_TAG_NAME));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textName = new Text(this, SWT.NONE);
		textName.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		textName.setLayoutData(gridData);

		// ----- Exhibits -----
		label = new Label(this, SWT.NONE);
		label.setText(ResourceManager.getText(OrdoUI.DETAIL_TAG_EXHIBITS));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		Label dropLabel = new Label(this, SWT.PUSH);
		dropLabel.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_DROP));

		DropTarget dropTarget = new DropTarget(dropLabel, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new ExhibitDropTargetAdapter());

		listExhibits = new List(this, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		listExhibits.setLayoutData(gridData);

		// ----- Buttons -----
		Composite buttonComposite = new Composite(this, SWT.NONE);
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gridData.horizontalSpan = 3;
		buttonComposite.setLayoutData(gridData);
		buttonComposite.setLayout(new GridLayout(2, false));

		Button button = new Button(buttonComposite, SWT.NONE);
		button.setImage(ResourceManager
				.getImage(getDisplay(), OrdoUI.IMAGES_OK));
		button.setText(ResourceManager.getText(OrdoUI.BUTTON_SAVE));
		button.addSelectionListener(new SaveSelectionListener());

		button = new Button(buttonComposite, SWT.NONE);
		button.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_CANCEL));
		button.setText(ResourceManager.getText(OrdoUI.BUTTON_CANCEL));
		button.addSelectionListener(new CancelSelectionListener());
	}

	private class ExhibitDropTargetAdapter extends DropTargetAdapter {

		@Override
		public void drop(DropTargetEvent event) {
			String msg = (String) event.data;
			String[] exhibits = msg.split(";");
			for (String msgExhibit : exhibits) {
				String[] split = msgExhibit.split("/");
				if (split[0].equals("exhibit")) {
					Long id = Long.parseLong(split[1]);
					Exhibit exhibit = DataAccess.getInstance().getExhibitById(
							id);
					if (null != exhibit) {
						if (!CreateTagComposite.this.exhibits.contains(exhibit)) {
							CreateTagComposite.this.exhibits.add(exhibit);
						} else {
							CreateTagComposite.this.exhibits.remove(exhibit);
						}
					}
				}
			}
			listExhibits.removeAll();
			for (Exhibit exhibit : CreateTagComposite.this.exhibits) {
				listExhibits.add(exhibit.getName());
			}
		}

	}

	private class SaveSelectionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if(null == textName.getText()) {
				MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR);
				messageBox.setText(ResourceManager.getText(OrdoUI.ERROR_NAME_EMPTY_TITLE));
				messageBox.setMessage(ResourceManager.getText(OrdoUI.ERROR_NAME_EMPTY));
				messageBox.open();
			} else {
				try {
					Tag tag = LogicAccess.saveTag(textName.getText(), exhibits);
					UiEvent event = new UiEvent(tag, UiEventType.TagAdded);
					MainFrame.handleEvent(event);
				} catch (EmptyNameException exn) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR);
					messageBox.setText(ResourceManager.getText(OrdoUI.ERROR_NAME_EMPTY_TITLE));
					messageBox.setMessage(ResourceManager.getText(OrdoUI.ERROR_NAME_EMPTY));
					messageBox.open();
				}
			}
		}

	}

	private class CancelSelectionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			UiEvent event = new UiEvent(null, UiEventType.AddTagCanceled);
			MainFrame.handleEvent(event);
		}
	}
}
