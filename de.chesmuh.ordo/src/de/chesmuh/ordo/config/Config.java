package de.chesmuh.ordo.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
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
			FileInputStream inputStream = new FileInputStream(new File(ConfigKeys.FILE_PATH));
				
				try {
					configFile.load(inputStream);
				} catch (IOException e) {
					LOGGER.warning("Can't load Configfile!");
					LOGGER.warning(e.getMessage());
				}
		} catch (FileNotFoundException exn) {
			LOGGER.warning("Couldn't find config!");
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
}
