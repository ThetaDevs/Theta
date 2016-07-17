package com.srgood.dbot.commands;

import com.srgood.dbot.Command;
import com.srgood.dbot.Main;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Shutdown implements Command {
	private final String help = "Used to shutdown Reasons. Use: " + Main.prefix + "shutdown -OR- " + Main.prefix + "shutdown override [override key]";
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		long uid = Long.parseLong(event.getAuthor().getId());
		
		
		try {
			if (164117897025683456L == uid || 138048665112543233L == uid) {
				event.getChannel().sendMessage("Shutting down! " + event.getAuthor().getAsMention());
				Main.SaveParams();
				Main.jda.shutdown();
			} else {
				if (args[0].toLowerCase().equals("override")) {
					if (Main.Okey.equals(args[1])) {
						event.getChannel().sendMessage("Valid key. Shutting down! " + event.getAuthor().getAsMention());
						Main.SaveParams();
						Main.jda.shutdown();
					} else {
						event.getChannel().sendMessage("Bad key " + event.getAuthor().getAsMention());
					}
				} else {
					
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			try {
				if (args[0].toLowerCase().equals("override")) {
					event.getChannel().sendMessage("Invalid Arguments, you should quit the debate team " + event.getAuthor().getAsMention());
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				event.getChannel().sendMessage("You arent me. you cant do that " + event.getAuthor().getAsMention());
			}
			
		}
		
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return help;
	}

	@Override
	public void executed(boolean success, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return;
	}

}