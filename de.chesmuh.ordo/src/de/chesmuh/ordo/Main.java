package de.chesmuh.ordo;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.chesmuh.ordo.data.Database;

public class Main {

	/**
	 * @author Chesmuh
	 * 
	 * @param args
	 */
	public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String[] args) {
		LOGGER.setLevel(Level.FINEST);
		try {
				Database.getInstance();
		} catch (SQLException exn) {
			exn.printStackTrace();
		} catch (ClassNotFoundException exn) {
			exn.printStackTrace();
		}
	}
}
