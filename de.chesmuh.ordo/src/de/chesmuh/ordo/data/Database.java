package de.chesmuh.ordo.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import de.chesmuh.ordo.config.Config;

public class Database {
	
	private static Database instance;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static Database getInstance() throws SQLException, ClassNotFoundException {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

	private Database() throws SQLException, ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw e;
		}
		createTables();
	}

	private void createTables() throws SQLException {
		Connection connection = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			statement.addBatch(SQLStatements.Museum.CREATE_TABLE);
			LOGGER.info(SQLStatements.Museum.CREATE_TABLE);
			statement.addBatch(SQLStatements.Section.CREATE_TABLE);
			LOGGER.info(SQLStatements.Section.CREATE_TABLE);
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

	private Connection getConnection() throws SQLException {
		String host = Config.getInstance().getDatabaseServer();
		String db = Config.getInstance().getDatabaseName();
		String user = Config.getInstance().getDatabaseUsername();
		String port = Config.getInstance().getDatabasePort();
		String pwd = Config.getInstance().getDatabasePassword();
		String url = "jdbc:mysql://" + host + ":" + port +"/" + db;
		LOGGER.info("URL:" + url);
		return DriverManager.getConnection(url, user, pwd);
	}
}
