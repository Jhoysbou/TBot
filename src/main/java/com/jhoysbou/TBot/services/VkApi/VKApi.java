package com.jhoysbou.TBot.services.VkApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.ConversationWrapper;
import com.jhoysbou.TBot.services.DefaultFAQService;
import com.jhoysbou.TBot.services.VkApi.bodyhandlers.ConversationWrapperBodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(VKApi.class);
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

        String uri = URL
                + "messages.send?access_token=" + ACCESS_TOKEN
                + "&peer_ids=" + peers.stream().distinct().map(String::valueOf).reduce((acc, id) -> acc + "," + id).orElse("")
                + "&random_id=" + System.currentTimeMillis()
                + "&v=" + API_VERSION;

        if (message.hasAttachment()) {
            var attachment = message.getAttachment();
            uri += "&attachment=" + attachment.getType() + attachment.getOwner_id() + "_" + attachment.getMedia_id();
        }

        if (message.hasText() && !message.getText().equals("")) {
            uri += "&message=" + message.getText();
        }

        if (message.hasKeyboard()) {
            ObjectWriter ow = new ObjectMapper().writer();
            uri += "&keyboard=" + ow.writeValueAsString(message.getKeyboard())
                    .replace("{", "%7B")
                    .replace("}", "%7D")
                    .replace("\"", "%22");
        }

        uri = uri.replace(" ", "+");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.debug("message send response body is {} ", response.body());
    }


}
