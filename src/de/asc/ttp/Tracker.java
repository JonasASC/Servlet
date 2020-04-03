package de.asc.ttp;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import de.asc.db.Database;
import de.asc.db.Table;

public class Tracker extends Database {
		@DBExpose
		@Expose
		public Long projectid;
		@DBExpose
		@Expose
		public Long userid;
		public String startts = null;
		public String endts = null;
		public String active = null;
		public String prepareInsertStmt;
		public String prepareUpdateStmt;
		private PreparedStatement insertStmt = null;
		private PreparedStatement UpdateStmt = null;
	
		public Tracker() throws SQLException {
		prepareInsertStmt = "insert into timeslices (projectid, userid, startts, active) values (?,?,current_timestamp,'t');";
		prepareUpdateStmt = "update timeslices set endts = current_timestamp , active = 'f' where projectid = ? and userid = ? and active;";
		insertStmt = connection.prepareStatement(prepareInsertStmt, Statement.RETURN_GENERATED_KEYS);
		UpdateStmt = connection.prepareStatement(prepareUpdateStmt, Statement.RETURN_GENERATED_KEYS);
	}

	public void StartTs() throws SQLException {
		insertStmt.setLong(1, projectid);
		insertStmt.setLong(2, userid);
		insertStmt.executeUpdate();
		 
	}

	public void EndTs() throws SQLException {
		UpdateStmt.setLong(1, projectid);
		UpdateStmt.setLong(2, userid);
		UpdateStmt.executeUpdate();
	
		
	}
	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}
	
}
