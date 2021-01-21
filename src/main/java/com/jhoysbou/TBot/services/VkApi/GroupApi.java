package com.jhoysbou.TBot.services.VkApi;

import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.User;

import java.util.List;

public interface GroupApi {

    List<User> getAvailableUsers(final String accessToken, final short count, final long offset);

    void sendMessage(final Message message, List<User> peers);
}
