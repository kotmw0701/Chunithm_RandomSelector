package jp.kotmw.chuniselect.selector;

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
			set = connection.createStatement().executeQuery(execute(limit, category, diff, artist, bpm));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return set;
	}
	
	private static String execute(int limit, String category, String str_diff, String artist, String str_bpm) {
		String ul_diff = getData(str_diff, false), ul_bpm = getData(str_bpm, false);
		double diff = Double.parseDouble(getData(str_diff, true) != null ? getData(str_diff, true).replace("+", ".7") : "0.0");
		int bpm = Integer.parseInt(getData(str_bpm, true) != null ? getData(str_bpm, true) : "0");
		String sql = "SELECT * FROM music "
				+(category != null ? "WHERE category LIKE '"+category+"' " : "")
				+(bpm < 1 ? "" : (category != null ? "AND " : "WHERE ")+"bpm "+ul_bpm+" '"+bpm+"' ")
				+(artist != null ? (category != null || bpm >= 1 ? "AND " : "WHERE ")+"artist LIKE '%"+artist+"%'" : "")
				+(diff < 1.0 ? "" : (category != null || artist != null || bpm >= 1 ? "AND " : "WHERE ")+"(basic "+ul_diff+" '"+diff+"' OR advanced "+ul_diff+" '"+diff+"' OR expert "+ul_diff+" '"+diff+"' OR master "+ul_diff+" '"+diff+"') ").replaceAll(".0", "")
				+"ORDER BY random() LIMIT "+limit;
		System.out.println(sql);
		return sql;
	}
	
	private static String getData(String str, boolean isvalue) {
		if(str == null || str.isEmpty() || !str.contains(":") || str.split(":").length < 2)
			return isvalue ? str : "=";
		String[] args = str.split(":");
		if(isvalue) return args[0];
		String ul = "=";
		if(args[1].equalsIgnoreCase("up")) ul = ">=";
		else if(args[1].equalsIgnoreCase("low")) ul = "<=";
		return ul;
	}
	
	/*
	 * SELECT * FROM (title, artist, diff)
	 * WHERE
	 *   category LIKE '<category>'
	 *   (basic = <diff> or advanced = <diff> or expert = <diff> or master = <diff>
	 *   bpm = <bpm>
	 *   
	 *   artist LIKE '%<artist>%'
	 * 
	 * ORDER BY random() LIMIT <limit>
	 * 
	 * 
	 */
}
