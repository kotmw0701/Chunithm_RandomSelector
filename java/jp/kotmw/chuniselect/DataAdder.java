package jp.kotmw.chuniselect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DataAdder {
	
	public static void createChuniDataBase() throws ClassNotFoundException, IOException {
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		
		File file = new File("musicdata.txt");
		
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			connection = DriverManager.getConnection("jdbc:sqlite:C://sqlite/musicgame.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			statement.executeUpdate("CREATE TABLE music (category TEXT, title TEXT, artist TEXT, bpm INTEGER, basic REAL, advanced REAL, expert REAL, master REAL)");
			//statement.executeUpdate("CREATE TABLE title (category TEXT, title TEXT);");
			//statement.executeUpdate("CREATE TABLE artist (title TEXT, artist TEXT);");
			//statement.executeUpdate("CREATE TABLE bpm (title TEXT, bpm INTEGER);");
			//statement.executeUpdate("CREATE TABLE diff (title TEXT, basic REAL, advanced REAL, expert REAL, master REAL);");
			String text = reader.readLine();
			while(text != null) {
				String[] args = text.replace("'", "''").split("//");
				System.out.println(Arrays.toString(args));
				statement.executeUpdate("INSERT INTO music VALUES('"+args[0]+"', '"+args[1]+"', '"+args[2]+"', '"+args[3]+"', '"+args[4]+"', '"+args[5]+"', '"+args[6]+"', '"+args[7]+"')");
				//statement.executeUpdate("INSERT INTO title VALUES('"+args[0]+"', '"+args[1]+"')");
				//statement.executeUpdate("INSERT INTO artist VALUES('"+args[1]+"', '"+args[2]+"')");
				//statement.executeUpdate("INSERT INTO bpm VALUES('"+args[1]+"', '"+args[3]+"')");
				//statement.executeUpdate("INSERT INTO diff VALUES('"+args[1]+"', '"+args[4]+"', '"+args[5]+"', '"+args[6]+"', '"+args[7]+"')");
				text = reader.readLine();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
