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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.entitys.Tag;
import de.chesmuh.ordo.exceptions.EmptyNameException;
import de.chesmuh.ordo.exceptions.SectionNotSetException;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;
import de.chesmuh.ordo.logic.LogicAccess;
import de.chesmuh.ordo.util.Util;

public class CreateExhibitComposite extends Composite {

	private Text textParent;
	private Text textName;
	private Text textDescription;
	private Text textTag;
	private ArrayList<Tag> tags;

	private Object selection;

	public CreateExhibitComposite(Composite parent, Object selection) {
		super(parent, SWT.NONE);
		this.selection = selection;
		this.tags = new ArrayList<Tag>();
		initialize();
	}

	private void initialize() {
		// ----- Layout -----
		GridData gridData = new GridData(SWT.FILL, SWT.UP, true, true);
		this.setLayoutData(gridData);
		this.setLayout(new GridLayout(3, false));

		// ----- Name -----
		Label label = new Label(this, SWT.NONE);
		label.setText(ResourceManager.getText(OrdoUI.DETAIL_EXHIBIT_NAME));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textName = new Text(this, SWT.NONE);
		textName.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		textName.setLayoutData(gridData);

		// ----- ParentSection -----
		label = new Label(this, SWT.NONE);
		label.setText(ResourceManager.getText(OrdoUI.DETAIL_EXHIBIT_SECTION));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		Label dropLabel = new Label(this, SWT.PUSH);
		dropLabel.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_DROP_ENABLED));

		DropTarget dropTarget = new DropTarget(dropLabel, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new SectionDropTargetAdapter());

		textParent = new Text(this, SWT.NONE);
		textParent.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		textParent.setLayoutData(gridData);
		textParent.setEditable(false);
		if (selection instanceof Museum) {
			textParent.setText(((Museum) selection).getName());
			textParent.setData(selection);
		} else if (selection instanceof Section) {
			Section section = (Section) selection;
			textParent.setText(Util.getPath(section));
			textParent.setData(selection);
		}

		// ----- Tag -----
		label = new Label(this, SWT.NONE);
		label.setText(ResourceManager.getText(OrdoUI.DETAIL_EXHIBIT_TAG));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		Label dropLabelLabel = new Label(this, SWT.PUSH);
		dropLabelLabel.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_DROP_ENABLED));

		DropTarget dropTargetLabel = new DropTarget(dropLabelLabel,
				DND.DROP_MOVE);
		dropTargetLabel
				.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTargetLabel.addDropListener(new LabelDropTargetAdapter());

		textTag = new Text(this, SWT.NONE);
		textTag.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		textTag.setLayoutData(gridData);
		textTag.setEditable(false);

		// ----- Description -----
		label = new Label(this, SWT.NONE);
		label.setText(ResourceManager
				.getText(OrdoUI.DETAIL_EXHIBIT_DESCRIPTION));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textDescription = new Text(this, SWT.NONE);
		textDescription.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		textDescription.setLayoutData(gridData);

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
		button.addSelectionListener(new SaveSelectionAdapter());

		button = new Button(buttonComposite, SWT.NONE);
		button.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_CANCEL));
		button.setText(ResourceManager.getText(OrdoUI.BUTTON_CANCEL));
		button.addSelectionListener(new CloseSelectionListener());
	}

	private class LabelDropTargetAdapter extends DropTargetAdapter {

		@Override
		public void drop(DropTargetEvent event) {
			String msg = (String) event.data;
			String[] labels = msg.split(";");
			for (String msgTag : labels) {
				String[] split = msgTag.split("/");
				if (split[0].equals("tag")) {
					Long id = Long.parseLong(split[1]);
					Tag tag = DataAccess.getInstance().getTagById(id);
					if (null != tag) {
						if (!tags.contains(tag)) {
							tags.add(tag);
						} else {
							tags.remove(tag);
						}
					}
				}
			}
			String text = "";
			for (Tag tagName : tags) {
				if (!text.isEmpty()) {
					text += ", " + tagName.getName();
				} else {
					text += tagName.getName();
				}
			}
			textTag.setText(text);
		}

	}

	private class SectionDropTargetAdapter extends DropTargetAdapter {

		@Override
		public void drop(DropTargetEvent event) {
			String msg = (String) event.data;

			String[] split = msg.split("/");
			if (split[0].equals("museum")) {
				Long id = Long.parseLong(split[1]);
				Museum museum = DataAccess.getInstance().getMuseumById(id);
				if (null != museum) {
					textParent.setText(museum.getName());
					textParent.setData(museum);
				}
			} else if (split[0].equals("section")) {
				Long id = Long.parseLong(split[1]);
				Section section = DataAccess.getInstance().getSectionById(id);
				if (null != section) {
					textParent.setText(Util.getPath(section));
					textParent.setData(section);
				}
			}
		}

	}

	private class SaveSelectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			String name = textName.getText();
			String description = textDescription.getText();
			Long museumId = null;
			Long sectionId = null;
			Exhibit exhibit = null;

			if (null == textParent.getData()) {
				MessageBox messageBox = new MessageBox(getShell());
				messageBox.setMessage(ResourceManager
						.getText(OrdoUI.ERROR_NOPARENT));
				messageBox.setText(ResourceManager
						.getText(OrdoUI.ERROR_NOPARENT_TITLE));
				messageBox.open();
			} else {
				if (textParent.getData() instanceof Section) {
					sectionId = ((Section) textParent.getData()).getId();
				} else if (textParent.getData() instanceof Museum) {
					museumId = ((Museum) textParent.getData()).getId();
				}
				try {
					exhibit = LogicAccess.saveExhibit(museumId, sectionId,
							name, description);
					for(Tag tag : tags) {
						LogicAccess.tagExhibit(exhibit, tag);
					}
					UiEvent event = new UiEvent(exhibit,
							UiEventType.ExhibitAdded);
					MainFrame.handleEvent(event);
				} catch (SectionNotSetException e1) {
					MessageBox messageBox = new MessageBox(getShell());
					messageBox.setMessage(ResourceManager
							.getText(OrdoUI.ERROR_NOSECTION));
					messageBox.setText(ResourceManager
							.getText(OrdoUI.ERROR_NOSECTION_TITLE));
					messageBox.open();
				} catch (EmptyNameException e1) {
					MessageBox messageBox = new MessageBox(getShell());
					messageBox.setMessage(ResourceManager
							.getText(OrdoUI.ERROR_NAME_EMPTY));
					messageBox.setText(ResourceManager
							.getText(OrdoUI.ERROR_NAME_EMPTY_TITLE));
					messageBox.open();
				}
			}

		}

	}

	private class CloseSelectionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			UiEvent event = new UiEvent(null, UiEventType.AddExhibitCanceled);
			MainFrame.handleEvent(event);
		}
	}
}
