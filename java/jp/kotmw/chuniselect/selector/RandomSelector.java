package jp.kotmw.chuniselect.selector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringJoiner;

import jp.kotmw.chuniselect.Configuration;
import jp.kotmw.chuniselect.Main;

public class RandomSelector {
	
	public static ResultSet randomget(int limit, String category, String diff, String artist, String bpm) {
		ResultSet set = null;
		Configuration config = Main.configuration;
		try(Connection connection = DriverManager.getConnection("jdbc:mysql:"+config.getPath(), config.getUser(), config.getPassword()); Statement statement = connection.createStatement()) {
			set = statement.executeQuery(execute(limit, category, diff, artist, bpm));
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
				+(category != null ? createCategorySQL(category) : "")
				+(bpm < 1 ? "" : (category != null ? "AND " : "WHERE ")+"bpm "+ul_bpm+" '"+bpm+"' ")
				+(artist != null ? (category != null || bpm >= 1 ? "AND " : "WHERE ")+"artist LIKE '%"+artist+"%' " : "")
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
	
	private static String createCategorySQL(String categorystack) {
		if(categorystack.indexOf(",") == 1) categorystack.replaceFirst(",", "");
		StringJoiner joiner = new StringJoiner("' OR category LIKE '", "WHERE (category LIKE '", "') ");
		for(String category : categorystack.split(",")) joiner.add(category);
		return joiner.toString();
	}
	
	/*
	 * SELECT * FROM music
	 * WHERE
	 *   (category LIKE '<category_1>' OR category LIKE '<category_2>' OR category LIKE '<category_3>' OR category LIKE '<category_4>' ...)
	 *   (basic = <diff> OR advanced = <diff> OR expert = <diff> OR master = <diff>
	 *   bpm = <bpm>
	 *   artist LIKE '%<artist>%'
	 * 
	 * ORDER BY random() LIMIT <limit>
	 * 
	 * 
	 */
}
