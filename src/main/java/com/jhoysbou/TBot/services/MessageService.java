package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.models.vkmodels.NewMessageWrapper;

public interface MessageService {
    void handleMessage(GroupEventDAO<NewMessageWrapper> event);

}
