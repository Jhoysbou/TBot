package com.jhoysbou.TBot.services.VkApi.bodyhandlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhoysbou.TBot.models.vkmodels.ConversationWrapper;
import com.jhoysbou.TBot.models.vkmodels.ResponseWrapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.ResponseInfo;
import java.nio.charset.StandardCharsets;

public class IsMemberBodyHandler implements HttpResponse.BodyHandler<ResponseWrapper<Integer>> {

  @Override
  public BodySubscriber<ResponseWrapper<Integer>> apply(ResponseInfo responseInfo) {
    HttpResponse.BodySubscriber<String> upstream = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);

    return HttpResponse.BodySubscribers.mapping(
        upstream,
        (String body) -> {
          try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(
                body,
                new TypeReference<ResponseWrapper<Integer>>() {
                });
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
        });
  }

}
