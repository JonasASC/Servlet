package de.asc.ttp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.google.gson.annotations.Expose;

import de.asc.db.Table;

public class DateTime extends Table {
	private static final String TABLENAME = "timeslices";

	@DBExpose
	@Expose
	public Long userid;
	@DBExpose
	@Expose
	public Long projectid;
	@DBExpose
	@Expose
	public String startts = null;
	@DBExpose
	@Expose
	public String endts = null;
	@DBExpose
	@Expose
	public String active = null;
	
	
	public DateTime() throws SQLException, IllegalArgumentException, IllegalAccessException {
		init(TABLENAME);
	}
	
	public DateTime(Long id) throws SQLException, IllegalArgumentException, IllegalAccessException {
		init(TABLENAME);
		fetch(id);
	}
	
	public List<DateTime> getDateTimebyeProjectid(Long projectid) throws IllegalArgumentException, IllegalAccessException, SQLException {
		List<DateTime> l = new ArrayList<DateTime>();
		for (Long id : select("projectid = ?", projectid)) {
			l.add(new DateTime(id));
		};
		
		return l;
	}
	
	
	
}
	