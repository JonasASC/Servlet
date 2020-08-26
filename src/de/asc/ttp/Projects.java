package de.asc.ttp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.util.PGInterval;

import com.google.gson.annotations.Expose;

import de.asc.db.Table;

public class Projects extends Table {
	private static final String TABLENAME = "projects";

	@DBExpose
	@Expose
	public Long userid;
	@DBExpose
	@Expose
	public String name = null;
	@DBExpose
	@Expose
	public String description = null;
	@DBExpose
	@Expose
	public String contract = null;
	@DBExpose
	@Expose
	public Integer budget = 0;
	@DBExpose
	@Expose
	public Integer projecttime;
	
	
	
	protected Integer getBudget() {
		return budget;
	}
	protected void setBudget(Integer budget) {
		this.budget = budget;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContract() {
		return contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
	}
	public Integer getProjecttime() {
		return projecttime;
	}
	public void setProjectTime(Integer projecttime) {
		this.projecttime = projecttime;
	}
	public Projects() throws SQLException, IllegalArgumentException, IllegalAccessException {
		init(TABLENAME);
	}
	
	public Projects(Long id) throws SQLException, IllegalArgumentException, IllegalAccessException {
		init(TABLENAME);
		fetch(id);
	}
	
	public List<Projects> getProjectlistbyeUser(Long userId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		List<Projects> l = new ArrayList<Projects>();
		for (Long id : select("userid = ?", userId)) {
			l.add(new Projects(id));
		};
		
		return l;
	}
	
	
	
}
	


