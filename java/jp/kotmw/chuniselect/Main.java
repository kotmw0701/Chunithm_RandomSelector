package jp.kotmw.chuniselect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {

	static boolean base = false;
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, LoginException, InterruptedException {
		//DataAdder.createChuniDataBase();
		if(base)
			return;
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
