package com.jhoysbou.TBot.utils;

import com.jhoysbou.TBot.models.MenuAttachmentsDto;

import java.util.List;

public interface AttachmentExtractor {

    MenuAttachmentsDto parse(final String text);


}
