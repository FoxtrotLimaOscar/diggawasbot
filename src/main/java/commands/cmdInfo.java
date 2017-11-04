package commands;

import core.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.SETTINGS;

import java.awt.*;

public class cmdInfo implements Command{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {

        String MusicChannel;
        if(Tools.MusicChannelIsForced(event.getGuild())) {
            MusicChannel = Tools.getMusicChannel(event.getGuild()).getAsMention();
        } else {
            MusicChannel = "Alle erlaubt";
        }

        String DJRole;
        try {
            DJRole = Tools.getDJRole(event.getGuild()).getAsMention();
        } catch (NullPointerException exception) {
            DJRole = "keine";
        }

        String roserver = "";
        for(Guild g : event.getJDA().getGuilds()) {

            roserver += g.getName() + "\n";

        }
        event.getTextChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.blue)
                .setTitle(":information_source: - " + event.getJDA().getSelfUser().getName().toUpperCase())
                .setDescription("Dieser Bot wurde ausschließlich für den GamingNationServer https://discord.gg/fVPeS7k von " +
                        event.getJDA().getUserById("265955256439930882").getAsMention() + " & " +
                        event.getJDA().getUserById("289423581077831681").getAsMention() + " entwickelt!")
                .addField("Coder", event.getJDA().getUserById("289423581077831681").getAsMention(), true)
                .addField("Hoster", event.getJDA().getUserById("265955256439930882").getAsMention(), true)
                .addField("Version", SETTINGS.VERSION + ": " + SETTINGS.VERSIONNAME, true)
                .addField("Prefix", Tools.prefix(event.getGuild()), true)
                .addField("MusicChannel", MusicChannel, true)
                .addField("DefaulChannel", Tools.getDefaulChannel(event.getGuild()).getAsMention(), true)
                .addField("DJRolle", DJRole, true)
                .addField("Hilfe", Tools.prefix(event.getGuild()) + "help", true)
                .addField(event.getJDA().getSelfUser().getName() + " läuft aktuell auf folgenden Servern", roserver, false)
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
