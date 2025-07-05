package com.cipherchat.storage;

import com.cipherchat.model.Message;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-backed MessageStore using data/messages/{username}.msgs.
 */
public class FileMessageStore implements MessageStore {
    private static final String MESSAGE_DIR = "data/messages";

    @Override
    @SuppressWarnings("unchecked")
    public List<Message> load(String username) throws IOException, ClassNotFoundException {
        Path dir = Paths.get(MESSAGE_DIR);
        Files.createDirectories(dir);
        Path file = dir.resolve(username + ".msgs");
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
            return (List<Message>) ois.readObject();
        }
    }

    @Override
    public void save(String username, Message message) throws IOException, ClassNotFoundException {
        List<Message> messages = load(username);
        messages.add(message);

        Path dir = Paths.get(MESSAGE_DIR);
        Files.createDirectories(dir);
        Path file = dir.resolve(username + ".msgs");
        Path temp = dir.resolve(username + ".msgs.tmp");

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(temp))) {
            oos.writeObject(messages);
        }
        Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }
}
