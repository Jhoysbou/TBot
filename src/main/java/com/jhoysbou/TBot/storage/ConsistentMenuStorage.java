package com.jhoysbou.TBot.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.models.MenuItemStorageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class ConsistentMenuStorage implements MenuStorage {
    private static long COUNTER = 0;
    private final String PATH;
    private final MenuItem root;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ConsistentMenuStorage(@Value("${menu.storage.path}") String path) throws IOException {
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

        final MenuItem item = new MenuItem(COUNTER++, parentItem, trigger, responseText);
        parentItem.getChildren().add(item);

        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public MenuItem updateMenuItem(long id,
                                   String trigger,
                                   String responseText) throws NoSuchElementException {
        final MenuItem item = getMenuById(id).orElseThrow(NoSuchElementException::new);
        item.setTrigger(trigger);
        item.setResponseText(responseText);
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
            e.printStackTrace();
        }
        return item;

    }

    @Override
    public MenuItem updateMenuItemResponse(long id, String responseText) throws NoSuchElementException {
        final MenuItem item = getMenuById(id).orElseThrow(NoSuchElementException::new);
        item.setResponseText(responseText);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
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
