package de.chesmuh.ordo.config;

/**
 * 
 * @author Chesmuh
 * 
 */
public class ConfigKeys {
	private ConfigKeys() {

	}

	public static final String FILE_PATH = "data/";
	public static final String FILE_NAME = "config.cfg";
	public static final String LOCALE_PATH = "resources/language/";
	public static final String LOCALE_NAME = "UiMessages";

	// ----- Database -----
	public static final String DATABASE_SERVER = "database.server";
	public static final String DATABASE_SERVER_DEFAULT = "localhost";
	public static final String DATABASE_PASSWORD = "database.password";
	public static final String DATABASE_PASSWORD_DEFAULT = "ordo";
	public static final String DATABASE_USERNAME = "database.username";
	public static final String DATABASE_USERNAME_DEFAULT = "ordo";
	public static final String DATABASE_DATABASE = "database.database";
	public static final String DATABASE_DATABASE_DEFAULT = "ordo";
	public static final String DATABASE_PORT = "database.port";
	public static final String DATABASE_PORT_DEFAULT = "3006";
	
	// ----- LOCATION -----
	public static final String LOCALE_LANGUAGE = "locale.language";
	public static final String LOCALE_LANGUAGE_DEFAULT = "de";
	public static final String LOCALE_COUNTRY = "locale.country";
	public static final String LOCALE_COUNTRY_DEFAULT = "DE";

	public static String getConfigContent() {
		String linesep = System.getProperty("line.separator");
		String keyvalue = "%s=%s" + linesep;
		
		StringBuilder sb = new StringBuilder();
		sb.append("# ----- DATABASE -----");
		sb.append(linesep);
		sb.append(String.format(keyvalue, DATABASE_SERVER, DATABASE_SERVER_DEFAULT));
		sb.append(String.format(keyvalue, DATABASE_PORT, DATABASE_PORT_DEFAULT));
		sb.append(String.format(keyvalue, DATABASE_DATABASE, DATABASE_DATABASE_DEFAULT));
		sb.append(String.format(keyvalue, DATABASE_USERNAME, DATABASE_USERNAME_DEFAULT));
		sb.append(String.format(keyvalue, DATABASE_PASSWORD, DATABASE_PASSWORD_DEFAULT));
		sb.append(linesep).append(linesep);
		sb.append("# ----- LOCATION -----");
		sb.append(linesep);
		sb.append(String.format(keyvalue, LOCALE_LANGUAGE, LOCALE_LANGUAGE_DEFAULT));
		sb.append(String.format(keyvalue, LOCALE_COUNTRY, LOCALE_COUNTRY_DEFAULT));
		return sb.toString();
	}
	
}
