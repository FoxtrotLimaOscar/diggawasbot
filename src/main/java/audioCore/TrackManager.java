package audioCore;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import core.Tools;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import sun.rmi.runtime.Log;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackManager extends AudioEventAdapter{

    private final AudioPlayer PLAYER;
    private final Queue<AudioInfo> queue;

    public TrackManager(AudioPlayer player) {
        this.PLAYER = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track, Member author) {
        AudioInfo info = new AudioInfo(track, author);
        queue.add(info);
        if(PLAYER.getPlayingTrack() == null) {
            PLAYER.playTrack(track);
        }
    }

    public Set<AudioInfo> getQueue() {
        return new LinkedHashSet<>(queue);
    }

    public AudioInfo getInfo(AudioTrack track) {
        return queue.stream()
                .filter(info -> info.getTrack().equals(track))
                .findFirst().orElse(null);
    }

    public void purgeQueue() {
        queue.clear();
    }

    public void shuffleQueue() {
        List<AudioInfo> cQueue = new ArrayList<>(getQueue());
        AudioInfo current = cQueue.get(0);
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        purgeQueue();
        queue.addAll(cQueue);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioInfo info = queue.element();
        Guild guild = info.getAuthor().getGuild();
        VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();
        if(vChan == null) {
            player.stopTrack();
        } else {
            info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);
            Tools.getMusicChannel(guild).sendMessage(new EmbedBuilder()
                    .setTitle(":musical_note: - MUSIC")
                    .setColor(Color.blue)
                    .setDescription(info.getTrack().getInfo().title + " wird nun abgespielt")
                    .setThumbnail(Tools.getThumbnail(info.getTrack().getInfo().uri))
                    .build()
            ).queue();
            Tools.InfoPanel(info.getTrack().getInfo().title, guild);
        }
    }

    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        try {
            AudioInfo info = queue.element();
            Guild g = queue.poll().getAuthor().getGuild();
            if (queue.isEmpty()) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        g.getAudioManager().closeAudioConnection();
                    }
                }, 0);
                Tools.InfoPanel("stoped", g);
            } else {
                player.playTrack(queue.element().getTrack());
            }
        } catch (Exception e) {
            //Hier steht noch nichts
        }
    }
}
