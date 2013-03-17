package de.chesmuh.ordo.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class MainFrame {

	Shell shell;

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
		Composite mainComposite = new Composite(shell, SWT.BORDER);
		mainComposite.setBackground(new Color(Display.getCurrent(), 128, 128, 128));
		mainComposite.setLayout(new GridLayout(5, true));

		// ----- Tree -----
		Composite treeComposite = new TreeComposite(mainComposite, SWT.BORDER);
		treeComposite.setBackground(new Color(Display.getCurrent(), 255,255,0));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		treeComposite.setLayoutData(gridData);
		treeComposite.setLayout(new GridLayout(1, true));

		// ----- Overview -----
		Composite overViewComposite = new Composite(mainComposite, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 4;
		overViewComposite.setLayoutData(gridData);
		overViewComposite.setLayout(new GridLayout(4, true));

		// ----- Table -----
		Composite tableComposite = new TableComposite(overViewComposite, SWT.BORDER);
		tableComposite.setBackground(new Color(Display.getCurrent(), 0, 0, 255));
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		tableComposite.setLayoutData(gridData);
		tableComposite.setLayout(new GridLayout(1, true));

		// ----- Label -----
		Composite labelComposite = new LabelComposite(overViewComposite, SWT.BORDER);
		labelComposite.setBackground(new Color(Display.getCurrent(), 0,255,0));
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 1;
		labelComposite.setLayoutData(gridData);
		labelComposite.setLayout(new GridLayout(1, true));

		// ----- Detail -----
		Composite detailComposite = new DetailComposite(overViewComposite, SWT.BORDER);
		detailComposite.setBackground(new Color(Display.getCurrent(), 255,0,0));
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 4;
		detailComposite.setLayoutData(gridData);
	}

}
