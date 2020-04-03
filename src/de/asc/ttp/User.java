package de.asc.ttp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import de.asc.db.Table;
import de.asc.exceptions.UserNotAuthenticatedException;

public class User extends Table {
	private static final String TABLENAME = "users";
	@DBExpose
	@Expose
	public Integer user_id;
	@DBExpose
	@Expose
	public String username = null;
	@DBExpose
	@Expose
	public String password = null;
	@DBExpose
	@Expose
	public String name = null;
	@DBExpose
	@Expose
	public String email = null;
	
	public User() throws SQLException, IllegalArgumentException, IllegalAccessException {
		init(TABLENAME);
	}

	public User(Long id) throws SQLException, IllegalArgumentException, IllegalAccessException {
		init(TABLENAME);
		fetch(id);
	}

	public User(Login login) throws IllegalArgumentException, IllegalAccessException, SQLException, UserNotAuthenticatedException {
		init(TABLENAME);
		login(login);
	}
	
	/**
	 * 
	 * @param login
	 * @throws UserNotAuthenticatedException 
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void login(Login login) throws UserNotAuthenticatedException, IllegalArgumentException, IllegalAccessException, SQLException {
		fetchByEmail(login.email);
		String clearTextPassword = login.password;
		String hashedPassword = password;
		
		if (!hashPassword(clearTextPassword).equals(hashedPassword))
			
			throw new UserNotAuthenticatedException();
		else {
			
		}
	}

	/**
	 * 
	 * @param email
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void fetchByEmail(String email) throws SQLException, IllegalArgumentException, IllegalAccessException {
		PreparedStatement stmt = connection.prepareStatement("Select id from Users where email =?");
		stmt.setString(1, email);
		ResultSet resultSet = stmt.executeQuery();
		while(resultSet.next()) {
			
			fetch(resultSet.getLong(1));
		}
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String hashPassword(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext; 
			}
		return hashtext; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	


}