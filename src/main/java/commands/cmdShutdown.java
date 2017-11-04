package commands;

import core.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.SETTINGS;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class cmdShutdown implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {

        if(Tools.hasPermission("BOT", event.getJDA().getSelfUser().getName() + " herunterzufahren", event)) {
            for (Guild g : event.getJDA().getGuilds()) {

                if(event.getGuild().equals(g)) {
                    event.getTextChannel().sendMessage(new EmbedBuilder()
                            .setColor(Color.orange)
                            .setTitle(":small_red_triangle_down: SHUTTING DOWN")
                            .setDescription( g.getSelfMember().getEffectiveName() + " wird in 5 Sekunden heruntergefahren!")
                            .build()
                    ).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                } else {
                    g.getTextChannels().get(0).sendMessage(new EmbedBuilder()
                            .setColor(Color.orange)
                            .setTitle(":small_red_triangle_down: SHUTTING DOWN")
                            .setDescription( g.getSelfMember().getEffectiveName() + " wird in 5 Sekunden heruntergefahren!")
                            .build()
                    ).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                }

            }
            System.out.println(Tools.Stamp("log", event.getJDA().getSelfUser().getName(), "\"" + event.getMember().getEffectiveName() +  "\" in \"" + event.getGuild().getName() + "\" used " + event.getMessage().getContent()));
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(1);
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
