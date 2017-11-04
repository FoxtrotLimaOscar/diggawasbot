package core;

import audioCore.AudioInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sun.audio.AudioPlayer;
import util.BOTSETTINGS;
import util.Shelf;
import util.TOPSECRET;


import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;

public class Tools {

    public static String setActiveToken() throws InterruptedException {

        //Übersetzungen
        Scanner Scanner = new Scanner(System.in);

        //Variablen
        String ActiveToken = "none";
        int Selection;

        //Code
        if(BOTSETTINGS.BOTSELECTION == 0) {
            System.out.print("Welchen Bot willst du ausführen?");
        }
        for(boolean loop1 = true; loop1;) {

            if(BOTSETTINGS.BOTSELECTION == 0) {
                System.out.print("\n1: offGNB\n2: devGNB\n3: GLaDOS\n");
                Selection = Scanner.nextInt();
            } else {
                Selection = BOTSETTINGS.BOTSELECTION;
            }
            switch (Selection) {

                case 1:
                    loop1 = false;
                    ActiveToken = TOPSECRET.TokenOFFGNB;
                    break;
                case 2:
                    loop1 = false;
                    ActiveToken = TOPSECRET.TokenDEVGNB;
                    break;
                case 3:
                    loop1 = false;
                    ActiveToken = TOPSECRET.TokenGLADOS;
                    break;
                default:
                    System.out.print(Spacer(100) + "Das steht leider nicht zur Auswahl");
                    Thread.sleep(1000);
                    System.out.print("\nProbiere es erneut");
            }

        }
        return ActiveToken;

    }



    private static String ays() throws InterruptedException {

        Scanner scanner = new Scanner(System.in);
        String input = "no String inserted (input)"; //Errormeldung
        String aysinput = "no String inserted (aysinput)"; //Errormeldung
        for (boolean loop1 = true; loop1; ) {

            input = scanner.nextLine();
            System.out.print(input + ", Bist du dir Sicher?");
            aysinput = scanner.nextLine();
            if (aysinput.equals("Y")) {

                loop1 = false;
                System.out.print("Okay");
                Thread.sleep(1000);

            } else {

                System.out.print("Okay, probiere es erneut: ");
                Thread.sleep(1000);

            }


        }
        return input;

    }



    public static String Spacer(int amount) {

        String Spacing = "";
        for(int loop1 = 0; loop1 < amount ;loop1 ++) {

            Spacing = Spacing + "\n";

        }
        return(Spacing);
    }

    public static String Stamp(String type, String author, String text) {

        LocalDateTime cT = LocalDateTime.now();
        return "[" + String.format("%02d", cT.getHour()) + ":" + String.format("%02d", cT.getMinute()) + ":" + String.format("%02d", cT.getSecond()) +
                "] [" + type.toUpperCase() + "] [" + author + "]: " + text;
    }

    public static String prefix(Guild guild) {
        try {
            String prefix = util.Shelf.read("Prefix" + guild.getId());
            if(prefix.equals("none")) {
                prefix = BOTSETTINGS.DEFAULTPREFIX;
            }
            return prefix;
        } catch (NullPointerException exception) {
            return BOTSETTINGS.DEFAULTPREFIX;
        }
    }

    public static TextChannel getDefaulChannel(Guild guild) {
        String defaultchannel = Shelf.read("DefaultChannel" + guild.getId());
        if(defaultchannel.equals("NoDefaulChannel") || defaultchannel.equals("none")) {
            return guild.getDefaultChannel();
        } else {
            return guild.getJDA().getTextChannelById(defaultchannel);
        }
    }

    public static TextChannel getMusicChannel(Guild guild) {
        String musicchannel = util.Shelf.read("MusicChannel" + guild.getId());
        if(musicchannel.equals("NoMusicChannel") || musicchannel.equals("none")) {
            return guild.getDefaultChannel();
        }else {
            return guild.getJDA().getTextChannelById(musicchannel);
        }

    }

    public static boolean MusicChannelIsForced(Guild guild) {
        String musicchannel = util.Shelf.read("MusicChannel" + guild.getId());
        if (musicchannel.equals("NoMusicChannel") || musicchannel.equals("none")) {
            return false;
        } else {
            return true;
        }
    }

    public static Role getDJRole(Guild guild) {
        String djrole = util.Shelf.read("DJRole" + guild.getId());
        if(djrole.equals("none")) {
            return null;
        } else {
            return guild.getRoleById(djrole);
        }
    }

    public static String getThumbnail(String raw) {
        return "https://img.youtube.com/vi/" + raw.substring(32, 43) + "/mqdefault.jpg";
    }

    public static boolean hasPermission(String level, String forwhat, MessageReceivedEvent event) {
        boolean hasPermissions = false;
        String mentions = "";
        String permissionlvl = "";
        switch (level) {

            case "DJR":
                permissionlvl = "DJ - :musical_note:";
                String djrole = Shelf.read("DJRole" + event.getMember().getGuild().getId());
                if(djrole.equals("none")) {
                    hasPermissions = true;
                } else if(event.getMember().getRoles().contains(event.getMember().getJDA().getRoleById(djrole))) {
                    hasPermissions = true;
                } else {
                    for(Member memberil : event.getMember().getGuild().getMembers()) {
                        if(memberil.getRoles().contains(event.getMember().getJDA().getRoleById(djrole))) {
                            mentions += memberil.getAsMention() + " ";
                        }
                    }
                }
                break;


            case "ADM":
                permissionlvl = ":blue_circle:";
                if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    hasPermissions = true;
                    Tools.Stamp("info", event.getJDA().getSelfUser().getName(), "\"" + event.getMember().getEffectiveName() + "\" in \"" + event.getGuild().getName() + "\" hat " + event.getMessage().getContent() + " benutzt.");
                } else {
                    for(Member memberil : event.getMember().getGuild().getMembers()) {
                        if(memberil.hasPermission(Permission.ADMINISTRATOR)) {
                            mentions += memberil.getAsMention() + " ";
                        }
                    }
                }
                break;


            case "BOT":
                permissionlvl = ":red_circle:";
                if(Arrays.asList(BOTSETTINGS.BOTOPERATORIDS).contains(event.getMember().getUser().getId())) {
                    hasPermissions = true;
                } else {
                    for(Member memberil : event.getMember().getGuild().getMembers()) {
                        if(Arrays.asList(BOTSETTINGS.BOTOPERATORIDS).contains(memberil.getUser().getId())) {
                            mentions += memberil.getAsMention() + " ";
                        }
                    }
                }
                break;
        }
        if(!hasPermissions) {
            event.getTextChannel().sendMessage(new EmbedBuilder()
                    .setColor(Color.orange)
                    .setTitle(":warning: - RECHTE")
                    .addField("Beschreibung", "Du hast nicht die benötigten Rechte um " + forwhat + "!\nBenötigt wird PermissionLVL " + permissionlvl + ".", false)
                    .addField("ServerUser die das Recht dazu haben", mentions, false)
                    .build()
            ).queue();
            Tools.Stamp("info", event.getJDA().getSelfUser().getName(), "\"" + event.getMember().getEffectiveName() + "\" in \"" + event.getGuild().getName() + "\" hat versucht " + event.getMessage().getContent() + " zu benutzen.");
        }
        return hasPermissions;
    }

    public static void InfoPanel(String title, Guild guild) {
        if(title.equals("paused")) {
            getMusicChannel(guild).getManager().setTopic("Die Wiedergabe ist pausiert").queue();
        } else if(title.equals("stoped")) {
            getMusicChannel(guild).getManager().setTopic("Aktuell wird kein Song gespielt").queue();
        } else {
            getMusicChannel(guild).getManager().setTopic("Aktueller Song: " + title).queue();
        }
    }
}
