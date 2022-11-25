package com.jhoysbou.TBot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
  private long id;
  private List<MenuItem> children = new ArrayList<>();
  private MenuAttachmentsDto attachments;
  private String url = "";
  @JsonIgnore
  private MenuItem parent;
  private String trigger;
  private String responseText;
  private Boolean isSubscriptionRequired;

  public MenuItem() {

  }

  public MenuItem(String trigger, String responseText) {
    this.trigger = trigger;
    this.responseText = responseText;
  }

  public MenuItem(long id, MenuItem parent, String trigger, String responseText) {
    this.id = id;
    this.parent = parent;
    this.trigger = trigger;
    this.responseText = responseText;
  }

  public MenuItem(long id,
      MenuItem parent,
      String trigger,
      String responseText,
      Boolean isSubscriptionRequired) {
    this.id = id;
    this.parent = parent;
    this.trigger = trigger;
    this.responseText = responseText;
    this.isSubscriptionRequired = isSubscriptionRequired;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<MenuItem> getChildren() {
    return children;
  }

  public void setChildren(List<MenuItem> children) {
    this.children = children;
  }

  public MenuItem getParent() {
    return parent;
  }

  public void setParent(MenuItem parent) {
    this.parent = parent;
  }

  public String getTrigger() {
    return trigger;
  }

  public void setTrigger(String trigger) {
    this.trigger = trigger;
  }

  public String getResponseText() {
    return responseText;
  }

  public void setResponseText(String responseText) {
    this.responseText = responseText;
  }

  public MenuAttachmentsDto getAttachments() {
    return attachments;
  }

  public void setAttachments(MenuAttachmentsDto attachments) {
    this.attachments = attachments;
  }

  public Boolean isSubscriptionRequired() {
    return isSubscriptionRequired;
  }

  public void setSubscriptionRequired(Boolean isSubscriptionRequired) {
    this.isSubscriptionRequired = isSubscriptionRequired;
  }
}
