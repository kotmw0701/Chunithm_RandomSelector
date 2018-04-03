package jp.kotmw.chuniselect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RandomSelector {
	
	public static List<String> randomget() {
		return randomget(3, null, null, null, 0);
	}
	
	public static List<String> randomget(int limit) {
		return randomget(limit, null, null, null, 0);
	}
	
	public static List<String> randomget(int limit, String category) {
		return randomget(limit, category, null, null, 0);
	}
	
	public static List<String> randomget(int limit, String category, String diff) {
		return randomget(limit, category, diff, null, 0);
	}
	
	public static List<String> randomget(int limit, String category, String diff, String artist) {
		return randomget(limit, category, diff, artist, 0);
	}
	
	public static List<String> randomget(int limit, String category, String diff, String artist, int bpm) {
		ArrayList<String> datas = new ArrayList<>();
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:C://sqlite/chunithm.db");
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(createSQL(limit, category, diff, artist, bpm));
			while(set.next()) 
				datas.add(set.getString("category")+" : "+set.getString("title")+" : "+set.getString("master").replace(".7", "+"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return datas;
	}
	
	private static String createSQL(int limit, String category, String str_diff, String artist, int bpm) {
		double diff = Double.parseDouble(str_diff != null ? str_diff.replace("+", ".7") : "0.0");
		String sql = "SELECT * FROM music "
				+(category != null ? "WHERE category LIKE '"+category+"' " : "")
				+(artist != null ? (category != null ? "AND " : "WHERE ")+"artist LIKE '%"+artist+"%' " : "")
				+(diff < 1.0 ? "" : (category != null || artist != null ? "AND " : "WHERE ")+"(basic = '"+diff+"' OR advanced = '"+diff+"' OR expert = '"+diff+"' OR master = '"+diff+"') ").replaceAll(".0", "")
				+"ORDER BY random() LIMIT "+(limit < 1 ? 3 : limit);
		return sql;
	}
	
	/*
	 * SELECT * FROM (title, artist, diff)
	 * WHERE
	 *   title.category LIKE '<category>'
	 *   artist.artist LIKE '%<artist>%'
	 *   diff.basic = <diff> or diff.advanced = <diff> or diff.expert = <diff> or diff.master = <diff>
	 * 
	 * ORDER BY random() LIMIT <limit>
	 */
}
