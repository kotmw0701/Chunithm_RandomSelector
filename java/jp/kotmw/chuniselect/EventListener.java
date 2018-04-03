package jp.kotmw.chuniselect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class EventListener {
	
	String prefix = "/", separator = "\r\n";
	
	@EventSubscriber
	public void onMessageRecive(MessageReceivedEvent event) {
		if(event.getAuthor().isBot())
			return;
		String text = event.getMessage().getContent();
		if(text == null || text.isEmpty())
			return;
		if(!text.startsWith(prefix))
			return;
		String[] args = text.split(" ");
		if(args[0].equalsIgnoreCase("/random")) {
			int limit = args.length >= 2 ? Integer.parseInt(args[1]) : 3;
			String diff = getCommand(args, 3);
			String category = getCommand(args, 4);
			String artist = getCommand(args, 5);
			int bpm = args.length >= 6 ? Integer.parseInt(args[5]) : 0;
			List<String> texts = new ArrayList<>();
			texts.add(event.getAuthor().mention());
			texts.addAll(RandomSelector.randomget(limit, category, diff, artist, bpm));
			texts.add("");
			texts.add(randomSerif());
			event.getChannel().sendMessage(convert(texts));
		}
	}
	
	private String convert(List<String> titles) {
		String string = "";
		for(String title : titles) {
			string += title + separator;
		}
		return string;
	}
	
	private String getCommand(String[] str, int length) {
		return str.length >= length && !str[length-1].contains("-") ? str[length-1] : null;
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
