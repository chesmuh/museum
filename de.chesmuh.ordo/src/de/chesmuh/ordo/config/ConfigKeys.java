package de.chesmuh.ordo.config;

/**
 * 
 * @author Chesmuh
 *
 */
public class ConfigKeys {
	private ConfigKeys() {

	}

	public static final String FILE_PATH = "config.cfg";

	// ----- Database -----
	public static final String DATABASE_SERVER = "database.server";
	public static final String DATABASE_PASSWORD = "database.password";
	public static final String DATABASE_USERNAME = "database.username";
	public static final String DATABASE_DATABASE = "database.database";
	public static final String DATABASE_PORT = "database.port";

	public static final String CONFIG_CONTENT = "# ----- DATABASE -----\n"
			+ DATABASE_SERVER + "=localhost\n" + DATABASE_PORT + "=3306"
			+ DATABASE_DATABASE + "=ordo\n" + DATABASE_USERNAME + "=root\n"
			+ DATABASE_PASSWORD + "=\n\n";
}
