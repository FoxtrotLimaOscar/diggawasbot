package commands;

import core.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Shelf;

import java.awt.*;

public class cmdDJ implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        try {
            if(!args[0].equals("none")) {
                util.Shelf.write("DJRole" + event.getGuild().getId(), event.getGuild().getRolesByName(args[0], false).get(0).getId());
                event.getTextChannel().sendMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":information_source: - DJ")
                        .setDescription(event.getGuild().getRoleById(Shelf.read("DJRole" + event.getGuild().getId())).getName() + " ist die neue DJ Rolle")
                        .build()
                ).queue();
            } else {
                util.Shelf.write("DJRole" + event.getGuild().getId(), "none");
                event.getTextChannel().sendMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":information_source: - DJ")
                        .setDescription("Es kann nun jeder MusicCommands benutzen")
                        .build()
                ).queue();
            }
        } catch (IndexOutOfBoundsException e) {
            event.getTextChannel().sendMessage(new EmbedBuilder()
                    .setColor(Color.blue)
                    .setTitle(":information_source: - " + Tools.prefix(event.getGuild()) + "DJ")
                    .addField("Info", "Begrenzt die Rollen von denen " + event.getGuild().getSelfMember().getEffectiveName() +
                            " " + Tools.prefix(event.getGuild()) + "skip akzeptiert auf eine DJ Rolle. " + "Mit \"none\" kann man den Bot wieder alle Ränge " +
                            "akzeptieren lassen.", false)
                    .addField("Benutzung", Tools.prefix(event.getGuild()) + "dj <channelname / \"none\">", false)
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
