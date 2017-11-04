package commands;

import core.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class cmdPrefix implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        try {
            util.Shelf.write("Prefix" + event.getGuild().getId(), args[0]);
            event.getTextChannel().sendMessage(new EmbedBuilder()
                    .setColor(Color.blue)
                    .setTitle(":information_source: - PREFIX")
                    .setDescription("Das Prefix wurde auf " + Tools.prefix(event.getGuild()) + " gesetzt!")
                    .build()
            ).queue();
        } catch (IndexOutOfBoundsException e) {
            event.getTextChannel().sendMessage(new EmbedBuilder()
                    .setColor(Color.blue)
                    .setTitle(":information_source: - " + Tools.prefix(event.getGuild()) + "PREFIX")
                    .addField("Info", "Stellt das Prefix ein. Aktuelles Prefix: " + Tools.prefix(event.getGuild()), false)
                    .addField("Benutzung", Tools.prefix(event.getGuild()) + "prefix <Neues Prefix>", false)
                    .build()
            ).queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
