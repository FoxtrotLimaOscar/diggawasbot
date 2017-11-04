package commands;

import core.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class cmdMusicChannel implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        try {
            if(!args[0].equals("none")) {
                util.Shelf.write("MusicChannel" + event.getGuild().getId(), event.getGuild().getTextChannelsByName(args[0], false).get(0).getId());
                event.getTextChannel().sendMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":information_source: - MUSICCHANNEL")
                        .setDescription(Tools.getMusicChannel(event.getGuild()).getAsMention() + " ist der neue MusicChannel")
                        .build()
                ).queue();
            } else {
                util.Shelf.write("MusicChannel" + event.getGuild().getId(), "NoMusicChannel");
                event.getTextChannel().sendMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":information_source: - MUSICCHANNEL")
                        .setDescription("Es können nun überall MusicCommands benutzt werden")
                        .build()
                ).queue();
            }
        } catch (IndexOutOfBoundsException e) {
            String MusicChannel;
            try {
                MusicChannel = "\nAktueller MusicChannel: ";
                MusicChannel += Tools.getMusicChannel(event.getGuild()).getAsMention();
            } catch (NullPointerException e2) {
                MusicChannel = "\nAktuell ist kein Channel Festgelegt";
            }
            event.getTextChannel().sendMessage(new EmbedBuilder()
                    .setColor(Color.blue)
                    .setTitle(":information_source: - " + Tools.prefix(event.getGuild()) + "MUSICCHANNEL")
                    .addField("Info", "Begrenzt die Channels in denen " + event.getGuild().getSelfMember().getEffectiveName() +
                            " MusicCommands akzeptiert. Mit \"none\" kann man " + event.getGuild().getSelfMember().getEffectiveName() +
                            " wieder alle Channel akzeptieren lassen." + MusicChannel, false)
                    .addField("Benutzung", Tools.prefix(event.getGuild()) + "musicchannel <channelname / \"none\">", false)
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
