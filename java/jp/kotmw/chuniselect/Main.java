package jp.kotmw.chuniselect;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.security.auth.login.LoginException;

import jp.kotmw.chuniselect.Listener.EventListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {

	public static Configuration configuration;
	
	static boolean base = false;
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, LoginException, InterruptedException {
		//DataAdder.createChuniDataBase();
		if(base)
			return;
		File file = new File("Settings.properties");
		if(!file.exists()) {
			new Configuration(file);
			System.out.println("コンフィグファイルが生成されました，自身の値に設定し直してから再起動してください");
			System.exit(0);
			return;
		}
		configuration = new Configuration(file);
		JDA jda = new JDABuilder(AccountType.BOT).setToken("NDI5NTUxMDMxNTg0NTU1MDEw.DaETbA.nYfkcARQerD8lUNZ1BVPVYtT4Ys").addEventListener(new EventListener()).buildBlocking();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			while(true) {
				String text = reader.readLine();
				if(text == null || text.isEmpty())
					continue;
				if(!text.startsWith("%"))
					continue;
				String[] command = text.split(" ");
				if(command[0].equalsIgnoreCase("%stop")) {
					jda.shutdown();
					System.exit(0);
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
