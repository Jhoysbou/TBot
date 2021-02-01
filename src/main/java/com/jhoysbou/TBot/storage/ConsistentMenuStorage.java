package com.jhoysbou.TBot.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhoysbou.TBot.models.Attachment;
import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.models.MenuItemStorageDto;
import com.jhoysbou.TBot.utils.AttachmentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class ConsistentMenuStorage implements MenuStorage {
    private static final Logger log = LoggerFactory.getLogger(ConsistentMenuStorage.class);
    private static long COUNTER = 0;
    private final String PATH;
    private final MenuItem root;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AttachmentParser attachmentParser;

    @Autowired
    public ConsistentMenuStorage(@Value("${menu.storage.path}") String path,
                                 AttachmentParser attachmentParser) throws IOException {
        this.attachmentParser = attachmentParser;
        File file = Paths.get(path).toFile();
        if (file.exists()) {
            MenuItemStorageDto dto = objectMapper.readValue(file, MenuItemStorageDto.class);
            root = dto.getMenuItem();
            COUNTER = dto.getId();
        } else {
            root = new MenuItem(
                    COUNTER++,
                    null,
                    "start",
                    "Привет!"
            );
        }

        PATH = path;
    }

    @Override
    public MenuItem createMenuItem(Optional<MenuItem> parent, String trigger, String responseText) {
        MenuItem parentItem = root;
        if (parent.isPresent()) {
            parentItem = parent.get();
        }
        List<Attachment> attachments = attachmentParser.parse(responseText);

        final MenuItem item = new MenuItem(COUNTER++, parentItem, trigger, responseText);
        item.setAttachments(attachments);
        parentItem.getChildren().add(item);

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
                                   String responseText) throws NoSuchElementException {
        final MenuItem item = getMenuById(id).orElseThrow(NoSuchElementException::new);
        List<Attachment> attachments = attachmentParser.parse(responseText);
        item.setTrigger(trigger);
        item.setResponseText(responseText);
        item.setAttachments(attachments);

        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public MenuItem updateMenuItemTrigger(long id, String trigger) throws NoSuchElementException {
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
    public MenuItem updateMenuItemResponse(long id, String responseText) throws NoSuchElementException {
        List<Attachment> attachments = attachmentParser.parse(responseText);
        final MenuItem item = getMenuById(id).orElseThrow(NoSuchElementException::new);
        item.setAttachments(attachments);
        item.setResponseText(responseText);
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

    private void save() throws IOException {
        objectMapper.writeValue(Paths.get(PATH).toFile(), new MenuItemStorageDto(root, COUNTER));
    }

}
