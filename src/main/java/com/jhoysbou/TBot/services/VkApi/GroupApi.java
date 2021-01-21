package com.jhoysbou.TBot.services.VkApi;

import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.ConversationDAO;
import com.jhoysbou.TBot.models.vkmodels.ConversationWrapper;
import com.jhoysbou.TBot.models.vkmodels.UserDAO;

import java.io.IOException;
import java.util.List;

public interface GroupApi {

    /**
     * @param count Positive number less or equal than 200
     * @param offset
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    ConversationWrapper getConversations(final short count, final long offset) throws IOException, InterruptedException;

    void sendMessage(final Message message, List<UserDAO> peers);
}
