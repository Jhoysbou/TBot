package com.jhoysbou.TBot.models;

import java.util.Optional;

public class UpdateItemDto {
  private Optional<String> trigger;
  private Optional<Boolean> isSubscriptionRequired;
  private Optional<String> responseText;

  public Optional<String> getTrigger() {
    return trigger;
  }

  public void setTrigger(Optional<String> trigger) {
    this.trigger = trigger;
  }

  public Optional<String> getResponseText() {
    return responseText;
  }

  public void setResponseText(Optional<String> responseText) {
    this.responseText = responseText;
  }

  public Optional<Boolean> getIsSubscriptionRequired() {
    return isSubscriptionRequired;
  }

  public void setIsSubscriptionRequired(Optional<Boolean> isSubscriptionRequired) {
    this.isSubscriptionRequired = isSubscriptionRequired;
  }
}
