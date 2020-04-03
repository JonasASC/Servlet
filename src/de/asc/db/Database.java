package de.asc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * database wrapper
 * 
 * 
 * 
 *
 */
public class Database {
	private static String url = "jdbc:postgresql://sql629.your-server.de:5432/timepro";
	private static String userid = "asrcom_1";
	private static String password = "DYjcgyJa8aDJU5j8";

	protected static Connection connection = null;

	public Database() throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connect();
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public static void connect() throws SQLException {
		if (connection == null)
			connection = DriverManager.getConnection(url, userid, password);
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public static void disconnect() throws SQLException {
		if (connection != null)
			connection.close();
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		connect();
		System.out.println("Connect");
		return connection;
	}

}