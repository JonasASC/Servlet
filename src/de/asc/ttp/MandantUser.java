/**
 * 
 */
package de.asc.ttp;
import java.sql.SQLException;

import de.asc.db.Table;
/**
 * @author jonas
 *
 */
public class MandantUser extends Table{
	private static final String TABLENAME = "mandantuser";
	public int mandantid;
	public int userid;

	public MandantUser() throws SQLException {
		init(TABLENAME);
	}
}
