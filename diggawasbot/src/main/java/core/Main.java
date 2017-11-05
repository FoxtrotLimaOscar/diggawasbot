package core;

import commands.*;
import listeners.commandListener;
import listeners.eeListener;
import listeners.readyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import util.Shelf;

import javax.security.auth.login.LoginException;

public class Main {

    public static JDABuilder builder;

    public static void main(String[] arguments) throws InterruptedException {

            //Übersetzungen
        builder = new JDABuilder(AccountType.BOT);

            //Code
        builder.setToken(Tools.setActiveToken());
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setGame(Game.of(Shelf.read("Game")));
        addListeners();
        addCommands();

            //Versteh ich nicht
        try {
            JDA jda = builder.buildBlocking();
        } catch (LoginException | RateLimitedException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void addCommands() {

            //Standart
        commandHandler.commands.put("ping", new cmdPing());
        commandHandler.commands.put("version", new cmdVersion());
        commandHandler.commands.put("server", new cmdServer());
        commandHandler.commands.put("info", new cmdInfo());
        commandHandler.commands.put("help", new cmdHelp());
        commandHandler.commands.put("flipacoin", new cmdFlipACoin());
        commandHandler.commands.put("speedtest", new cmdSpeedTest());


            //Musik
        commandHandler.commands.put("play", new Music());
        commandHandler.commands.put("search", new Music());
        commandHandler.commands.put("np", new Music());
        commandHandler.commands.put("pause", new Music());
        commandHandler.commands.put("resume", new Music());
        commandHandler.commands.put("skip", new Music());
        commandHandler.commands.put("stop", new Music());
        commandHandler.commands.put("queue", new Music());
        commandHandler.commands.put("shuffle", new Music());
        commandHandler.commands.put("favsong", new Music());

            //HighPermission
        commandHandler.commands.put("musicchannel", new cmdMusicChannel());
        commandHandler.commands.put("shutdown", new cmdShutdown());
        commandHandler.commands.put("dj", new cmdDJ());
        commandHandler.commands.put("prefix", new cmdPrefix());
        commandHandler.commands.put("status", new cmdStatus());
        commandHandler.commands.put("settings", new cmdSettings());

    }

    public static void addListeners() {

        builder.addEventListener(new readyListener());
        builder.addEventListener(new commandListener());
        builder.addEventListener(new eeListener());

    }

}
