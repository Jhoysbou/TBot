package com.jhoysbou.TBot.services.VkApi;

import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.ConversationWrapper;

import java.io.IOException;
import java.util.List;

public interface GroupApi {

    /**
     * @param count  Positive number less or equal than 200. Number of conversations to return
     * @param offset Positive number. Offset needed to return a specific subset of conversations
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    ConversationWrapper getConversations(final short count, final long offset) throws IOException, InterruptedException;

    void sendMessage(final Message message, List<Long> peers) throws IOException, InterruptedException;

    String getConfirmationCode() throws IOException, InterruptedException;
}
