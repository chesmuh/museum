package de.chesmuh.ordo.gui.composites.dialogs;

import java.util.Collection;

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

public class ExhibitInformationComposite extends Composite {

	private Text textName;
	private Text textParent;
	private Exhibit exhibit;
	private Text textDescription;
	private Composite buttonComposite;
	private Collection<Tag> tags;
	private Label sectionDropLabel;
	private Text textTag;
	private Label tagDropLabel;

	public ExhibitInformationComposite(Composite parent, Exhibit exhibit) {
		super(parent, SWT.NONE);
		this.exhibit = exhibit;
		this.tags = DataAccess.getInstance().getTagByExhibit(exhibit);
		initilalize();
	}

	private void initilalize() {
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
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		textName.setLayoutData(gridData);
		textName.setEditable(false);
		textName.setText(exhibit.getName());

		// ----- ParentSection -----
		label = new Label(this, SWT.NONE);
		label.setText(ResourceManager.getText(OrdoUI.DETAIL_EXHIBIT_SECTION));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		sectionDropLabel = new Label(this, SWT.PUSH);
		sectionDropLabel.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_DROP_DISABLED));

		textParent = new Text(this, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		textParent.setLayoutData(gridData);
		textParent.setEditable(false);
		if (null != exhibit.getMuseumId()) {
			textParent.setText(exhibit.getMuseum().getName());
			textParent.setData(exhibit.getMuseum());
		} else if (null != exhibit.getSection()) {
			textParent.setText(exhibit.getSection().getName());
			textParent.setData(exhibit.getSection());
		}

		// ----- Tag -----
		label = new Label(this, SWT.NONE);
		label.setText(ResourceManager.getText(OrdoUI.DETAIL_EXHIBIT_TAG));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		tagDropLabel = new Label(this, SWT.PUSH);
		tagDropLabel.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_DROP_DISABLED));

		textTag = new Text(this, SWT.NONE);
		textTag.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		textTag.setLayoutData(gridData);
		textTag.setEditable(false);
		textTag.setText(Util.getTags(tags));

		// ----- Description -----
		label = new Label(this, SWT.NONE);
		label.setText(ResourceManager
				.getText(OrdoUI.DETAIL_EXHIBIT_DESCRIPTION));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textDescription = new Text(this, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		textDescription.setLayoutData(gridData);
		textDescription.setEditable(false);
		textDescription.setText(exhibit.getDescription());

		// ----- Buttons -----
		buttonComposite = new Composite(this, SWT.NONE);
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gridData.horizontalSpan = 3;
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

			// -----Enable UI -----
			textName.setEditable(true);
			textDescription.setEditable(true);
			sectionDropLabel.setImage(ResourceManager.getImage(getDisplay(),
					OrdoUI.IMAGES_DROP_ENABLED));
			DropTarget dropTargetSection = new DropTarget(sectionDropLabel,
					DND.DROP_MOVE);
			dropTargetSection.setTransfer(new Transfer[] { TextTransfer
					.getInstance() });
			dropTargetSection.addDropListener(new SectionDropTargetAdapter());

			
			tagDropLabel.setImage(ResourceManager.getImage(getDisplay(),
					OrdoUI.IMAGES_DROP_ENABLED));
			DropTarget dropTargetTag = new DropTarget(tagDropLabel,
					DND.DROP_MOVE);
			dropTargetTag.setTransfer(new Transfer[] { TextTransfer
					.getInstance() });
			dropTargetTag.addDropListener(new TagDropTargetAdapter());

			buttonComposite.layout();
			ExhibitInformationComposite.this.layout();
			UiEvent event = new UiEvent(null, UiEventType.EditExhibit);
			MainFrame.handleEvent(event);
		}

	}

	private class SaveSelectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			String name = textName.getText();
			String description = textDescription.getText();
			Section section = null;
			Museum museum = null;
			Long sectionId = null;

			if (null == textParent.getData()) {
				MessageBox messageBox = new MessageBox(getShell());
				messageBox.setMessage(ResourceManager
						.getText(OrdoUI.ERROR_NOPARENT));
				messageBox.setText(ResourceManager
						.getText(OrdoUI.ERROR_NOPARENT_TITLE));
				messageBox.open();
			} else {
				if (textParent.getData() instanceof Section) {
					section = ((Section) textParent.getData());
					museum = section.getMuseum();
					sectionId = section.getId();
				} else if (textParent.getData() instanceof Museum) {
					museum = (Museum) textParent.getData();
				}

				try {
					Exhibit exhibit = LogicAccess.updateExhibit(sectionId,
							museum.getId(), name, description,
							ExhibitInformationComposite.this.exhibit);
					UiEvent event = new UiEvent(exhibit,
							UiEventType.ExhibitEdited);
					MainFrame.handleEvent(event);
				} catch (EmptyNameException exn) {
					MessageBox messageBox = new MessageBox(getShell(),
							SWT.ICON_ERROR);
					messageBox.setText(ResourceManager
							.getText(OrdoUI.ERROR_NAME_EMPTY_TITLE));
					messageBox.setMessage(ResourceManager
							.getText(OrdoUI.ERROR_NAME_EMPTY));
					messageBox.open();
				} catch (SectionNotSetException exn) {
					MessageBox messageBox = new MessageBox(getShell(),
							SWT.ICON_ERROR);
					messageBox.setText(ResourceManager
							.getText(OrdoUI.ERROR_NOSECTION));
					messageBox.setMessage(ResourceManager
							.getText(OrdoUI.ERROR_NOSECTION_TITLE));
					messageBox.open();
				}
			}
		}

	}

	private class CancelSelectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			UiEvent event = new UiEvent(null, UiEventType.EditExhibitCanceled);
			MainFrame.handleEvent(event);
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
				textParent.setText(museum.getName());
				textParent.setData(museum);
			} else if (split[0].equals("section")) {
				Long id = Long.parseLong(split[1]);
				Section section = DataAccess.getInstance().getSectionById(id);
				textParent.setText(Util.getPath(section));
				textParent.setData(section);
			}
		}

	}

	private class TagDropTargetAdapter extends DropTargetAdapter {

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
			textTag.setText(Util.getTags(tags));
		}

	}

}
