package de.chesmuh.ordo.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

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

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.gui.composites.CategorieComposite;
import de.chesmuh.ordo.gui.composites.DetailComposite;
import de.chesmuh.ordo.gui.composites.LabelComposite;
import de.chesmuh.ordo.gui.composites.TableComposite;
import de.chesmuh.ordo.gui.composites.SectionComposite;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.interfaces.UiEventType;
import de.chesmuh.ordo.gui.resources.OrdoUI;
import de.chesmuh.ordo.gui.resources.ResourceManager;

public class MainFrame {

	private Shell shell;
	private ResourceBundle uiBundle;
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
		uiBundle = Config.getInstance().getUIBundle();
		shell.setText(uiBundle.getString(OrdoUI.WINDOW_TITLE));
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
		Composite tableComposite = new TableComposite(overViewComposite,
				SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		tableComposite.setLayoutData(gridData);

		// ----- Label -----
		Composite labelComposite = new LabelComposite(overViewComposite,
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
		menuItemFile.setText(uiBundle.getString(OrdoUI.MENU_FILE));
		Menu menuFile = new Menu(menuItemFile);
		
		// ---- File.New ----
		MenuItem menuItemNew = new MenuItem(menuFile, SWT.CASCADE);
		menuItemNew.setText(uiBundle.getString(OrdoUI.MENU_FILE_NEW));
		Menu menuNew = new Menu(menuItemNew);
		menuItemNew.setMenu(menuNew);
	    
	    // ----- File.New.Museum -----
	    MenuItem menuItemNewMuseum = new MenuItem(menuNew, SWT.CASCADE);
		menuItemNewMuseum.setText(uiBundle.getString(OrdoUI.MENU_FILE_NEW_MUSEUM));
		menuItemNewMuseum.setImage(ResourceManager.getImage(shell.getDisplay(), OrdoUI.IMAGES_MUSEUM));
	    
		// ----- File.Close -----
		MenuItem menuItemFileClose = new MenuItem(menuFile, SWT.CASCADE);
		menuItemFileClose.setText(uiBundle.getString(OrdoUI.MENU_FILE_CLOSE));
		menuItemFileClose.setAccelerator(SWT.MOD1 + 'Q');
		menuItemFileClose
				.addSelectionListener(new FileCloseSelectionListener());
		menuItemFile.setMenu(menuFile);

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

}
