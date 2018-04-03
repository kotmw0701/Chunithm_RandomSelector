package jp.kotmw.chuniselect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataAdder {
	
	public static void createChuniDataBase() throws ClassNotFoundException, IOException {
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		
		File file = new File("musicdata.txt");
		
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			connection = DriverManager.getConnection("jdbc:sqlite:C://sqlite/chunithm.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			statement.executeUpdate("CREATE TABLE music (category, title, artist, bpm, basic, advanced, expert, master)");
			//statement.executeUpdate("create table title (category, title);");
			//statement.executeUpdate("create table artist (title, artist);");
			//statement.executeUpdate("create table bpm (title, bpm);");
			//statement.executeUpdate("create table diff (title, basic, advanced, expert, master);");
			String text = reader.readLine();
			while(text != null) {
				String[] args = text.replace("'", "''").split("//");
				statement.executeUpdate("INSERT INTO music VALUES('"+args[0]+"', '"+args[1]+"', '"+args[2]+"', '"+args[3]+"', '"+args[4]+"', '"+args[5]+"', '"+args[6]+"', '"+args[7]+"')");
				//statement.executeUpdate("insert into title values('"+args[0]+"', '"+args[1]+"')");
				//statement.executeUpdate("insert into artist values('"+args[1]+"', '"+args[2]+"')");
				//statement.executeUpdate("insert into bpm values('"+args[1]+"', '"+args[3]+"')");
				//statement.executeUpdate("insert into diff values('"+args[1]+"', '"+args[4]+"', '"+args[5]+"', '"+args[6]+"', '"+args[7]+"')");
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
