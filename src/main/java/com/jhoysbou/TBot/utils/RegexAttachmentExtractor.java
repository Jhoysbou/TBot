package com.jhoysbou.TBot.utils;

import com.jhoysbou.TBot.models.Attachment;
import com.jhoysbou.TBot.models.MenuAttachmentsDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class RegexAttachmentExtractor implements AttachmentExtractor {
    private final Pattern urlPattern = Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)");
    private final Pattern pattern = Pattern.compile("((photo)|(video)|(audio)|(doc)|(wall)|(market)|(poll))-*\\d+_\\d+");

    @Override
    public MenuAttachmentsDto parse(String text) {
        Matcher matcher = urlPattern.matcher(text);
        List<String> allMatches = new ArrayList<>();

        while (matcher.find()) {
            var link = matcher.group();
            var m = pattern.matcher(link);
            if (m.find()) {
                allMatches.add(m.group());
                text = text.replace(link, "");
            }
        }

        return new MenuAttachmentsDto(
                allMatches.stream()
                        .map(str -> {
                            var type = str.replaceAll("[^a-zA-Z]", "");
                            var splitted = str.split("_");
                            var ownerId = splitted[0].replace(type, "");
                            var mediaId = splitted[1];
                            return new Attachment(type, ownerId, mediaId);
                        })
                        .collect(Collectors.toList()),
                text
                );
    }


}
