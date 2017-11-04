package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class cmdFlipACoin implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        Random random = new Random();
        Message message = event.getTextChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.blue)
                .setTitle(":game_die: - FLIPACOIN")
                .setDescription("Münze wird geworfen")
                .build()
        ).complete();
        message.editMessage(new EmbedBuilder()
                .setColor(Color.blue)
                .setTitle(":game_die: - FLIPACOIN")
                .setDescription("Münze wird geworfen.")
                .build()
        ).queueAfter(500,TimeUnit.MILLISECONDS);
        message.editMessage(new EmbedBuilder()
                .setColor(Color.blue)
                .setTitle(":game_die: - FLIPACOIN")
                .setDescription("Münze wird geworfen..")
                .build()
        ).queueAfter(1000,TimeUnit.MILLISECONDS);
        message.editMessage(new EmbedBuilder()
                .setColor(Color.blue)
                .setTitle(":game_die: - FLIPACOIN")
                .setDescription("Münze wird geworfen...")
                .build()
        ).queueAfter(1500,TimeUnit.MILLISECONDS);
        if(random.nextBoolean()) {
            message.editMessage(new EmbedBuilder()
                    .setColor(Color.blue)
                    .setTitle(":game_die: - FLIPACOIN")
                    .setDescription("Münze wird geworfen... KOPF")
                    .build()
            ).queueAfter(2, TimeUnit.SECONDS);
        } else {
            message.editMessage(new EmbedBuilder()
                    .setColor(Color.blue)
                    .setTitle(":game_die: - FLIPACOIN")
                    .setDescription("Münze wird geworfen... ZAHL")
                    .build()
            ).queueAfter(2, TimeUnit.SECONDS);
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
