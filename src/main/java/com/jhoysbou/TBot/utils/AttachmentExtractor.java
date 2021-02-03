package com.jhoysbou.TBot.utils;

import com.jhoysbou.TBot.models.MenuAttachmentsDto;


public interface AttachmentExtractor {

    MenuAttachmentsDto parse(final String text);


}
