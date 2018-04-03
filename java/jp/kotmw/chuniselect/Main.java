package jp.kotmw.chuniselect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;


public class Main {

	static boolean base = false;
	static IDiscordClient client;
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		//RandomSelector.createChuniDataBase();
		if(base)
			return;
		client = createClient("******", true);
		client.getDispatcher().registerListener(new EventListener());
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			while(true) {
				String text = reader.readLine();
				if(text == null || text.isEmpty())
					continue;
				if(!text.startsWith("%"))
					continue;
				String[] command = text.split(" ");
				if(command[0].equalsIgnoreCase("%stop")) {
					client.logout();
					System.exit(0);
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static IDiscordClient createClient(String token, boolean login) { // Returns a new instance of the Discord client
		ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
		clientBuilder.withToken(token); // Adds the login info to the builder
		try {
			if (login) {
				return clientBuilder.login(); // Creates the client instance and logs the client in
			} else {
				return clientBuilder.build(); // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
			}
		} catch (DiscordException e) { // This is thrown if there was a problem building the client
			e.printStackTrace();
			return null;
		}
	}
}
