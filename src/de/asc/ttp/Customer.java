package de.asc.ttp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import de.asc.db.Table;

public class Customer extends Table {
	private static final String TABLENAME = "customer";
	
	
	@DBExpose
	@Expose
	public String name = null;
	@DBExpose
	@Expose
	public Long userid = null;
	
	public Customer() throws SQLException, IllegalArgumentException, IllegalAccessException {
		init(TABLENAME);
	}
	
	public Customer(Long id) throws SQLException, IllegalArgumentException, IllegalAccessException {
		init(TABLENAME);
		fetch(id);
	}
	
	public List<Customer> getActiveProjectlistbyeUser(Long userId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		List<Customer> l = new ArrayList<Customer>();
		for (Long id : select("userid = ?", userId)) {
			l.add(new Customer(id));
		};
		
		return l;
	}
	
}
