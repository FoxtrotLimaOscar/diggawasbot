package commands;

import core.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

public class cmdSettings implements Command{
    private int Timeout = 0;
    private int Page = 1;
    private int maxPage = 6;
    private int button1 = 1;
    private int button2 = 1;

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) throws ParseException, IOException {
        if(args.length < 1) {
            Message message = event.getTextChannel().sendMessage(new EmbedBuilder()
                    .setColor(Color.blue)
                    .setTitle(":gear: - EINSTELLUNGEN")
                    .addField("MusicChannel", "none", true)
                    .addField("ForcedMusicChannel", "none", true)
                    .addField("BotChannel", "none", true)
                    .addField("DJRolle", "none", true)
                    .addField("Prefix", "none", true)
                    .addField("Volume", "none", true)
                    .setFooter("Seite " + Page + "/" + maxPage, event.getJDA().getSelfUser().getAvatarUrl())
                    .build()
            ).complete();
            message.addReaction("⬅").queue();
            message.addReaction("➡").queue();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        Timeout++;
                        if(message.getTextChannel().getMessageById(message.getId()).complete().getReactions().get(0).getCount() != button1) {
                            button1 = message.getTextChannel().getMessageById(message.getId()).complete().getReactions().get(0).getCount();
                            Timeout = 0;
                            Page = getPageContent(message, Page -1, maxPage);
                        }
                        if(message.getTextChannel().getMessageById(message.getId()).complete().getReactions().get(1).getCount() != button2) {
                            button2 = message.getTextChannel().getMessageById(message.getId()).complete().getReactions().get(1).getCount();
                            Timeout = 0;
                            Page = getPageContent(message, Page +1, maxPage);
                        }
                        if(Timeout == 60) {
                            event.getMessage().delete().queue();
                            message.delete().queue();
                            this.cancel();
                        }
                    } catch (Exception exception) {
                        //Noch nichts
                    }

                }
            }, 0 ,500);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }

    private static int getPageContent(Message message, int page, int maxpage) {
        if(page == maxpage +1) page = 1;
        if(page == 0) page = maxpage;
        switch (page) {
            case 1:
                message.editMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":gear: - EINSTELLUNGEN")
                        .addField("MusicChannel", "none", true)
                        .addField("ForcedMusicChannel", "none", true)
                        .addField("BotChannel", "none", true)
                        .addField("DJRolle", "none", true)
                        .addField("Prefix", "none", true)
                        .setFooter("Seite " + page + "/" + maxpage, message.getJDA().getSelfUser().getAvatarUrl())
                        .build()
                ).queue();
                break;

            case 2:
                message.editMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":gear: - MUSICCHANNEL")
                        .addField("Beschreibung", "Der MusicChannel ist der TextChannel in dem der Bot alle Musik relevanten Nachrichten postet.", false)
                        .addField("Eingabe", Tools.prefix(message.getGuild()) + "set mc < Der neue MusicChannel als #Mention >", false)
                        .setFooter("Seite " + page + "/" + maxpage, message.getJDA().getSelfUser().getAvatarUrl())
                        .build()
                ).queue();
                break;

            case 3:
                message.editMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":gear: - FORCEDMUSICCHANNEL")
                        .addField("Beschreibung", "Wenn aktiviert werden nur MusikCommands im festgelegten MusicChannel akzeptiert.", false)
                        .addField("Eingabe", Tools.prefix(message.getGuild()) + "set forcedmc < true / false >", false)
                        .setFooter("Seite " + page + "/" + maxpage, message.getJDA().getSelfUser().getAvatarUrl())
                        .build()
                ).queue();
                break;

            case 4:
                message.editMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":gear: - BOTCHANNEL")
                        .addField("Beschreibung", "Legt den TextChannel fest in den der Bot alle nicht Music relevanten Machrichten postet.", false)
                        .addField("Eingabe", Tools.prefix(message.getGuild()) + "set bc < Der neue BotChannel als #Mention >", false)
                        .setFooter("Seite " + page + "/" + maxpage, message.getJDA().getSelfUser().getAvatarUrl())
                        .build()
                ).queue();
                break;

            case 5:
                message.editMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":gear: - DJROLLE")
                        .addField("Beschreibung", "MusicCommands wie " + Tools.prefix(message.getGuild()) + "skip oder " + Tools.prefix(message.getGuild()) + "stop werden für alle User ohne die Festgelegte Rolle gesperrt.", false)
                        .addField("Eingabe", Tools.prefix(message.getGuild()) + "set djr < Die DJRolle als @Mention / none >", false)
                        .setFooter("Seite " + page + "/" + maxpage, message.getJDA().getSelfUser().getAvatarUrl())
                        .build()
                ).queue();
                break;

            case 6:
                message.editMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":gear: - PREFIX")
                        .addField("Beschreibung", "Legt den Invoke, das Prefix, das/die erste/ersten Zeichen Fest die noch vor dem Command geschrieben werden.", false)
                        .addField("Eingabe", Tools.prefix(message.getGuild()) + "set prefix < Das neue Prefix >", false)
                        .setFooter("Seite " + page + "/" + maxpage, message.getJDA().getSelfUser().getAvatarUrl())
                        .build()
                ).queue();
                break;
        }
        return page;
    }
}