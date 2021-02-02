package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.models.vkmodels.NewMessageWrapper;

import java.io.IOException;

public interface FAQService {
    void handleMessage(GroupEventDAO<NewMessageWrapper> event);

    String getConfirmationCode() throws IOException, InterruptedException;

}
