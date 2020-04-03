/**
 * 
 */
package de.asc.ttp;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import de.asc.db.Table;
/**
 * @author jonas
 *
 */
public class Tasks extends Table{
	private static final String TABLENAME = "tasks";
	@Expose
	public int taskid;
	@Expose
	public String name = null;
	@Expose
	public String pph = null;
	
	public Tasks() throws SQLException {
		init(TABLENAME);
	}
}
