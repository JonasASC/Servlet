
package de.asc.ttp;
import java.sql.SQLException;

import de.asc.db.Table;
/**
 * @author jonas
 *
 */
public class Mandants extends Table {
	private static final String TABLENAME = "mandants";
	public String name = null;
	public String address = null;
	public int zip;
	public String city = null;
	public String country = null;
	
	public Mandants() throws SQLException {
		init(TABLENAME);
	}

}
