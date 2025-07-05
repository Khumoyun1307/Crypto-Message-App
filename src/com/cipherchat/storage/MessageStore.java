package com.cipherchat.storage;

import com.cipherchat.model.Message;
import java.io.IOException;
import java.util.List;

/**
 * Interface for loading and saving user inboxes.
 */
public interface MessageStore {
    List<Message> load(String username) throws IOException, ClassNotFoundException;
    void save(String username, Message message) throws IOException, ClassNotFoundException;
}
