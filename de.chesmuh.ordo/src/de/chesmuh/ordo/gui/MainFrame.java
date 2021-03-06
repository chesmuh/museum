package de.chesmuh.ordo.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import de.chesmuh.ordo.gui.composites.CategorieComposite;
import de.chesmuh.ordo.gui.composites.DetailComposite;
import de.chesmuh.ordo.gui.composites.ExhibitComposite;
import de.chesmuh.ordo.gui.composites.SectionComposite;
import de.chesmuh.ordo.gui.composites.TagComposite;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;

public class MainFrame {

	private Shell shell;
	private static HashMap<UiEventType, Collection<IUiListener>> listeners = new HashMap<UiEventType, Collection<IUiListener>>();

	public MainFrame() {
		Display display = new Display();

		shell = new Shell(display);

		shell.setLayout(new FillLayout());

		initialize();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void initialize() {
		shell.setText(ResourceManager.getText(OrdoUI.WINDOW_TITLE));
		shell.setImage(ResourceManager.getImage(shell.getDisplay(),
				OrdoUI.IMAGES_ORDO));

		// ----- Menu -----
		setMenu();

		// ----- Content -----
		Composite mainComposite = new Composite(shell, SWT.BORDER);
		mainComposite.setLayout(new GridLayout(5, true));

		// ----- SectionTree -----
		Composite treeComposite = new SectionComposite(mainComposite, SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		treeComposite.setLayoutData(gridData);

		// ----- Overview -----
		Composite overViewComposite = new Composite(mainComposite, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 4;
		overViewComposite.setLayoutData(gridData);
		overViewComposite.setLayout(new GridLayout(4, true));

		// ----- Table -----
		Composite tableComposite = new ExhibitComposite(overViewComposite,
				SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		tableComposite.setLayoutData(gridData);

		// ----- Label -----
		Composite labelComposite = new TagComposite(overViewComposite,
				SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		labelComposite.setLayoutData(gridData);

		// ----- Detail -----
		Composite detailComposite = new DetailComposite(overViewComposite,
				SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		detailComposite.setLayoutData(gridData);

		// ----- Categories -----
		Composite categorieComposite = new CategorieComposite(
				overViewComposite, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		categorieComposite.setLayoutData(gridData);
	}

	private void setMenu() {
		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);

		// ----- File -----
		MenuItem menuItemFile = new MenuItem(menuBar, SWT.CASCADE);
		menuItemFile.setText(ResourceManager.getText(OrdoUI.MENU_FILE));
		Menu menuFile = new Menu(menuItemFile);
		menuItemFile.setMenu(menuFile);
		
		// ---- File.New ----
		MenuItem menuItemFileNew = new MenuItem(menuFile, SWT.CASCADE);
		menuItemFileNew.setText(ResourceManager.getText(OrdoUI.MENU_FILE_NEW));
		menuItemFileNew.setImage(ResourceManager.getImage(shell.getDisplay(), OrdoUI.IMAGES_ADD));
		Menu menuNew = new Menu(menuItemFileNew);
		menuItemFileNew.setMenu(menuNew);
	    
	    // ----- File.New.Museum -----
	    MenuItem menuItemNewMuseum = new MenuItem(menuNew, SWT.CASCADE);
		menuItemNewMuseum.setText(ResourceManager.getText(OrdoUI.MENU_FILE_NEW_MUSEUM));
		menuItemNewMuseum.setImage(ResourceManager.getImage(shell.getDisplay(), OrdoUI.IMAGES_MUSEUM));
		menuItemNewMuseum.addSelectionListener(new NewMuseumSelectionAdapter());
	    
		// ----- File.Close -----
		MenuItem menuItemFileClose = new MenuItem(menuFile, SWT.CASCADE);
		menuItemFileClose.setText(ResourceManager.getText(OrdoUI.MENU_FILE_CLOSE));
		menuItemFileClose.setAccelerator(SWT.MOD1 + 'Q');
		menuItemFileClose
				.addSelectionListener(new FileCloseSelectionListener());

		// ----- Edit -----
		MenuItem menuItemEdit = new MenuItem(menuBar, SWT.CASCADE);
		menuItemEdit.setText(ResourceManager.getText(OrdoUI.MENU_EDIT));
		Menu menuEdit = new Menu(menuItemFile);
		menuItemEdit.setMenu(menuEdit);
		
		// ---- Edit.Delete ----
		MenuItem menuItemEditDelete = new MenuItem(menuEdit, SWT.CASCADE);
		menuItemEditDelete.setText(ResourceManager.getText(OrdoUI.MENU_EDIT_DELETE));
		menuItemEditDelete.setImage(ResourceManager.getImage(shell.getDisplay(), OrdoUI.IMAGES_DELETE));
		Menu menuDelete = new Menu(menuItemEditDelete);
		menuItemEditDelete.setMenu(menuDelete);
		
		// ---- Edit.Delete.Museum ----
		MenuItem menuItemDeleteMuseum = new MenuItem(menuDelete, SWT.CASCADE);
		menuItemDeleteMuseum.setText(ResourceManager.getText(OrdoUI.MENU_EDIT_DELETE_MUSEUM));
		menuItemDeleteMuseum.setImage(ResourceManager.getImage(shell.getDisplay(), OrdoUI.IMAGES_MUSEUM));
		menuItemDeleteMuseum.addSelectionListener(new DeleteMuseumSelectionAdapter());
	}

	public static void addObserver(UiEventType type, IUiListener listener) {
		if (listeners.containsKey(type)) {
			Collection<IUiListener> list = listeners.get(type);
			list.add(listener);
		} else {
			Collection<IUiListener> list = new ArrayList<IUiListener>();
			list.add(listener);
			listeners.put(type, list);
		}
	}

	public static void handleEvent(UiEvent event) {
		if (listeners.containsKey(event.getType())) {
			Collection<IUiListener> collection = listeners.get(event.getType());
			for (IUiListener listener : collection) {
				listener.handleEvent(event);
			}
		}
	}

	private class FileCloseSelectionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			shell.close();
		}

	}
	
	private class DeleteMuseumSelectionAdapter extends SelectionAdapter {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			UiEvent event = new UiEvent(null, UiEventType.RemoveMuseum);
			MainFrame.handleEvent(event);
		}
		
	}
	
	private class NewMuseumSelectionAdapter extends SelectionAdapter {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			UiEvent event = new UiEvent(null, UiEventType.AddMuseum);
			MainFrame.handleEvent(event);
		}
		
	}
	

}
