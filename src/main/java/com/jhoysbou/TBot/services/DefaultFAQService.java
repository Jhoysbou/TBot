package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.models.vkmodels.NewMessageWrapper;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultFAQService implements FAQService {
    private final GroupApi api;

    @Autowired
    public DefaultFAQService(GroupApi api) {
        this.api = api;
    }

    @Override
    public void handleMessage(GroupEventDAO<NewMessageWrapper> event) {

    }
}
