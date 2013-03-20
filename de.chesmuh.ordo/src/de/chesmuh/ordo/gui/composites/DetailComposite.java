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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.gui.MainFrame;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;

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
		group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_TITLE));

		// ----- DropTarget -----
		// Muss wieder für Labels und Sectionen eingebaut werden.
		// DropTarget dropTarget = new DropTarget(, DND.DROP_MOVE);
		// dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance()
		// });
		// dropTarget.addDropListener(new ListDropAdapter());

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
		this.setRedraw(false);
		this.setLayoutData(gridData);
		group.setLayout(new GridLayout(3, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setText(bundle.getString(OrdoUI.DETAIL_GROUP_ADD_SECTION));

		Label label = new Label(group, SWT.NONE);
		label.setText(bundle.getString(OrdoUI.DETAIL_SECTION_NAME));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		label.setLayoutData(gridData);

		final Text textName = new Text(group, SWT.NONE);
		textName.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		textName.setLayoutData(gridData);

		label = new Label(group, SWT.NONE);
		label.setText(bundle.getString(OrdoUI.DETAIL_SECTION_PARENTSECTION));
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		label.setLayoutData(gridData);

		final Text textParent = new Text(group, SWT.NONE);
		textParent.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		textParent.setLayoutData(gridData);
		textParent.setEditable(false);
		
		Label dropLabel = new Label(group, SWT.PUSH);
		dropLabel.setImage(ResourceManager.getImage(getDisplay(), OrdoUI.IMAGES_DROP));
		
		DropTarget dropTarget = new DropTarget(dropLabel, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance()});
		dropTarget.addDropListener(new DropTargetAdapter() {
			
			@Override
			public void drop(DropTargetEvent event) {
				String msg = (String) event.data;
				String[] split = msg.split("/");
				if(split[0].equals("museum")) {
					Long id = Long.parseLong(split[1]); 
					Museum museum = DataAccess.getInstance().getMuseumById(id);
					textParent.setText(museum.getName());
					textParent.setData(museum);
				} else if(split[0].equals("section")) {
					Long id = Long.parseLong(split[1]); 
					Section section = DataAccess.getInstance().getSectionById(id);
					textParent.setText(section.getName());
					textParent.setData(section);
				}
			}
			
		});
		
		label = new Label(group, SWT.NONE);
		label.setText(bundle.getString(OrdoUI.DETAIL_SECTION_DESCRIPTION));
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		label.setLayoutData(gridData);
		
		final Text textDescription = new Text(group, SWT.NONE);
		textDescription.setText("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		textDescription.setLayoutData(gridData);

		label = new Label(group, SWT.NONE);
		label.setImage(ResourceManager.getImage(getDisplay(), OrdoUI.IMAGES_OK));
		
		label = new Label(group, SWT.NONE);
		label.setImage(ResourceManager.getImage(getDisplay(), OrdoUI.IMAGES_CANCEL));

		this.setRedraw(true);
		this.pack();
	}

}
