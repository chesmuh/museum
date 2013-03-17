package de.chesmuh.ordo.gui;

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
import de.chesmuh.ordo.gui.resources.OrdoUI;

public class MainFrame {

	Shell shell;
	ResourceBundle uiBundle;
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

	private class FileCloseSelectionListener implements SelectionListener {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			shell.close();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
