package de.chesmuh.ordo.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.gui.composites.DetailComposite;
import de.chesmuh.ordo.gui.composites.LabelComposite;
import de.chesmuh.ordo.gui.composites.TableComposite;
import de.chesmuh.ordo.gui.composites.TreeComposite;
import de.chesmuh.ordo.gui.interfaces.IUiListener;
import de.chesmuh.ordo.gui.interfaces.UiEvent;
import de.chesmuh.ordo.gui.resources.OrdoUI;

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
		
		// ----- Menu -----
		setMenu();
		
		// ----- Content -----
		Composite mainComposite = new Composite(shell, SWT.BORDER);
		mainComposite.setBackground(new Color(Display.getCurrent(), 128, 128, 128));
		mainComposite.setLayout(new GridLayout(5, true));

		// ----- Tree -----
		Composite treeComposite = new TreeComposite(mainComposite, SWT.BORDER);
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
		Composite tableComposite = new TableComposite(overViewComposite, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		tableComposite.setLayoutData(gridData);

		// ----- Label -----
		Composite labelComposite = new LabelComposite(overViewComposite, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		labelComposite.setLayoutData(gridData);

		// ----- Detail -----
		Composite detailComposite = new DetailComposite(overViewComposite, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 4;
		detailComposite.setLayoutData(gridData);
	}

	private void setMenu() {
		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		
		// ----- File -----
		MenuItem menuItemFile = new MenuItem(menuBar, SWT.CASCADE);
		menuItemFile.setText(uiBundle.getString(OrdoUI.MENU_FILE));
		Menu menuFile = new Menu(menuItemFile);
		
		// ----- File.Close -----
		MenuItem menuItemFileClose = new MenuItem(menuFile, SWT.CASCADE);
		menuItemFileClose.setText(uiBundle.getString(OrdoUI.MENU_FILE_CLOSE));
		menuItemFileClose.setAccelerator(SWT.MOD1 + 'Q');
		menuItemFileClose.addSelectionListener(new FileCloseSelectionListener());
		menuItemFile.setMenu(menuFile);
		
	}
	
	public static void addObserver(UiEventType type, IUiListener listener) {
		if(listeners.containsKey(type)) {
			Collection<IUiListener> list = listeners.get(type);
			list.add(listener);
		} else {
			Collection<IUiListener> list = new ArrayList<IUiListener>();
			list.add(listener);
			listeners.put(type, list);
		}
	}
	
	public static void handleEvent(UiEvent event) {
		if(listeners.containsKey(event.getType())) {
			Collection<IUiListener> collection = listeners.get(event.getType());
			for(IUiListener listener : collection) {
				listener.handleEvent(event);
			}
		}
	}

	private class FileCloseSelectionListener implements SelectionListener {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			shell.close();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			
		}
		
	}
	
	
}