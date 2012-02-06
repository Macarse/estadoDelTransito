package com.appspot.estadodeltransito.parsers;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appspot.estadodeltransito.domain.train.Train;

public class TrainsParser {
    public static final String trainsURL = "http://servicios.lanacion.com.ar/informacion-general/transito";
    private static final Pattern groupPattern = Pattern.compile("<div[^>]*>\\s*<img[^>]*>\\s*TRENES[^<]*<[^>]*>\\s*<div[^>]*>(.*?)<div[^>]*class=\"infoTransito\"[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern trainPattern = Pattern.compile("<b>((?!L.nea)[^<]*)\\s*\\((?:TBA-?)?\\s*([^\\)]+)\\)</b>\\s*<[^>]*><b>[^<]*<[^>]*>[^<]*<span[^>]*>\\s*\\|\\s*</span>\\s*[^<]*?\\s*<span>\\s*Estado\\s*:\\s*</span[^>]*>\\s*<b>\\s*<font[^>]*>([^<]*)(?:</font>\\s*</b>\\s*<br>\\s*<span>[^<]*</span>\\s*<b>(.*?)<br>)?", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static String getUrlContent() {
        String urlContent = null;
        try {
            urlContent = ParserUtils.getUrlContent(trainsURL);
        } catch (Exception e) {
            return null;
        }

        return urlContent;
    }

    public static LinkedList<Train> getTrains(String html) {
        if ( html == null || html.length() == 0 ) {
            return null;
        }

        LinkedList<Train> ret = new LinkedList<Train>();

        Matcher groupMatcher = groupPattern.matcher(html);
        if ( groupMatcher.find() ) {
            Matcher trainMatcher = trainPattern.matcher(groupMatcher.group(1));
            while ( trainMatcher.find() ) {
                Train train = new Train();
                
                train.setName(trainMatcher.group(1));
                train.setLine(trainMatcher.group(2).replaceAll("\\.", "").replaceAll("Roca", "Gral Roca").replaceAll("Gral Gral", "Gral"));
                train.setStatus(trainMatcher.group(3));

                if(trainMatcher.group(4) != null) {
                    train.setStatus(train.getStatus() + " " + trainMatcher.group(4).replaceAll("<[^>]*>", "").replaceAll("\\s+", " "));
                }

                ret.add(train);
            }
        }

        return ret;
    }
}
