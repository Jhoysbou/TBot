package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.storages.MenuStorage;
import com.jhoysbou.TBot.storages.TopicStorage;
import com.jhoysbou.TBot.utils.HashtagParser;
import com.jhoysbou.TBot.utils.ServicePreferenceTag;
import com.jhoysbou.TBot.utils.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DefaultEditingService implements EditingService {
    private final MenuStorage storage;
    private final TopicStorage subscriptionStorage;
    private final HashtagParser hashtagParser;

    @Autowired
    public DefaultEditingService(@Qualifier(value = "consistentMenuStorage") MenuStorage storage,
                                 TopicStorage subscriptionStorage,
                                 HashtagParser hashtagParser) {
        this.storage = storage;
        this.subscriptionStorage = subscriptionStorage;
        this.hashtagParser = hashtagParser;
    }

    @Override
    public MenuItem getRoot() {
        return storage.getRoot();
    }

    @Override
    public MenuItem getMenuItemById(long id) {
        return storage.getMenuById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void updateMenuItem(long id, Optional<String> trigger, Optional<String> responseText) throws ValidationException {
        responseText.ifPresent(response -> {
            var menuItem = storage.getMenuById(id).orElseThrow(NoSuchElementException::new);
            var hashtag = hashtagParser.parse(responseText);

            hashtag.ifPresent(tag -> {
//              Changing topic names is not supported. News topics can only be created or deleted
//              therefore we should create topic in NewsPreferenceStorage, otherwise throwing exception.
                if (menuItem.getTrigger().equals("") && menuItem.getResponseText().equals("")) {
//                  Do not save service tags
                    if (!tag.equals(ServicePreferenceTag.ALL) && !tag.equals(ServicePreferenceTag.NONE)) {
                        subscriptionStorage.addNewPreference(hashtag.get());
                    }
                } else {
                    throw new UnsupportedOperationException("Changing topic names is not supported");
                }
            });
        });
        if (trigger.isPresent() && responseText.isPresent()) {
            storage.updateMenuItem(id, trigger.get(), responseText.get());
        } else {
            trigger.ifPresent(s -> storage.updateMenuItemTrigger(id, s));
            responseText.ifPresent(s -> storage.updateMenuItemResponse(id, s));
        }
    }

    @Override
    public void createNewMenuItem(final long parentId) throws ValidationException {
        storage.createMenuItem(
                Optional.of(getMenuItemById(parentId)),
                "",
                ""
        );
    }

    @Override
    public void deleteMenuItem(long id) {
        var menuItem = storage.getMenuById(id);
        menuItem.ifPresent(item -> {
            var hashtag = hashtagParser.parse(Optional.ofNullable(item.getResponseText()));
            hashtag.ifPresent(subscriptionStorage::deletePreference);
        });
        storage.deleteMenuItem(
                storage.getMenuById(id).orElseThrow(NoSuchElementException::new)
        );
    }
}
