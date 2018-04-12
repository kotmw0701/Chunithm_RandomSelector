package jp.kotmw.chuniselect.musicdatas;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Chunithm {
	
	String title, artist, category;
	int bpm;
	double basic, advanced, expert, master;
	
	public Chunithm(ResultSet set) {
		try {
			title = set.getString("title");
			artist = set.getString("artist");
			category = set.getString("category");
			bpm = set.getInt("bpm");
			basic = set.getDouble("basic");
			advanced = set.getDouble("advanced");
			expert = set.getDouble("expert");
			master = set.getDouble("master");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getTitle() {return title;}

	public String getArtist() {return artist;}

	public String getCategory() {return category;}

	public int getBpm() {return bpm;}

	public double getBasic() {return basic;}

	public double getAdvanced() {return advanced;}

	public double getExpert() {return expert;}

	public double getMaster() {return master;}
}
