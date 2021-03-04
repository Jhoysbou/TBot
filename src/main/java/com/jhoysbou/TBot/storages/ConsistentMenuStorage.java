package com.jhoysbou.TBot.storages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhoysbou.TBot.models.MenuAttachmentsDto;
import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.models.MenuItemStorageDto;
import com.jhoysbou.TBot.utils.AttachmentExtractor;
import com.jhoysbou.TBot.utils.OpenLinkExtractor;
import com.jhoysbou.TBot.utils.validation.ValidationException;
import com.jhoysbou.TBot.utils.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class ConsistentMenuStorage implements MenuStorage {
    private static final Logger log = LoggerFactory.getLogger(ConsistentMenuStorage.class);
    private static long COUNTER = 0;
    private final String PATH;
    private final MenuItem root;
    private final AttachmentExtractor attachmentExtractor;
    private final OpenLinkExtractor linkExtractor;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Validator<MenuItem> validator;

    @Autowired
    public ConsistentMenuStorage(@Value("${menu.storage.path}") String path,
                                 AttachmentExtractor attachmentExtractor,
                                 OpenLinkExtractor linkExtractor,
                                 Validator<MenuItem> validator) throws IOException {

        this.attachmentExtractor = attachmentExtractor;
        this.linkExtractor = linkExtractor;
        this.validator = validator;

        File file = Paths.get(path).toFile();
        if (file.exists()) {
            MenuItemStorageDto dto = objectMapper.readValue(file, MenuItemStorageDto.class);
            root = restoreParents(dto.getMenuItem(), null);
            COUNTER = dto.getId();
        } else {
            var responseText = "Привет!";
            root = new MenuItem(
                    COUNTER++,
                    null,
                    "start",
                    responseText
            );
            root.setAttachments(attachmentExtractor.parse(responseText));
        }

        PATH = path;
    }

    @PreDestroy
    void close() {
        try {
            save();
        } catch (IOException e) {
            log.error("Couldn't save storage before destroy!", e);
        }
    }

    @Override
    public MenuItem createMenuItem(Optional<MenuItem> parent, String trigger, String responseText) throws ValidationException {
        MenuItem parentItem = root;
        if (parent.isPresent()) {
            parentItem = parent.get();
        }

        final MenuItem item = new MenuItem(COUNTER++, parentItem, trigger, responseText);
        MenuAttachmentsDto attachments = attachmentExtractor.parse(responseText);
        item.setAttachments(attachments);
        parentItem.getChildren().add(item);
        try {
            validator.validate(parentItem);
        } catch (ValidationException e) {
            parentItem.getChildren().remove(item);
            throw e;
        }

        try {
            save();
        } catch (IOException e) {
            log.error("Couldn't save menu items!", e);
        }

        return item;
    }

    @Override
    public MenuItem getRoot() {
        return root;
    }

    @Override
    public void deleteMenuItem(MenuItem item) {
        item.getParent().getChildren().remove(item);
        try {
            save();
        } catch (IOException e) {
            log.error("Couldn't save menu items!", e);
        }
    }

    @Override
    public MenuItem updateMenuItem(long id,
                                   String trigger,
                                   String responseText) throws NoSuchElementException, ValidationException {
        validator.validate(new MenuItem(trigger, responseText));

        final MenuItem item = getMenuById(id).orElseThrow(NoSuchElementException::new);
        MenuAttachmentsDto attachments = attachmentExtractor.parse(responseText);
        var link = linkExtractor.extract(responseText);

        item.setAttachments(attachments);
        item.setTrigger(trigger);
        item.setUrl(link.orElse(""));
        item.setResponseText(responseText);

        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public MenuItem updateMenuItemTrigger(long id, String trigger) throws NoSuchElementException, ValidationException {
        validator.validate(new MenuItem(trigger, ""));
        final MenuItem item = getMenuById(id).orElseThrow(NoSuchElementException::new);

        item.setTrigger(trigger);
        try {
            save();
        } catch (IOException e) {
            log.error("Couldn't save menu items!", e);
        }
        return item;

    }

    @Override
    public MenuItem updateMenuItemResponse(long id, String responseText) throws NoSuchElementException, ValidationException {
        validator.validate(new MenuItem("", responseText));

        final MenuItem item = getMenuById(id).orElseThrow(NoSuchElementException::new);
        MenuAttachmentsDto attachments = attachmentExtractor.parse(responseText);
        var link  = linkExtractor.extract(responseText);
        item.setAttachments(attachments);
        item.setResponseText(responseText);
        item.setUrl(link.orElse(""));

        try {
            save();
        } catch (IOException e) {
            log.error("Couldn't save menu items!", e);
        }
        return item;
    }


    @Override
    public Optional<MenuItem> getMenuByText(String text) {
        return find(root, text);
    }

    @Override
    public Optional<MenuItem> getMenuByResponseText(String response) {
        return findByResponse(root, response);
    }

    @Override
    public Optional<MenuItem> getMenuById(long id) {
        return find(root, id);
    }


    private Optional<MenuItem> find(final MenuItem currentItem, final long id) {
        if (currentItem.getId() == id) {
            return Optional.of(currentItem);
        }

        for (MenuItem item : currentItem.getChildren()) {
            Optional<MenuItem> result = find(item, id);
            if (result.isPresent()) {
                return result;
            }
        }


        return Optional.empty();
    }

    // Case insensitive
    private Optional<MenuItem> find(final MenuItem currentItem, final String text) {
        if (currentItem.getTrigger().strip().toLowerCase(Locale.ROOT).equals(text.strip().toLowerCase(Locale.ROOT))) {
            return Optional.of(currentItem);
        }

        for (MenuItem item : currentItem.getChildren()) {

            Optional<MenuItem> result = find(item, text);
            if (result.isPresent()) {
                return result;
            }
        }

        return Optional.empty();
    }

    private Optional<MenuItem> findByResponse(final MenuItem currentItem, final String text) {
        if (currentItem.getResponseText().strip().toLowerCase(Locale.ROOT).equals(text.strip().toLowerCase(Locale.ROOT))) {
            return Optional.of(currentItem);
        }

        for (MenuItem item : currentItem.getChildren()) {

            Optional<MenuItem> result = findByResponse(item, text);
            if (result.isPresent()) {
                return result;
            }
        }

        return Optional.empty();
    }

    private void save() throws IOException {
        objectMapper.writeValue(Paths.get(PATH).toFile(), new MenuItemStorageDto(root, COUNTER));
    }

    private MenuItem restoreParents(final MenuItem item, final MenuItem parent) {
        item.setParent(parent);

        for (MenuItem i : item.getChildren()) {
            restoreParents(i, item);
        }
        return item;
    }

}
