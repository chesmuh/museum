package de.chesmuh.ordo.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import de.chesmuh.ordo.config.Config;
import de.chesmuh.ordo.data.Ordo.Section;

/**
 * 
 * @author Chesmuh
 *
 */
public class Database {

	private static Database instance;
	private static final Logger LOGGER = Logger
			.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static Database getInstance() throws SQLException {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

	private Database() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			LOGGER.warning("Database-Driver not found!");
		}
		createTables();
	}

	private void createTables() throws SQLException {
		Connection connection = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			statement.addBatch(de.chesmuh.ordo.data.Ordo.Museum.CREATE_TABLE);
			LOGGER.info(de.chesmuh.ordo.data.Ordo.Museum.CREATE_TABLE);
			statement.addBatch(Section.CREATE_TABLE);
			LOGGER.info(Section.CREATE_TABLE);
			statement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public Connection getConnection() throws SQLException {
		String host = Config.getInstance().getDatabaseServer();
		String db = Config.getInstance().getDatabaseName();
		String user = Config.getInstance().getDatabaseUsername();
		String port = Config.getInstance().getDatabasePort();
		String pwd = Config.getInstance().getDatabasePassword();
		String url = "jdbc:mysql://" + host + ":" + port + "/" + db;
		LOGGER.info("URL:" + url);
		return DriverManager.getConnection(url, user, pwd);
	}
	
}
