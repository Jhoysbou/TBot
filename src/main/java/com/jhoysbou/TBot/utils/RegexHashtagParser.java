package com.jhoysbou.TBot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class RegexHashtagParser implements HashtagParser {
    private final Pattern hashtagPattern = Pattern.compile("\\B(\\#[a-zA-Zа-яА-Я]+\\b)(?!;)");
    private final String importantNotificationTag;

    public RegexHashtagParser(@Value("${notification.important.tag}") String importantNotificationTag) {
        this.importantNotificationTag = importantNotificationTag;
    }


    @Override
    public Optional<String> parse(Optional<String> text) {
        if (text.isPresent()) {
            var t = text.get();

            if (t.contains(importantNotificationTag)) {
                return Optional.of(importantNotificationTag);
            }

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
