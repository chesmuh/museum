package de.chesmuh.ordo.gui.composites;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;

public class AddSectionComposite extends Composite {

	private ResourceBundle bundle;
	private Text textParent;
	private Text textName;
	private Text textDescription;

	public AddSectionComposite(Composite parent, int style) {
		super(parent, style);
		bundle = Config.getInstance().getUIBundle();
		initialize();
	}

	private void initialize() {
		// ----- Layout -----
		GridData gridData = new GridData(SWT.FILL, SWT.UP, true, true);
		this.setLayoutData(gridData);
		this.setLayout(new GridLayout(3, false));
		
		
		Label label = new Label(this, SWT.NONE);
		label.setText(bundle.getString(OrdoUI.DETAIL_SECTION_NAME));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textName = new Text(this, SWT.NONE);
		textName.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		textName.setLayoutData(gridData);

		label = new Label(this, SWT.NONE);
		label.setText(bundle.getString(OrdoUI.DETAIL_SECTION_PARENTSECTION));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);


		Label dropLabel = new Label(this, SWT.PUSH);
		dropLabel.setImage(ResourceManager.getImage(getDisplay(),
				OrdoUI.IMAGES_DROP));

		DropTarget dropTarget = new DropTarget(dropLabel, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new SectionDropTargetAdapter());

		textParent = new Text(this, SWT.NONE);
		textParent.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		textParent.setLayoutData(gridData);
		textParent.setEditable(false);

		label = new Label(this, SWT.NONE);
		label.setText(bundle.getString(OrdoUI.DETAIL_SECTION_DESCRIPTION));
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, true);
		label.setLayoutData(gridData);

		textDescription = new Text(this, SWT.NONE);
		textDescription.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		textDescription.setLayoutData(gridData);

		Composite buttonComposite = new Composite(this, SWT.NONE);
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gridData.horizontalSpan = 2;
		buttonComposite.setLayoutData(gridData);
		buttonComposite.setLayout(new GridLayout(2, false));
		
		Button button = new Button(buttonComposite, SWT.NONE);
		button.setImage(ResourceManager.getImage(getDisplay(), OrdoUI.IMAGES_OK));
		button.setText(bundle.getString(OrdoUI.DETAIL_SECTION_ADD_SAVE));

		button = new Button(buttonComposite, SWT.NONE);
		button.setImage(ResourceManager.getImage(getDisplay(), OrdoUI.IMAGES_CANCEL));
		button.setText(bundle.getString(OrdoUI.DETAIL_SECTION_ADD_CANCEL));
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
				Section section = DataAccess.getInstance().getSectionById(
						id);
				textParent.setText(getPath(section));
				textParent.setData(section);
			}
		}
		
		private String getPath(Section section) {
			String path = section.getName();
			while(section.getParent() != null) {
				path = section.getParent().getName() + "\\" + path;
				section = section.getParent();
			}
			path = section.getMuseum().getName() + "\\" + path;
			return path;
		}

	}
}
