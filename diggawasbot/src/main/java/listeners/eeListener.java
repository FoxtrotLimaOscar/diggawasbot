package listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class eeListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            String content = event.getMessage().getContent().toLowerCase();



            if(content.contains("the cake")) {
                event.getTextChannel().sendMessage("...is a lie!").queue();
            }

            if(content.contains("keks")) {
                event.getTextChannel().sendMessage(":cookie: KEKKSE").queue();
            }

            if(content.contains("soldat")) {
                event.getTextChannel().sendMessage("AYE! SIR!").queue();
            }

            if(content.contains("gamingnation")) {
                event.getTextChannel().sendMessage("DASCH 1 NICE SERVER!").queue();
            }

            if(content.contains("bug")) {
                event.getTextChannel().sendMessage("Its not a bug...\nIts a feature!").queue();
            }

            if(content.contains("flo")) {
                event.getTextChannel().sendMessage("Der Flo der is schon 1 krasser Tüpp").queue();
            }

            if(content.contains("jonas")) {
                event.getTextChannel().sendMessage("Hab auch schon was von ihm gehört.\nMan sagt er tötet Eulen...").queue();
            }

            if(content.contains("m&m") || content.contains("mnm")) {
                event.getTextChannel().sendMessage("Wenn M&M hier sind kann " + event.getJDA().getUserById("265921736225193985").getAsMention() + " auch nciht weit sein!").queue();
            }

            if(content.contains("maven")) {
                event.getTextChannel().sendMessage("Jonas wollte dass hier was steht, ok... das tut es.").queue();
            }



        }
    }
}
