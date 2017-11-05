package commands;

import audioCore.AudioInfo;
import audioCore.PlayerSendHandler;
import audioCore.TrackManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import core.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.BOTSETTINGS;
import util.Shelf;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by zekro on 18.06.2017 / 11:47
 * supremeBot.commands
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Music implements Command{

    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();
    private static Guild guild;
    private int Timercount = 0;


    /**
     * Audio Manager als Audio-Stream-Recource deklarieren.
     */
    public Music() {
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }

    /**
     * Erstellt einen Audioplayer und fügt diesen in die PLAYERS-Map ein.
     * @param g Guild
     * @return AudioPlayer
     */
    private AudioPlayer createPlayer(Guild g) {
        AudioPlayer p = MANAGER.createPlayer();
        TrackManager m = new TrackManager(p);
        p.addListener(m);

        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(p));

        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(p, m));

        return p;
    }

    /**
     * Returnt, ob die Guild einen Eintrag in der PLAYERS-Map hat.
     * @param g Guild
     * @return Boolean
     */
    private boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }

    /**
     * Returnt den momentanen Player der Guild aus der PLAYERS-Map,
     * oder erstellt einen neuen Player für die Guild.
     * @param g Guild
     * @return AudioPlayer
     */
    private AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g))
            return PLAYERS.get(g).getKey();
        else
            return createPlayer(g);
    }

    /**
     * Returnt den momentanen TrackManager der Guild aus der PLAYERS-Map.
     * @param g Guild
     * @return TrackManager
     */
    private TrackManager getManager(Guild g) {
        return PLAYERS.get(g).getValue();
    }

    /**
     * Returnt, ob die Guild einen Player hat oder ob der momentane Player
     * gerade einen Track spielt.
     * @param g Guild
     * @return Boolean
     */
    private boolean isIdle(Guild g) {
        return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
    }

    /**
     * Läd aus der URL oder dem Search String einen Track oder eine Playlist
     * in die Queue.
     * @param identifier URL oder Search String
     * @param event dass den Track eingereiht hat
     */
    private void loadTrack(String identifier, MessageReceivedEvent event) {

        Guild guild = event.getGuild();
        getPlayer(guild);

        MANAGER.setFrameBufferDuration(5000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, event.getMember());
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    if(!getManager(guild).getQueue().isEmpty()) {
                        event.getTextChannel().sendMessage(new EmbedBuilder()
                                .setTitle(":musical_note: - MUSIC")
                                .setColor(Color.blue)
                                .setDescription(playlist.getTracks().get(0).getInfo().title + " wurde der queue hinzugefügt")
                                .setThumbnail(Tools.getThumbnail(playlist.getTracks().get(0).getInfo().uri))
                                .build()
                        ).queue();
                    }
                    trackLoaded(playlist.getTracks().get(0));
                } else {
                    for (int i = 0; i < Math.min(playlist.getTracks().size(), BOTSETTINGS.YTPLAYLISTLIMIT); i++) {
                        getManager(guild).queue(playlist.getTracks().get(i), event.getMember());
                    }
                    if(playlist.getTracks().size() == 1) {
                        event.getTextChannel().sendMessage(new EmbedBuilder()
                                .setTitle(":musical_note: - MUSIC")
                                .setColor(Color.blue)
                                .setDescription("Die Playlist \"" + playlist.getName() + "\" wurde der queue hinzugefügt")
                                .setThumbnail(Tools.getThumbnail(playlist.getTracks().get(0).getInfo().uri))
                                .build()
                        ).queue();
                    } else if(!getManager(guild).getQueue().isEmpty()){
                        event.getTextChannel().sendMessage(new EmbedBuilder()
                                .setTitle(":musical_note: - MUSIC")
                                .setColor(Color.blue)
                                .setDescription(playlist.getTracks().get(0).getInfo().title + " wurde der queue hinzugefügt")
                                .setThumbnail(Tools.getThumbnail(playlist.getTracks().get(0).getInfo().uri))
                                .build()
                        ).queue();
                    }
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });

    }

    private void searchTrack(String identifier, MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        getPlayer(guild);

        MANAGER.setFrameBufferDuration(5000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, event.getMember());
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                Message message = event.getTextChannel().sendMessage(new EmbedBuilder()
                        .setColor(Color.blue)
                        .setTitle(":musical_note: - MUSIC")
                        .addField(":one: - Ergebnis", playlist.getTracks().get(0).getInfo().title, false)
                        .addField(":two: - Ergebnis", playlist.getTracks().get(1).getInfo().title, false)
                        .addField(":three: - Ergebnis", playlist.getTracks().get(2).getInfo().title, false)
                        .addField(":four: - Ergebnis", playlist.getTracks().get(3).getInfo().title, false)
                        .addField(":five: - Ergebnis", playlist.getTracks().get(4).getInfo().title, false)
                        .build()
                ).complete();
                message.addReaction("\u0031\u20E3").queue();
                message.addReaction("\u0032\u20E3").queue();
                message.addReaction("\u0033\u20E3").queue();
                message.addReaction("\u0034\u20E3").queue();
                message.addReaction("\u0035\u20E3").queue();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            Timercount++;
                            for(int i = 0; i < 5; i++) {
                                if(message.getTextChannel().getMessageById(message.getId()).complete().getReactions().get(i).getCount() == 2) {
                                    message.delete().queue();
                                    if(!getManager(guild).getQueue().isEmpty()) {
                                        event.getTextChannel().sendMessage(new EmbedBuilder()
                                                .setTitle(":musical_note: - MUSIC")
                                                .setColor(Color.blue)
                                                .setDescription(playlist.getTracks().get(0).getInfo().title + " wurde der queue hinzugefügt")
                                                .setThumbnail(Tools.getThumbnail(playlist.getTracks().get(i).getInfo().uri))
                                                .build()
                                        ).queue();
                                    }
                                    getManager(guild).queue(playlist.getTracks().get(i), event.getMember());
                                    this.cancel();
                                }
                            }
                            if(Timercount == 60) {
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

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Stoppt den momentanen Track, worauf der nächste Track gespielt wird.
     * @param g Guild
     */
    private void skip(Guild g) {
        getPlayer(g).stopTrack();
    }

    /**
     * Erzeugt aus dem Timestamp in Millisekunden ein hh:mm:ss - Zeitformat.
     * @param milis Timestamp
     * @return Zeitformat
     */
    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    /**
     * Returnt aus der AudioInfo eines Tracks die Informationen als String.
     * @param info AudioInfo
     * @return Informationen als String
     */
    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[" + getTimestamp(length) + "]` " + title + "\n";
    }

    /**
     * Sendet eine Embed-Message in der Farbe Rot mit eingegebenen Content.
     * @param event MessageReceivedEvent
     * @param content Error Message Content
     */
    private void sendErrorMsg(MessageReceivedEvent event, String content) {
        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setTitle(":warning: - ERROR")
                        .setColor(Color.orange)
                        .setDescription(content)
                        .build()
        ).queue();
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {


        guild = event.getGuild();

        AudioTrack track;
        AudioTrackInfo info;
        if(!Tools.MusicChannelIsForced(event.getGuild()) || event.getTextChannel().equals(Tools.getMusicChannel(event.getGuild()))) {
            switch (invoke.toLowerCase()) {

                case "play":
                    try {
                        if(getPlayer(guild).isPaused()) {
                            getPlayer(guild).setPaused(false);
                            event.getTextChannel().sendMessage(
                                    new EmbedBuilder()
                                            .setTitle(":musical_note: - MUSIC")
                                            .setColor(Color.blue)
                                            .setDescription("Die Wiedergabe wurde fortgesetzt")
                                            .build()
                            ).queue();
                            Tools.InfoPanel(getPlayer(guild).getPlayingTrack().getInfo().title, guild);
                        } else if(!event.getMember().getVoiceState().inVoiceChannel()) {
                            event.getTextChannel().sendMessage(new EmbedBuilder()
                                    .setColor(Color.orange)
                                    .setTitle(":warning: - " + Tools.prefix(event.getGuild()) + "Music")
                                    .setDescription("Du musst in einem VoiceChannel sein um Musik abzuspielen")
                                    .build()
                            ).queue();
                        } else {
                            String input = Arrays.stream(args).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                            if(input.equals("favsong")) {
                                String favsong = Shelf.read("FavSong" + event.getAuthor().getId());
                                if(favsong.equals("none")) {
                                    event.getTextChannel().sendMessage(new EmbedBuilder()
                                            .setColor(Color.orange)
                                            .setTitle(":warning: - MUSIC")
                                            .setDescription("Du musst zuerst einen favsong mit " + Tools.prefix(event.getGuild()) + "favsong festgelegt haben")
                                            .build()
                                    ).queue();
                                } else {
                                    input = favsong;
                                }
                            }
                            if (!(input.startsWith("http://") || input.startsWith("https://")))
                                input = "ytsearch: " + input;

                            loadTrack(input, event);
                        }

                    } catch (IndexOutOfBoundsException e) {
                        event.getTextChannel().sendMessage(new EmbedBuilder()
                                .setColor(Color.blue)
                                .setTitle(":information_source: - " + Tools.prefix(event.getGuild()) + "PLAY")
                                .addField("Info", "Fügt einen Song der Queue hinzu entweder mit einem YouTubeLink oder mit einem Songnamen.", false)
                                .addField("Benutzung", Tools.prefix(event.getGuild()) + "play <Link / Songname>", false)
                                .build()
                        ).queue();
                    }
                    break;


                case "search":
                    if(!event.getMember().getVoiceState().inVoiceChannel()) {
                        event.getTextChannel().sendMessage(new EmbedBuilder()
                                .setColor(Color.orange)
                                .setTitle(":warning: - " + Tools.prefix(event.getGuild()) + "Music")
                                .setDescription("Du musst in einem VoiceChannel sein um Musik abzuspielen")
                                .build()
                        ).queue();
                    } else {
                        String input = Arrays.stream(args).map(s -> " " + s).collect(Collectors.joining()).substring(1);
                        input = "ytsearch: " + input;
                        searchTrack(input, event);
                    }
                    break;


                case "skip":
                    if (isIdle(guild)) return;
                    if(Tools.hasPermission("DJR", "um einen Song zu skippen", event)) {
                        for (int i = (args.length > 1 ? Integer.parseInt(args[1]) : 1); i == 1; i--) {
                            skip(guild);
                        }
                    }
                    break;


                case "pause":
                    getPlayer(guild).setPaused(true);
                    event.getTextChannel().sendMessage(
                            new EmbedBuilder()
                                    .setTitle(":musical_note: - MUSIC")
                                    .setColor(Color.blue)
                                    .setDescription("Die Wiedergabe wurde pausiert")
                                    .build()
                    ).queue();
                    Tools.InfoPanel("paused", event.getGuild());
                    break;


                case "resume":
                    getPlayer(guild).setPaused(false);
                    event.getTextChannel().sendMessage(
                            new EmbedBuilder()
                                    .setTitle(":musical_note: - MUSIC")
                                    .setColor(Color.blue)
                                    .setDescription("Die Wiedergabe wurde fortgesetzt")
                                    .build()
                    ).queue();
                    break;


                case "stop":
                    if (isIdle(guild)) return;
                    if(Tools.hasPermission("DJR", "um einen Song zu stoppen", event)) {
                        getManager(guild).purgeQueue();
                        skip(guild);
                        guild.getAudioManager().closeAudioConnection();
                        event.getTextChannel().sendMessage(
                                new EmbedBuilder()
                                        .setTitle(":musical_note: - MUSIC")
                                        .setColor(Color.blue)
                                        .setDescription("Die Wiedergabe wurde gestoppt")
                                        .build()
                        ).queue();
                    }
                    break;


                case "shuffle":
                    if (isIdle(guild)) return;
                    getManager(guild).shuffleQueue();
                    event.getTextChannel().sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.blue)
                                    .setTitle(":musical_note: - SONG")
                                    .setDescription("Die Playlist wurde erfolgreich geshuffled!")
                                    .build()
                    ).queue();
                    break;


                case "np":
                    if (isIdle(guild)) return;

                    track = getPlayer(guild).getPlayingTrack();
                    info = track.getInfo();

                    event.getTextChannel().sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.blue)
                                    .setTitle(":musical_note: - SONG")
                                    .setThumbnail(Tools.getThumbnail(track.getInfo().uri))
                                    .addField("Titel", info.title, true)
                                    .addField("Dauer", getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()), true)
                                    .build()
                    ).queue();
                    break;


                case "queue":

                    if (isIdle(guild)) return;

                    int sideNumb = args.length > 1 ? Integer.parseInt(args[0]) : 1;

                    List<String> tracks = new ArrayList<>();
                    List<String> trackSublist;

                    getManager(guild).getQueue().forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

                    if (tracks.size() > 20)
                        trackSublist = tracks.subList((sideNumb-1)*20, (sideNumb-1)*20+20);
                    else
                        trackSublist = tracks;

                    String out = trackSublist.stream().collect(Collectors.joining(""));
                    int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;

                    event.getTextChannel().sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.blue)
                                    .setTitle(":musical_note: - QUEUE")
                                    .setDescription(out)
                                    .setFooter("Songs | Seite " + sideNumb + " / " + sideNumbAll, event.getJDA().getSelfUser().getAvatarUrl())
                                    .build()
                    ).queue();
                    break;


                case "favsong":
                    try {
                        String input = Arrays.stream(args).map(s -> " " + s).collect(Collectors.joining()).substring(1);
                        Shelf.write("FavSong" + event.getAuthor().getId(), input);
                        event.getTextChannel().sendMessage(new EmbedBuilder()
                                .setColor(Color.blue)
                                .setTitle(":musical_note: - FAVSONG")
                                .setDescription(input + " ist dein neuer FavSong")
                                .build()
                        ).queue();
                    } catch (StringIndexOutOfBoundsException exception) {
                        String favsong =  "Dein aktueller FavSong ist: " + Shelf.read("FavSong" + event.getAuthor().getId());
                        if(favsong.equals("none")) favsong = "Du hast noch keinen FavSong festgelegt.";
                        event.getTextChannel().sendMessage(new EmbedBuilder()
                                .setColor(Color.blue)
                                .setTitle(":information_source: - " + Tools.prefix(event.getGuild()) + "FAVSONG")
                                .addField("Info", "Legt einen LieblingsSong fest der mit play " + Tools.prefix(event.getGuild()) + "favsong abgespielt werden kann.\n" + favsong, false)
                                .addField("Benutzung", Tools.prefix(event.getGuild()) + "favsong <Link / Songname>", false)
                                .build()
                        ).queue();
                    }
                    break;
            }


        } else {
            event.getTextChannel().sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.orange)
                            .setTitle(":warning: - " + Tools.prefix(event.getGuild()) + "MUSIC")
                            .setDescription("Bitte schreibe Musiccommands in " + Tools.getMusicChannel(event.getGuild()).getAsMention())
                            .build()
            ).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
        }
    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "Doyouseethis?";
    }
}