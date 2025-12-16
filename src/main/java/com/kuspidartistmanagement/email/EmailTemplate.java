package com.kuspidartistmanagement.email;

import com.kuspidartistmanagement.domain.entity.Artist;
import com.kuspidartistmanagement.domain.entity.Beat;
import com.kuspidartistmanagement.domain.entity.Pack;

import java.util.List;
import java.util.stream.Collectors;

public final class EmailTemplate {
    private EmailTemplate() {
        // Prevent instantiation
    }

    public static String buildBeatEmailBody(Artist artist, List<Beat> beats, String customMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append(String.format("<h2>Hi %s,</h2>", artist.getName()));

        if (customMessage != null && !customMessage.isBlank()) {
            sb.append(String.format("<p>%s</p>", customMessage));
        } else {
            sb.append("<p>We have some fresh beats that we think you'll love!</p>");
        }

        sb.append("<h3>Beats:</h3>");
        sb.append("<ul>");
        for (Beat beat : beats) {
            sb.append(String.format("<li><strong>%s</strong>", beat.getTitle()));
            if (beat.getBpm() != null) {
                sb.append(String.format(" - %d BPM", beat.getBpm()));
            }
            if (beat.getGenre() != null) {
                sb.append(String.format(" - %s", beat.getGenre()));
            }
            sb.append("</li>");
        }
        sb.append("</ul>");

        sb.append("<p>Let us know what you think!</p>");
        sb.append("<p>Best regards,<br/>The Team</p>");
        sb.append("</body></html>");

        return sb.toString();
    }

    public static String buildPackEmailBody(Artist artist, Pack pack, String customMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append(String.format("<h2>Hi %s,</h2>", artist.getName()));

        if (customMessage != null && !customMessage.isBlank()) {
            sb.append(String.format("<p>%s</p>", customMessage));
        } else {
            sb.append("<p>We have a beat pack that we think you'll love!</p>");
        }

        sb.append(String.format("<h3>Pack: %s</h3>", pack.getName()));
        if (pack.getDescription() != null) {
            sb.append(String.format("<p>%s</p>", pack.getDescription()));
        }

        sb.append(String.format("<p>This pack contains %d beats.</p>", pack.getBeatCount()));

        sb.append("<p>Let us know what you think!</p>");
        sb.append("<p>Best regards,<br/>The Team</p>");
        sb.append("</body></html>");

        return sb.toString();
    }

    public static String buildDefaultSubject(boolean isPack) {
        return isPack ? "New Beat Pack Available" : "New Beats Available";
    }
}