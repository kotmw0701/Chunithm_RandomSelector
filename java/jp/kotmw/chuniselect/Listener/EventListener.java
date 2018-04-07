package jp.kotmw.chuniselect.Listener;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jp.kotmw.chuniselect.selector.RandomSelector;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class EventListener extends ListenerAdapter implements StatusListener {
	
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
			String limit = getCommand(args, 2, "3");
			String diff = getCommand(args, 3, null);
			String category = getCommand(args, 4, null);
			String artist = getCommand(args, 5, null);
			String bpm = getCommand(args, 6, "0");
			if(!limit.matches("^\\d+$") || (diff != null && !diff.matches("^\\d{1,2}\\+?(:up|:low)?")) || (bpm != null && !bpm.matches("^\\d+(:up|:low)?"))) {
				Message message = event.getChannel().sendMessage("コマンドにエラーが存在します").complete();
				new Runnable() {
					public void run() {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						message.delete().complete();
					}
				}.run();
				return;
			}
			event.getChannel().sendMessage(event.getAuthor().getAsMention()).embed(convertEmbed(RandomSelector.randomget(Integer.parseInt(limit), category, diff, artist, bpm))).complete();
		} else if(args[0].equalsIgnoreCase("/help")) {
			event.getChannel().sendMessage(setSeparator("<> : 必須  [] : 任意","/random [曲数] [難易度[:up/:low]] [カテゴリ] [BPM[:up/:low]]"));
			return;
		}
	}

	@Override
	public void onStatus(Status status) {
		if(!status.getInReplyToScreenName().equalsIgnoreCase("motlof_kotmw"))
		return;
		String text = status.getText().replaceAll("@motlof_kotmw\\s?", "").replace("\r", "").replace("\n", "");
		System.out.println(text);
		if(text == null || text.isEmpty())
			return;
		String[] args = text.split(" ");
		String reply = "";
		if(args[0].equalsIgnoreCase("random")) {
			String limit = getCommand(args, 2, "3");
			String diff = getCommand(args, 3, null);
			String category = getCommand(args, 4, null);
			String artist = getCommand(args, 5, null);
			String bpm = getCommand(args, 6, "0");
			if(!limit.matches("^\\d+$") || (diff != null && !diff.matches("^\\d{1,2}\\+?(:up|:low)?")) || (bpm != null && !bpm.matches("^\\d+(:up|:low)?"))) {
				reply = "コマンドにエラーが存在します";
			} else {
				ResultSet set = RandomSelector.randomget(Integer.parseInt(limit), category, diff, artist, bpm);
				try {
					while (set.next()) {
						reply += "『"+set.getString("title")+"』"+separator+set.getString("category")+separator+separator;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			Twitter twitter = new TwitterFactory().getInstance();
			try {
				twitter.updateStatus(new StatusUpdate("@"+status.getUser().getScreenName()+separator+reply).inReplyToStatusId(status.getId()));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
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
	
	private String setSeparator(String... args) {
		StringBuilder builder = new StringBuilder();
		for(String arg : args)
			builder.append(arg).append(separator);
		return builder.toString();
	}
	
	private String getCommand(String[] str, int length, String default_var) {
		return str.length >= length && !str[length-1].contains("-") ? str[length-1] : default_var;
	}
	
	private String randomSerif() {
		List<String> serifs = Arrays.asList("ねっ、簡単でしょ～","さあやってみよう！","意外と簡単、やってみよ～！");
		Collections.shuffle(serifs);
		return serifs.get(0);
	}

	@Override
	public void onException(Exception ex) {}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {}

	@Override
	public void onStallWarning(StallWarning warning) {}
	
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
