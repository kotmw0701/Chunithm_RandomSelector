package jp.kotmw.chuniselect;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter {
	
	String prefix = "/", separator = "\r\n";
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot())
			return;
		String text = event.getMessage().getContentStripped();
		if(text == null || text.isEmpty())
			return;
		if(!text.startsWith(prefix))
			return;
		String[] args = text.split(" ");
		if(args[0].equalsIgnoreCase("/random")) {
			int limit = Integer.parseInt(getCommand(args, 2, "3"));
			String diff = getCommand(args, 3, null);
			String category = getCommand(args, 4, null);
			String artist = getCommand(args, 5, null);
			String bpm = getCommand(args, 6, "0");
			event.getChannel().sendMessage(event.getAuthor().getAsMention()).embed(convertEmbed(RandomSelector.randomget(limit, category, diff, artist, bpm))).complete();
		}
	}
	
	private MessageEmbed convertEmbed(ResultSet set) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);
		builder.setTitle("おすすめ曲を教えてあげるね！ ");
		builder.setDescription(randomSerif());
		try {
			while (set.next())
				builder.addField("『"+set.getString("title")+"』", set.getString("artist")+separator+set.getString("category"), true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return builder.build();
	}
	
	private String getCommand(String[] str, int length, String default_var) {
		return str.length >= length && !str[length-1].contains("-") ? str[length-1] : default_var;
	}
	
	private String randomSerif() {
		List<String> serifs = Arrays.asList("ねっ、簡単でしょ～","さあやってみよう！","意外と簡単、やってみよ～！");
		Collections.shuffle(serifs);
		return serifs.get(0);
	}
	/*
	 * コマンドアイディア
	 * 
	 * /random [曲数] [難易度] [カテゴリ] [作曲者] 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
}
