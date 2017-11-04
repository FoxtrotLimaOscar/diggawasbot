package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.SETTINGS;

public class cmdVersion implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage("Version: " + SETTINGS.VERSION + ": " + SETTINGS.VERSIONNAME + " :tada:").queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
