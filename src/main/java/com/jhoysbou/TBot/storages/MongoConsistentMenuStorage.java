package com.jhoysbou.TBot.storages;

import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.utils.AttachmentExtractor;
import com.jhoysbou.TBot.utils.validation.ValidationException;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.awt.*;
import java.util.NoSuchElementException;
import java.util.Optional;

public class MongoConsistentMenuStorage implements MenuStorage {
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection collection;
    private static long COUNTER = 0;
    private final AttachmentExtractor attachmentExtractor;

    @Autowired
    public MongoConsistentMenuStorage(@Value("${mongo.host}") final String mongoHost,
                                      @Value("${mongo.port}") final String mongoPort,
                                      @Value("${mongo.user}") final String user,
                                      @Value("${mongo.password}") final int password,
                                      AttachmentExtractor attachmentExtractor) {

        final MongoClientURI uri = new MongoClientURI(
                "mongodb://%s:%s@%s:@%s/"
                        .formatted(user, password, mongoHost, mongoPort)
        );
        this.mongoClient = new MongoClient(uri);
        this.database = mongoClient.getDatabase("tbot");
        this.collection = database.getCollection("menu");
        this.attachmentExtractor = attachmentExtractor;
    }

    private MenuItem getOrCreateRootMenu() {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("id", "0");
        FindIterable<MenuItem> iter = collection.find(searchQuery);
        // Must be exactly one value
        for (MenuItem item : iter) {
            return item;
        }

        // No values here, so we need to create one
        var responseText = "Привет!";
        var item = new MenuItem(
                COUNTER++,
                null,
                "start",
                responseText
        );
        item.setAttachments(attachmentExtractor.parse(responseText));
        return item;
    }

    private Document createDocument(final MenuItem item) {
        Document doc = new Document("_id", new ObjectId());
        doc
            .append("id", item.getId())
            .append("url", item.getUrl())
            .append("trigger", item.get)

    }

    @Override
    public MenuItem createMenuItem(Optional<MenuItem> parent, String trigger, String responseText) throws ValidationException {
        return null;
    }

    @Override
    public MenuItem getRoot() {
        return null;
    }

    @Override
    public void deleteMenuItem(MenuItem item) {

    }

    @Override
    public MenuItem updateMenuItem(long id, String trigger, String responseText) throws NoSuchElementException, ValidationException {
        return null;
    }

    @Override
    public MenuItem updateMenuItemTrigger(long id, String trigger) throws NoSuchElementException, ValidationException {
        return null;
    }

    @Override
    public MenuItem updateMenuItemResponse(long id, String responseText) throws NoSuchElementException, ValidationException {
        return null;
    }

    @Override
    public Optional<MenuItem> getMenuByText(String text) {
        return Optional.empty();
    }

    @Override
    public Optional<MenuItem> getMenuByResponseText(String response) {
        return Optional.empty();
    }

    @Override
    public Optional<MenuItem> getMenuById(long id) {
        return Optional.empty();
    }
}
