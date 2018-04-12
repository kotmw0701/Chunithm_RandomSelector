package jp.kotmw.chuniselect.Listener;

import java.awt.Color;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jp.kotmw.chuniselect.musicdatas.Chunithm;
import jp.kotmw.chuniselect.selector.RandomSelector;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter {
	
	private String prefix = "/", separator = System.lineSeparator();
	
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
			String limit = getCommand(args, 2, "3");
			String diff = getCommand(args, 3, null);
			String category = getCommand(args, 4, null);
			String artist = getCommand(args, 5, null);
			String bpm = getCommand(args, 6, "0");
			if(!limit.matches("^\\d+$") || (diff != null && !diff.matches("^\\d{1,2}\\+?(:up|:low)?")) || (bpm != null && !bpm.matches("^\\d+(:up|:low)?"))) {
				lateRemover(event.getChannel().sendMessage("コマンドにエラーが存在します").complete());
				return;
			}
			try {
				event.getChannel().sendMessage(event.getAuthor().getAsMention()).embed(convertEmbed(RandomSelector.selector.randomget(Integer.parseInt(limit), category, diff, artist, bpm))).complete();
			} catch (NumberFormatException | SQLException e) {
				lateRemover(event.getChannel().sendMessage(e.getMessage()+separator+"コマンドにエラーが存在します").complete());
				e.printStackTrace();
			}
		} else if(args[0].equalsIgnoreCase("/help")) {
			event.getChannel().sendMessage(setSeparator("<> : 必須  [] : 任意","/random [曲数] [難易度[:up/:low]] [カテゴリ] [BPM[:up/:low]]"));
			return;
		}
	}
	
	private MessageEmbed convertEmbed(List<Chunithm> chunithms) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);
		builder.setTitle("おすすめ曲を教えてあげるね！ ");
		builder.setDescription(randomSerif());
		chunithms.forEach(uni -> builder.addField("『"+uni.getTitle()+"』", uni.getArtist()+separator+uni.getCategory(), true));
		return builder.build();
	}
	
	private String setSeparator(String... args) {
		StringBuilder builder = new StringBuilder();
		for(String arg : args)
			builder.append(arg).append(separator);
		return builder.toString();
	}
	
	private String getCommand(String[] str, int length, String default_var) {
		return str.length >= length && (!str[length-1].equalsIgnoreCase("-") && !str[length-1].equalsIgnoreCase("none")) ? str[length-1] : default_var;
	}
	
	private String randomSerif() {
		List<String> serifs = Arrays.asList("ねっ、簡単でしょ～","さあやってみよう！","意外と簡単、やってみよ～！");
		Collections.shuffle(serifs);
		return serifs.get(0);
	}
	
	private void lateRemover(Message message) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				message.delete().complete();
			}
		}).start();
	}
	
	/*
	 * コマンドアイディア
	 * 
	 * /random [曲数] [難易度] [カテゴリ] [作曲者] [BPM]
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
}
