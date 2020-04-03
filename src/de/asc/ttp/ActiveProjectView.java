package de.asc.ttp;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import de.asc.db.Table;

public class ActiveProjectView extends Table{
	private static final String TABLENAME = "activeprojectlist";
	@DBExpose
	@Expose
	public Integer userid;
	@DBExpose
	@Expose
	public Boolean active;
	@DBExpose
	@Expose
	public String elapsed;
	@DBExpose
	@Expose
	public String name;
	@DBExpose
	@Expose
	public String progress;
	
	public ActiveProjectView() throws SQLException {
		init(TABLENAME, Table.READONLY);
	}
	
	public ActiveProjectView(Long id) throws SQLException, IllegalArgumentException, IllegalAccessException {
		init(TABLENAME, Table.READONLY);
		fetch(id);
		
	}
	
	public List<ActiveProjectView> getActiveProjectlistbyeUser(Long userId) throws IllegalArgumentException, IllegalAccessException, SQLException {
		List<ActiveProjectView> l = new ArrayList<ActiveProjectView>();
		for (Long id : select("userid = ?", userId)) {
			l.add(new ActiveProjectView(id));
		};
		
		return l;
	}
	
}
