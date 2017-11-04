package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class cmdServer implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        String out = " ";
        for(Guild g : event.getJDA().getGuilds()) {

            out += g.getName() + " (ID: " + g.getId() + ")\n\n";

        }
        event.getTextChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.blue)
                .setTitle(":information_source:   " + event.getGuild().getSelfMember().getEffectiveName() + " läuft auf folgenden Servern:")
                .setDescription(out)
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
