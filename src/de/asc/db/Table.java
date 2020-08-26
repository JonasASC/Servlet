/**
 * 
 */
package de.asc.db;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import de.asc.ttp.DBExpose;

/**
 * 
 *
 */
public class Table extends Database {

	public static final Boolean READONLY = Boolean.TRUE;
	
	protected Logger logger = Logger.getLogger(Table.class.getName());
	protected String tablename = null;
	private String insertSql = null;
	private String selectSql = null;
	private String updateSql = null;
	private List<Field> dbfields = new ArrayList<Field>();
	protected String insert = null;
	private String value = null;
	private String deleteRow = null;
	private PreparedStatement selectStmt = null;
	private PreparedStatement insertStmt = null;
	private PreparedStatement deleteStmt = null;
	private PreparedStatement updateStmt = null;
	
	@DBExpose
	@Expose
	public Long id = null;

	/**
	 * 
	 * @throws SQLException
	 */
	public Table() throws SQLException {
		super();

	}

	/**
	 * 
	 * @throws SQLException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Table(Long id) throws SQLException, IllegalArgumentException, IllegalAccessException {
		super();
		fetch(id);
	}


	/**
	 * 
	 * @return
	 */
	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}

	/**
	 * @param tablename2
	 * @throws SQLException
	 * 
	 */
	public void init(String tablename) throws SQLException {
		init(tablename, Boolean.FALSE);
	}

	/**
	 * @param tablename2
	 * @throws SQLException
	 * 
	 */
	public void init(String tablename, Boolean ro) throws SQLException {
		this.tablename = tablename;
		buildFieldList();
		prepareSelectStatement();
		if (!ro) {
			prepareUpdateStatement();
			prepareInsertStatement();
			prepareDeleteStatement();
		}
	}

	/**
	 * 
	 */
	private void buildFieldList() {
		Field[] fieldlist = this.getClass().getFields();
		for (int i = 0; i < fieldlist.length; i++) {
			if (!fieldlist[i].isAnnotationPresent(DBExpose.class))
				continue;
			
			dbfields.add(fieldlist[i]);
		}
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	private void prepareSelectStatement(String where) throws SQLException {
		if (where == null) {
			where = "id = ?";
		}
		selectSql = "select ";
		for (int i = 0; i < dbfields.size(); i++) {
			if (i > 0)
				selectSql += ", ";
			selectSql += dbfields.get(i).getName();
		}
		
		selectSql += " from " + tablename + " where " + where;
		//logger.info("preparing select statement: " + selectSql);
		selectStmt = connection.prepareStatement(selectSql);
	}

	private void prepareSelectStatement() throws SQLException {
		prepareSelectStatement("id = ?");
	}

	protected void fillObject(ResultSet rs) throws IllegalArgumentException, IllegalAccessException, SQLException {
		while (rs.next()) {
			for (int i = 0; i < dbfields.size(); i++) {
				Field f = dbfields.get(i);
				if (f.getType().isAssignableFrom(Long.class)) {
					f.set(this, rs.getLong(f.getName()));
				} else if (f.getType().isAssignableFrom(Integer.class)) {
					f.set(this, rs.getInt(f.getName()));
				} else if (f.getType().isAssignableFrom(Double.class)) {
					f.set(this, rs.getDouble(f.getName()));
				} else if (f.getType().isAssignableFrom(Boolean.class)) {
					f.set(this, rs.getBoolean(f.getName()));
				} else if (f.getType().isAssignableFrom(Timestamp.class)) {
					f.set(this, rs.getTimestamp(f.getName()));
				} else {
					f.set(this, rs.getString(f.getName()));
				}
			}

		}
	}

	public void fetch(Long id2) throws SQLException, IllegalArgumentException, IllegalAccessException {
		selectStmt.setLong(1, id2);
		ResultSet rs = selectStmt.executeQuery();
		fillObject(rs);
	}

	public List<Long> select(String query, Long id)
			throws SQLException, IllegalArgumentException, IllegalAccessException {
		PreparedStatement stmt = connection.prepareStatement("select id from " + tablename + " where " + query);
		stmt.setLong(1, id);
		ResultSet rs = stmt.executeQuery();

		List<Long> idList = new ArrayList<Long>();

		while (rs.next()) {
			idList.add(rs.getLong(1));
		}

		return idList;
	}


	private void prepareInsertStatement() throws SQLException {
		if (insertStmt != null)
			return;
		insertSql = "insert into " + tablename + " (";
		String fieldNames = "";
		String fieldValues = "";
		for (int i = 0; i < dbfields.size(); i++) {
			if (!dbfields.get(i).isAnnotationPresent(DBExpose.class) || dbfields.get(i).getName().equals("id"))
				continue;

			if (i > 0) {
				fieldNames += ", ";
				fieldValues += ", ";
			}
			fieldNames += dbfields.get(i).getName();
			fieldValues += "?";
		}
		insertSql += fieldNames + ") values (" + fieldValues + ")";
		//logger.info("preparing insert statement: " + insertSql);
		insertStmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
	}

	private void prepareUpdateStatement() throws SQLException {
		updateSql = "update " + tablename + " set ";
		for (int i = 0; i < dbfields.size(); i++) {
			if (dbfields.get(i).getName().equals("id"))
				continue; 
			if (i > 0)
				updateSql += ", ";
			updateSql += dbfields.get(i).getName() + " = ?";
		}
		updateSql += " where id = ?";
		//logger.info("preparing update statement: " + updateSql);
		updateStmt = connection.prepareStatement(updateSql);
	}

	private void prepareDeleteStatement() throws SQLException {
		deleteRow = "delete from " + tablename + " where id = ?";
		deleteStmt = connection.prepareStatement(deleteRow);
	}

	public void update() throws IllegalArgumentException, IllegalAccessException, SQLException {
		int i = 0;
		int offset = 0;
		for (; i < dbfields.size(); i++) {
			if (dbfields.get(i).getName().equals("id")) {
				offset++;
				continue;
			}
			updateStmt.setObject(1+i-offset, dbfields.get(i-offset).get(this));
		}
		updateStmt.setObject(1+i-offset, id);
		updateStmt.executeUpdate();
	}

	public void delete(Long id) throws Exception {
		try {
			if (id == null) {
				throw new IllegalArgumentException("id cannot be null!!");
			}
			deleteStmt.setLong(1, id);
			deleteStmt.executeUpdate();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
	}

	public void setSelect(String selectSql) {
		this.selectSql = selectSql;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public void insert() throws IllegalArgumentException, IllegalAccessException, SQLException {
		for (int i = 0; i < dbfields.size(); i++) {
			if (dbfields.get(i).getName().equals("id")) {
				continue;
			}
			insertStmt.setObject(i + 1, dbfields.get(i).get(this));
		}
		int affectedRows = insertStmt.executeUpdate();
		if (affectedRows > 0) {
			logger.info(affectedRows + " rows inserted");
			ResultSet rs = insertStmt.getGeneratedKeys();
			if (rs.next())
				this.id = rs.getLong(1);
		}
	}

	public String toString() {
		return selectSql + "\n" + updateSql;
	}

	public static String MD5(String input) {
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
