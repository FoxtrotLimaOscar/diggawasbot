package commands;

import core.Tools;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

public class cmdSpeedTest implements Command{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if(Tools.hasPermission("BOT", "einen Speedtest auszuführen", event)) {

            SpeedTestSocket Dspeed = new SpeedTestSocket();
            SpeedTestSocket Uspeed = new SpeedTestSocket();
            StringBuilder uploadout = new StringBuilder();
            StringBuilder downloadout = new StringBuilder();
            Message msg = event.getChannel().sendMessage(new EmbedBuilder()
                    .setColor(Color.orange)
                    .setTitle(":wrench: - SPEEDTEST")
                    .addField("Download", "Wird getestet...", true)
                    .addField("Upload", "Wartet", true)
                    .build()
            ).complete();

            Dspeed.addSpeedTestListener(new ISpeedTestListener() {
                @Override
                public void onCompletion(SpeedTestReport report) {
                    downloadout.append(Math.round(report.getTransferRateBit().floatValue() / 1024 / 1024) + " MBit/s");
                    msg.editMessage(new EmbedBuilder()
                            .setColor(Color.orange)
                            .setTitle(":wrench: - SPEEDTEST")
                            .addField("Download", downloadout.toString(), true)
                            .addField("Upload", "Wird getestet...", true)
                            .build()
                    ).queue();
                    Uspeed.startUpload("http://2.testdebit.info/", 1000000);
                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                }

                @Override
                public void onError(SpeedTestError speedTestError, String s) {
                    System.out.println(speedTestError);
                }

            });

            Uspeed.addSpeedTestListener(new ISpeedTestListener() {
                @Override
                public void onCompletion(SpeedTestReport report) {
                    uploadout.append(Math.round(report.getTransferRateBit().floatValue() / 1024 / 1024) + " MBit/s");
                    msg.editMessage(new EmbedBuilder()
                            .setColor(Color.orange)
                            .setTitle(":wrench: - SPEEDTEST")
                            .addField("Download", downloadout.toString(), true)
                            .addField("Upload", uploadout.toString(), true)
                            .build()
                    ).queue();
                }

                @Override
                public void onProgress(float v, SpeedTestReport speedTestReport) {

                }

                @Override
                public void onError(SpeedTestError speedTestError, String s) {
                    System.out.println(speedTestError);
                }

            });

            Dspeed.startDownload("http://2.testdebit.info/10M.iso");

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
