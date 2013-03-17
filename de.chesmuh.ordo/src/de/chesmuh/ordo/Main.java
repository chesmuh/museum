package de.chesmuh.ordo;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.chesmuh.ordo.gui.MainFrame;

public class Main {

	/**
	 * @author Chesmuh
	 * 
	 * @param args
	 */
	public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String[] args) {
		LOGGER.setLevel(Level.FINEST);
		new MainFrame();
	}
		
}
