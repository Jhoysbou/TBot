package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.MenuAttachmentsDto;
import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.models.vkmodels.MessageDAO;
import com.jhoysbou.TBot.models.vkmodels.NewMessageWrapper;
import com.jhoysbou.TBot.models.vkmodels.keyboard.*;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import com.jhoysbou.TBot.storages.IdStorage;
import com.jhoysbou.TBot.storages.MenuStorage;
import com.jhoysbou.TBot.storages.TopicStorage;
import com.jhoysbou.TBot.utils.HashtagParser;
import com.jhoysbou.TBot.utils.ServicePreferenceTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DefaultMessageService implements MessageService {
  private static final Logger log = LoggerFactory.getLogger(DefaultMessageService.class);

  private final GroupApi api;
  private final MenuStorage menuStorage;
  private final IdStorage<Long> userIdStorage;
  private final HashtagParser hashtagParser;
  private final TopicStorage subscriptionStorage;

  @Autowired
  public DefaultMessageService(GroupApi api,
      @Qualifier(value = "consistentMenuStorage") MenuStorage menuStorage,
      IdStorage<Long> userIdStorage, HashtagParser hashtagParser,
      TopicStorage subscriptionStorage) {
    this.api = api;
    this.menuStorage = menuStorage;
    this.userIdStorage = userIdStorage;
    this.hashtagParser = hashtagParser;
    this.subscriptionStorage = subscriptionStorage;
  }

  @Override
  public void handleMessage(GroupEventDAO<NewMessageWrapper> event) {
    final MessageDAO message = event.getObject().getMessage();
    final long peer = message.getFrom_id();

    // Save user id for important notifications
    saveUserId(peer);

    final String text = message.getText().toLowerCase(Locale.ROOT);

    Optional<MenuItem> menuItem = menuStorage.getMenuByText(text);
    Optional<String> hashtag = Optional.empty();

    if (menuItem.isPresent()) {
      hashtag = hashtagParser
          .parse(
              Optional.ofNullable(
                  menuItem.get().getResponseText()));
    }

    if (hashtag.isPresent()) {
      handlePreference(hashtag.get(), peer, text);
    } else if (text.equals(ControlButton.HOME)) {
      sendMessage(menuStorage.getRoot(), peer);
    } else if (text.equals(ControlButton.BACK)) {
      MenuItem currentMenuItem = menuStorage
          .getMenuById(
              Long.parseLong(
                  Optional.ofNullable(message.getPayload()).orElse("0")))
          .orElseThrow(NoSuchElementException::new);

      MenuItem parent = currentMenuItem.getParent();
      if (parent == null) {
        parent = menuStorage.getRoot();
      }
      sendMessage(parent, peer);
    } else {
      if (menuItem.isPresent()) {
        MenuItem currentMenuItem = menuItem.get();
        boolean isSubRequired = currentMenuItem.isSubscriptionRequired();

        if (isSubRequired) {
          boolean isMember = false;
          try {
            isMember = this.api.isMember(peer);
          } catch (IOException | InterruptedException e) {
            log.error("Exception while trying get member status");
          }

          if (isMember) {
            sendMessage(currentMenuItem, peer);
          } else {
            sendMessage(
                "Вам нужно подписаться на группу, чтобы получить доступ.",
                makeKeyboard(currentMenuItem.getParent()),
                List.of(peer));
          }

        } else {
          sendMessage(currentMenuItem, peer);
        }
      }
    }
  }

  private void handlePreference(final String hashtag, final long userId, final String text) {
    Optional<MenuItem> menuItem = menuStorage.getMenuByText(text);

    if (hashtag.equals(ServicePreferenceTag.ALL)) {
      subscriptionStorage.addAllPreferenceToUser(userId);
      menuItem.ifPresent(item -> {
        sendMessage(
            "Вы успешно подписались на все категории!",
            makeKeyboard(item),
            List.of(userId));
      });
    } else if (hashtag.equals(ServicePreferenceTag.NONE)) {
      subscriptionStorage.deleteAllPreferencesFromUser(userId);
      menuItem.ifPresent(item -> {
        sendMessage(
            "Вы успешно отписались от всех категорий",
            makeKeyboard(item),
            List.of(userId));
      });
    } else if (subscriptionStorage.getByTag(hashtag).orElse(new HashSet<>()).contains(userId)) {
      subscriptionStorage.deletePreferenceFromUser(hashtag, userId);
      menuItem.ifPresent(item -> {
        sendMessage(
            "Вы успешно отписались от категории " + item.getTrigger(),
            makeKeyboard(item),
            List.of(userId));
      });

    } else {
      subscriptionStorage.addPreferenceToUser(hashtag, userId);
      menuItem.ifPresent(item -> {
        sendMessage(
            "Вы успешно подписались на категорию " + item.getTrigger(),
            makeKeyboard(item),
            List.of(userId));
      });

    }

  }

  private void sendMessage(final String message, final KeyboardDAO keyboard, final List<Long> peers) {
    try {
      api.sendMessage(
          new Message(
              message,
              keyboard),
          peers);
    } catch (IOException | InterruptedException e) {
      log.error("Couldn't send message", e);
    }

  }

  private void sendMessage(final MenuItem menuItem, final Long peer) {
    final KeyboardDAO keyboard = makeKeyboard(menuItem);
    MenuAttachmentsDto attachments = menuItem.getAttachments();
    String text = attachments.getText();
    AtomicReference<String> topics = new AtomicReference<>();

    subscriptionStorage.getTopicsByUser(peer)
        .stream()
        .map(menuStorage::getMenuByResponseText)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(MenuItem::getTrigger)
        .reduce((acc, cur) -> acc + ", " + cur)
        .ifPresentOrElse(
            mergedTopics -> topics.set("Вы подписны на категории: " + mergedTopics),
            () -> topics.set("Пока вы ни на что не подписаны \uD83D\uDE14"));

    // Checking tag to print all user's subscriptions
    text = text.replace(
        ServicePreferenceTag.TOPICS,
        topics.get());

    try {
      api.sendMessage(new Message(text, attachments.getAttachments(), keyboard), List.of(peer));
    } catch (IOException | InterruptedException e) {
      log.error("Couldn't send message", e);
    }
  }

  private KeyboardDAO makeKeyboard(final MenuItem item) {
    final KeyboardDAO keyboard = new KeyboardDAO();
    keyboard.setInline(true);
    final List<Button[]> buttons = new ArrayList<>();
    List<MenuItem> menuItems = item.getChildren();

    for (MenuItem menuItem : menuItems) {
      Button button;

      if (menuItem.getUrl().length() == 0) {
        button = new ColoredButton(
            "secondary",
            new TextAction(menuItem.getTrigger(), ""));
      } else {
        button = new Button(
            new OpenLinkAction(menuItem.getTrigger(), "", menuItem.getUrl()));

      }

      buttons.add(new Button[] { button });
    }
    // Adding control buttons
    if (item.getParent() != null) {
      buttons.add(
          new Button[] {
              new ColoredButton(
                  "primary",
                  new TextAction(ControlButton.BACK, String.valueOf(item.getId()))),
              new ColoredButton(
                  "secondary",
                  new TextAction(ControlButton.HOME, ""))
          });
    }

    keyboard.setButtons(buttons);
    return keyboard;
  }

  private void saveUserId(long userId) {
    userIdStorage.addId(userId);
  }

  private static class ControlButton {
    private static final String HOME = "домой";
    private static final String BACK = "назад";
  }
}
