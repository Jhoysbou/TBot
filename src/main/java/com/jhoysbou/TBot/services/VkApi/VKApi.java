package com.jhoysbou.TBot.services.VkApi;

import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.ConversationDAO;
import com.jhoysbou.TBot.models.vkmodels.ConversationWrapper;
import com.jhoysbou.TBot.models.vkmodels.UserDAO;
import com.jhoysbou.TBot.services.VkApi.handlers.ConversationBodyHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VKApi implements GroupApi {
    private static final String URL = "https://api.vk.com/method/";
    private final String ACCESS_KEY;
    private final HttpClient client;

    public VKApi(@Value("${vk.access.key}") String accessKey) {
        this.ACCESS_KEY = accessKey;
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public List<ConversationDAO> getConversations(String accessToken, short count, long offset) throws IOException, InterruptedException {
        final URI uri = URI.create(URL
                + "messages.getConversations?access_token=" + ACCESS_KEY
                + "&count=" + count
                + "&offset=" + offset
                + "&extended=0");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<ConversationWrapper> response = client.send(request, new ConversationBodyHandler());

        return response
                .body()
                .getItems()
                .stream()
                .map(ConversationWrapper.Pair::getConversation)
                .collect(Collectors.toList());
    }

    @Override
    public void sendMessage(Message message, List<UserDAO> peers) {

    }


}
