package de.chesmuh.ordo.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * 
 * @author Chesmuh
 *
 */
public class Config {

	private static Config instance;
	private Properties configFile;
	private static final Logger LOGGER = Logger
			.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private Config() {
		configFile = new Properties();
		try {
			loadFile();
		} catch (FileNotFoundException exn) {
			LOGGER.warning("Couldn't find config!");
			LOGGER.info("Try to create ConfigFile.");
			
			File dir = new File(ConfigKeys.FILE_PATH);
			if(!dir.exists()) {
				dir.mkdirs();			
			}
			File file = new File(ConfigKeys.FILE_PATH + ConfigKeys.FILE_NAME);
			if(!file.exists()) {
				try {
					file.createNewFile();
					Writer writer = new FileWriter(file);
					writer.write(ConfigKeys.getConfigContent());
					writer.flush();
					writer.close();
				} catch (IOException e) {
					LOGGER.warning("Unable to Create ConfigFile: " + file.getAbsolutePath());
				}
			}
		}
	}
	
	private void loadFile() throws FileNotFoundException {
		FileInputStream inputStream = new FileInputStream(new File(ConfigKeys.FILE_PATH + ConfigKeys.FILE_NAME));
		try {
			configFile.load(inputStream);
		} catch (IOException e) {
			LOGGER.warning("Can't load Configfile!");
			LOGGER.warning(e.getMessage());
		}
	}

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	public String getDatabaseServer() {
		return configFile.getProperty(ConfigKeys.DATABASE_SERVER);
	}

	public String getDatabaseName() {
		return configFile.getProperty(ConfigKeys.DATABASE_DATABASE);
	}

	public String getDatabasePassword() {
		return configFile.getProperty(ConfigKeys.DATABASE_PASSWORD);
	}

	public String getDatabaseUsername() {
		return configFile.getProperty(ConfigKeys.DATABASE_USERNAME);
	}

	public String getDatabasePort() {
		return configFile.getProperty(ConfigKeys.DATABASE_PORT);
	}
	
	public ResourceBundle getUIBundle() {
		String language = configFile.getProperty(ConfigKeys.LOCALE_LANGUAGE);
		String country = configFile.getProperty(ConfigKeys.LOCALE_COUNTRY);
		return ResourceBundle.getBundle(ConfigKeys.LOCALE_PATH + ConfigKeys.LOCALE_NAME, new Locale(language, country));
	}
}
