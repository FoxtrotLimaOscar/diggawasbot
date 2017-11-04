package commands;

import core.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class cmdHelp implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {

        event.getTextChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.blue)
                .setTitle(":information_source: - HELP")
                .setDescription(event.getAuthor().getAsMention() + " du hast eine Nachricht erhalten!")
                .build())
                .queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));

        String prefix = "\n" + Tools.prefix(event.getGuild());
        event.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                .setColor(Color.blue)
                .setTitle(":information_source: - HILFE")
                .setDescription("Das sind alle Commands (Abgesehen von den EasterEggs natürlich :wink:)")
                .addField("Standart", prefix + "servers" + prefix + "help" + prefix + "info" + prefix +
                        "version" + prefix + "ping" + prefix + "flipacoin", true)
                .addField("Musik", prefix + "search" + prefix + "play" + prefix + "np" + prefix + "pause" + prefix + "resume" +
                        prefix + "skip" + prefix + "stop" + prefix + "queue" + prefix + "shuffle" + prefix + "favsong",true)
                .addField("Bot", prefix + "settings" + prefix + "speedtest" + prefix + "dj" + prefix + "musicchannel" +
                        prefix + "prefix" + prefix + "shutdown" + prefix + "status", true)
                .build()
        ).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
