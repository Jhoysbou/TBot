package com.jhoysbou.TBot.services.VkApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.ConversationWrapper;
import com.jhoysbou.TBot.models.vkmodels.ResponseWrapper;
import com.jhoysbou.TBot.services.VkApi.bodyhandlers.ConversationWrapperBodyHandler;
import com.jhoysbou.TBot.services.VkApi.bodyhandlers.IsMemberBodyHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class VKApi implements GroupApi {
  private static final Logger log = LoggerFactory.getLogger(VKApi.class);
  private static final String URL = "https://api.vk.com/method/";
  private final String ACCESS_TOKEN;
  private final HttpClient client;
  private final String API_VERSION;
  private final String GROUP_ID;

  public VKApi(@Value("${vk.access.key}") String accessKey,
      @Value("${vk.api.version}") String apiVersion,
      @Value("${vk.group.id}") String groupId) {
    this.ACCESS_TOKEN = accessKey;
    this.API_VERSION = apiVersion;
    this.GROUP_ID = groupId;
    this.client = HttpClient.newHttpClient();
  }

  @Override
  public ConversationWrapper getConversations(final short count, final long offset)
      throws IOException, InterruptedException {
    final URI uri = URI.create(URL
        + "messages.getConversations?access_token=" + ACCESS_TOKEN
        + "&count=" + count
        + "&v=" + API_VERSION
        + "&offset=" + offset
        + "&extended=");

    HttpRequest request = HttpRequest.newBuilder()
        .uri(uri)
        .GET()
        .build();

    HttpResponse<ConversationWrapper> response = client.send(request, new ConversationWrapperBodyHandler());

    return response.body();
  }

  /**
   * @param message
   * @param peers   no more than 100
   * @throws IOException
   * @throws InterruptedException
   */
  @Override
  public void sendMessage(Message message, List<Long> peers) throws IOException, InterruptedException {

    String uri = URL
        + "messages.send?access_token=" + ACCESS_TOKEN
        + "&peer_ids=" + peers.stream().distinct().map(String::valueOf).reduce((acc, id) -> acc + "," + id).orElse("")
        + "&random_id=" + System.currentTimeMillis()
        + "&dont_parse_links=1"
        + "&v=" + API_VERSION;

    String body = "";
    if (message.hasAttachment()) {
      var attachments = message.getAttachments();
      body += "&attachment=" + attachments.stream()
          .map(attachment -> attachment.getType() + attachment.getOwner_id() + "_" + attachment.getMedia_id())
          .reduce((acc, cur) -> acc + "," + cur)
          .get();
    }

    if (message.hasText()) {
      body += "&message=" + URLEncoder.encode(message.getText(), StandardCharsets.UTF_8);
    }

    if (message.hasKeyboard()) {
      ObjectWriter ow = new ObjectMapper().writer();
      body += "&keyboard=" + URLEncoder.encode(ow.writeValueAsString(message.getKeyboard()), StandardCharsets.UTF_8);
    }

    // encode url
    uri = uri.replace(" ", "+");

    HttpRequest request = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .uri(URI.create(uri))
        .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
        .build();

    CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    response.thenAccept(r -> log.info("message send response body is {} ", r.body()));
  }

  @Override
  public boolean isMember(long userId) throws IOException, InterruptedException {
    URI uri = URI.create(URL
        + "groups.isMember?access_token=" + ACCESS_TOKEN
        + "&user_id=" + String.valueOf(userId)
        + "&group_id=" + this.GROUP_ID
        + "&v=" + this.API_VERSION);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(uri)
        .GET()
        .build();

    HttpResponse<ResponseWrapper<Integer>> response = client.send(request, new IsMemberBodyHandler());

    ResponseWrapper<Integer> isMemberResponse = response.body();

    return isMemberResponse.getResponse() == 1;
  }

}
