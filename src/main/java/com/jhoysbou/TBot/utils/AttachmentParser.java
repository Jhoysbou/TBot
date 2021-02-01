package com.jhoysbou.TBot.utils;

import com.jhoysbou.TBot.models.Attachment;

import java.util.List;

public interface AttachmentParser {

    List<Attachment> parse(final String text);
}
