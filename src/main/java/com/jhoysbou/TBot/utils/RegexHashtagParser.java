package com.jhoysbou.TBot.utils;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class RegexHashtagParser implements HashtagParser {
    private final Pattern hashtagPattern = Pattern.compile("\\B(\\#[a-zA-Zа-яА-Я]+\\b)(?!;)");

    @Override
    public Optional<String> parse(Optional<String> text) {
        if (text.isPresent()) {
            var t = text.get();
            if (t.length() > 1) {
                var matcher = hashtagPattern.matcher(t);
                if (matcher.find()) {
                    return Optional.of(matcher.group());
                }
            }
        }

        return Optional.empty();
    }
}
