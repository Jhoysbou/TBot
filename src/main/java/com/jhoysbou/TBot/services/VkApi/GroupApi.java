package com.jhoysbou.TBot.services.VkApi;

import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.UserDAO;

import java.io.IOException;
import java.util.List;

public interface GroupApi {

    List<UserDAO> getUsersFromConversations(final String accessToken, final short count, final long offset) throws IOException, InterruptedException;

    void sendMessage(final Message message, List<UserDAO> peers);
}
