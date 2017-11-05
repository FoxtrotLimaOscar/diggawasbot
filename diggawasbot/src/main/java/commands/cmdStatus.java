package commands;

import core.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.SETTINGS;
import util.Shelf;

import java.awt.*;
import java.util.Arrays;

public class cmdStatus implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {

        if(Tools.hasPermission("ADM", "den Status zu wechseln", event)) {
            try {

                String out = "";
                for( String arg : args ) {

                    out += " " + arg;

                }
                out = out.substring(1);
                event.getJDA().getPresence().setGame(Game.of(out));
                Shelf.write("Game", out);
                event.getTextChannel().sendMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":information_source: - Neuer Status")
                        .setDescription("Der Status wurde auf " + out + " gesetzt.")
                        .build()
                ).queue();

            } catch(StringIndexOutOfBoundsException e ) {

                event.getTextChannel().sendMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":information_source: - BEFEHL: " + Tools.prefix(event.getGuild()) + "status")
                        .setDescription(Tools.prefix(event.getGuild()) + "status <Neuer Status>")
                        .build()
                ).queue();

            }
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
