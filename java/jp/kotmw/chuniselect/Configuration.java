package jp.kotmw.chuniselect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	
	private String path, user, password;
	private boolean debug = false;
	
	public Configuration(File file) throws IOException {
		Properties properties = new Properties();
		if(!file.exists()) {
			properties.setProperty("Path", "//localhost:3306/Chunithm");
			properties.setProperty("User", "root");
			properties.setProperty("PassWord", "root");
			properties.setProperty("Debug", "false");
			properties.store(new FileOutputStream(file), "");
			return;
		}
		try(InputStream stream = new FileInputStream(file)) {
			properties.load(stream);
			path = properties.getProperty("Path");
			user = properties.getProperty("User");
			password = properties.getProperty("PassWord");
			debug = Boolean.parseBoolean(properties.getProperty("Debug", "false"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getPath() {
		return path;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public boolean isDebug() {
		return debug;
	}

}
