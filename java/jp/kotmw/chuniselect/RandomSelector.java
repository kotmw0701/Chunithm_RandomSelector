package jp.kotmw.chuniselect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RandomSelector {
	
	public static ResultSet randomget(int limit, String category, String diff, String artist, String bpm) {
		ResultSet set = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:C://sqlite/chunithm.db");
			//PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM music WHERE category LIKE ? AND artist LIKE '%' || ? || '%' AND (basic = ? OR advanced = ? OR expert = ? OR master = ?) ORDER BY random() LIMIT ?");
			set = connection.createStatement().executeQuery(execute(limit, category, diff, artist, bpm));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return set;
	}
	
	private static String execute(int limit, String category, String str_diff, String artist, String str_bpm) {
		double diff = Double.parseDouble(str_diff != null ? str_diff.replace("+", ".7") : "0.0");
		int bpm = Integer.parseInt(str_bpm != null ? str_bpm : "0");
		String sql = "SELECT * FROM music "
				+(category != null ? "WHERE category LIKE '"+category+"' " : "")
				+(artist != null ? (category != null ? "AND " : "WHERE ")+"artist LIKE '%"+artist+"%' " : "")
				+(bpm < 1 ? "" : (category != null || artist != null ? "AND " : "WHERE ")+"bpm = '"+bpm+"' ")
				+(diff < 1.0 ? "" : (category != null || artist != null || bpm >= 1 ? "AND " : "WHERE ")+"(basic = '"+diff+"' OR advanced = '"+diff+"' OR expert = '"+diff+"' OR master = '"+diff+"') ").replaceAll(".0", "")
				+"ORDER BY random() LIMIT "+limit;
		System.out.println(sql);
		return sql;
	}
	
	/*
	 * SELECT * FROM (title, artist, diff)
	 * WHERE
	 *   category LIKE '<category>'
	 *   artist LIKE '%<artist>%'
	 *   basic = <diff> or advanced = <diff> or expert = <diff> or master = <diff>
	 *   bpm = <bpm>
	 * 
	 * ORDER BY random() LIMIT <limit>
	 * 
	 * 
	 */
}
