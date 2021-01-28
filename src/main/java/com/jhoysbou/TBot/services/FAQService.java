package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.MessageResponse;
import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.models.vkmodels.NewMessageWrapper;

public interface FAQService {
    void handleMessage(GroupEventDAO<NewMessageWrapper> event);

}
