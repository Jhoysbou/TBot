package com.jhoysbou.TBot.utils;

import com.jhoysbou.TBot.models.Attachment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class SimpleAttachmentParser implements AttachmentParser {
    private final Pattern pattern = Pattern.compile("((photo)|(video)|(audio)|(doc)|(wall)|(market)|(poll))-*\\d+_\\d+");

    @Override
    public List<Attachment> parse(String text) {
        Matcher matcher = pattern.matcher(text);
        List<String> allMatches = new ArrayList<String>();

        while (matcher.find()) {
            allMatches.add(matcher.group());
        }

        return allMatches.stream()
                .map(str -> {
                    var type = str.replace("[^a-zA-Z]", "");
                    var splitted = str.split("_");
                    var ownerId = splitted[0].replace(type, "");
                    var mediaId = splitted[1];
                    return new Attachment(type, ownerId, mediaId);
                })
                .collect(Collectors.toList());
    }
}
