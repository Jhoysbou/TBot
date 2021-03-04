package com.jhoysbou.TBot.utils;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexOpenLinkExtractor implements OpenLinkExtractor {
    private static final Pattern linkPattern = Pattern.compile(
            "\\[https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)\\]"
    );

    @Override
    public Optional<String> extract(String text) {
        Matcher matcher = linkPattern.matcher(text);
        Optional<String> link = Optional.empty();

        if (matcher.find()) {
            var match = matcher.group();
            link = Optional.of(match.replace("[", "").replace("]", ""));
        }

        return link;
    }
}
