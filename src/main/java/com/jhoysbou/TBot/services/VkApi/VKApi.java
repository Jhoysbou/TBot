package com.jhoysbou.TBot.services.VkApi;

import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.ConversationWrapper;
import com.jhoysbou.TBot.models.vkmodels.UserDAO;
import com.jhoysbou.TBot.services.VkApi.bodyhandlers.ConversationWrapperBodyHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class VKApi implements GroupApi {
    private static final String URL = "https://api.vk.com/method/";
    private final String ACCESS_TOKEN;
    private final HttpClient client;
    private final String API_VERSION;

    public VKApi(@Value("${vk.access.key}") String accessKey,
                 @Value("${vk.api.version}") String api_version) {
        this.ACCESS_TOKEN = accessKey;
        API_VERSION = api_version;
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public ConversationWrapper getConversations(final short count, final long offset) throws IOException, InterruptedException {
        final URI uri = URI.create(URL
                + "messages.getConversations?access_token=" + ACCESS_TOKEN
                + "&count=" + count
                + "&v=" + API_VERSION
                + "&offset=" + offset
                + "&extended="
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<ConversationWrapper> response = client.send(request, new ConversationWrapperBodyHandler());

        return response.body();
    }

    @Override
    public void sendMessage(Message message, List<Long> peers) throws IOException, InterruptedException {
        final URI uri = URI.create(URL
                + "messages.send?access_token=" + ACCESS_TOKEN
                + "&peer_ids=" + peers.stream().distinct().map(String::valueOf).reduce((acc, id) -> acc + "," + id).orElse("")
                + "&random_id=" + System.currentTimeMillis()
                + "&message=" + message.getText()
                + "&v=" + API_VERSION
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }


}
