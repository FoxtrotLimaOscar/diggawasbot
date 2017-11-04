package listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class readyListener extends ListenerAdapter {

    public void onReady(ReadyEvent event) {

        String out = "\nDieser Bot läuft auf folgenden Servern:\n";
        for(Guild g : event.getJDA().getGuilds()) {

            out += g.getName() + " (ID: " + g.getId() + ")\n";

        }
        System.out.print(out);

        for (Guild g : event.getJDA().getGuilds()) {

            g.getTextChannels().get(0).sendMessage(new EmbedBuilder()
                    .setColor(Color.orange)
                    .setTitle(":small_red_triangle:   BOOTED UP")
                    .setDescription( g.getSelfMember().getEffectiveName() + " ist nun Betriebsbereit")
                    .build()
            ).queue(message -> message.delete().queueAfter(60, TimeUnit.SECONDS));

        }

    }

}
