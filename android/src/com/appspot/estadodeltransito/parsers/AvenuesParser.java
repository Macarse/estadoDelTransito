package com.appspot.estadodeltransito.parsers;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appspot.estadodeltransito.domain.highway.Highway;
import com.appspot.estadodeltransito.domain.highway.Highway.Text;

public class AvenuesParser {
    private static final Pattern groupPattern = Pattern.compile("avenidas\\s*de\\s*capital\\s*federal\\s*</div>(.*?)<div[^>]*id=\"tercera\"[^>]*>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern accessGroupPattern = Pattern.compile("<div[^>]*class=\"itemAutos\"[^>]*>(.*?)(?=<div[^>]*class=\"itemAutos\"[^>]*>|$)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern accessNamePattern = Pattern.compile("<div[^>]*+>\\s*<b[^>]*+>\\s*([^<]++)</b>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern accessDirectionPattern = Pattern.compile("<div[^>]*class=\"[^\"]*transitoDescripcion[^\"]*+\"[^>]*+>\\s*([^<:]+)[^<]*<b[^>]*>\\s*([^<]++)(?:<font[^>]*>([^<]*)</font>)?\\s*</b>\\s*(?:<span[^>]*>\\s*<img[^>]*>\\s*<span[^>]*>\\s*([^<]++))?", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    
    public static final String avenuesURL = "http://servicios.lanacion.com.ar/informacion-general/transito";

    public static String getUrlContent() {
        String urlContent = null;
        try {
            urlContent = ParserUtils.getUrlContent(avenuesURL);
        } catch (Exception e) {
            return null;
        }

        return urlContent;
    }

    public static LinkedList<Highway> getHighways(String html) {
        if ( html == null || html.length() == 0 ) {
            return null;
        }

        LinkedList<Highway> ret = new LinkedList<Highway>();

        Matcher groupMatcher = groupPattern.matcher(html);

        if ( groupMatcher.find() ) {
            String groups = groupMatcher.group(1);
            Matcher itemMatcher = accessGroupPattern.matcher(groups);

            while ( itemMatcher.find() ) {
                String item = itemMatcher.group(1);
                Highway highway = new Highway();

                Matcher accessNameMatcher = accessNamePattern.matcher(item);
                if ( accessNameMatcher.find() ) {
                    highway.setName(accessNameMatcher.group(1));
                }

                Matcher accessDirectionMatcher = accessDirectionPattern.matcher(item);
                if ( accessDirectionMatcher.find() ) {
                    String groupText;

                    groupText = accessDirectionMatcher.group(1);
                    if ( null != groupText ) {
                        highway.setDirectionFrom(groupText.trim());
                    }

                    groupText = accessDirectionMatcher.group(2);
                    if ( null != groupText ) {
                        highway.setStatusFrom(groupText.trim());
                    }

                    groupText = accessDirectionMatcher.group(3);
                    if ( null != groupText ) {
                        highway.setDelayFrom(groupText.trim());
                    }

                    groupText = accessDirectionMatcher.group(4);
                    if ( null != groupText ) {
                        Text text = new Text();
                        text.setValue(groupText.trim());
                        highway.setStatusMessageFrom(text);
                    }

                }

                if ( accessDirectionMatcher.find() ) {
                    String groupText;

                    groupText = accessDirectionMatcher.group(1);
                    if ( null != groupText ) {
                        highway.setDirectionTo(groupText.trim());
                    }

                    groupText = accessDirectionMatcher.group(2);
                    if ( null != groupText ) {
                        highway.setStatusTo(groupText.trim());
                    }

                    groupText = accessDirectionMatcher.group(3);
                    if ( null != groupText ) {
                        highway.setDelayTo(groupText.trim());
                    }

                    groupText = accessDirectionMatcher.group(4);
                    if ( null != groupText ) {
                        Text text = new Text();
                        text.setValue(groupText.trim());
                        highway.setStatusMessageTo(text);
                    }
                }

                ret.add(highway);
            }
        }

        return ret;
    }

}
