package com.cipherchat.app;

import com.cipherchat.model.UserProfile;
import com.cipherchat.model.Message;
import com.cipherchat.engine.CryptoException;
import com.cipherchat.engine.model.DecryptResult;

import java.io.IOException;
import java.util.List;

public class AppController {
    private final ConsoleUI ui = new ConsoleUI();
    private UserProfile user;

    public void start() {
        while (true) {
            ui.showStartupMenu();
            String choice = ui.prompt("Choose option");
            switch (choice) {
                case "1": createUser(); break;
                case "2": loadUser(); break;
                case "3": exit();
                    return;
                default: ui.showMessage("Invalid choice. Try again.");
            }
        }
    }

    private void createUser() {
        String name = ui.prompt("Enter new username");
        try {
            user = UserProfile.createNewUser(name);
            ui.showMessage("User created: " + name);
            mainLoop();
        } catch (CryptoException | IOException e) {
            ui.showMessage("Error creating user: " + e.getMessage());
        }
    }

    private void loadUser() {
        String name = ui.prompt("Enter username to load");
        try {
            user = UserProfile.loadUser(name);
            ui.showMessage("User loaded: " + name);
            mainLoop();
        } catch (Exception e) {
            ui.showMessage("Error loading user: " + e.getMessage());
        }
    }

    private void mainLoop() {
        while (user != null) {
            ui.showMainMenu(user.getUsername());
            String choice = ui.prompt("Choose option");
            switch (choice) {
                case "1": sendMessage(); break;
                case "2": readInbox(); break;
                case "3": logout(); return;
                case "4": exit(); return;
                default: ui.showMessage("Invalid choice. Try again.");
            }
        }
    }

    private void sendMessage() {
        String recipient = ui.prompt("Recipient username");
        String text = ui.prompt("Enter message");
        try {
            user.sendMessage(recipient, text);
            ui.showMessage("Message sent to " + recipient);
        } catch (CryptoException | IOException | ClassNotFoundException e) {
            ui.showMessage("Error sending message: " + e.getMessage());
        }
    }

    private void readInbox() {
        try {
            user.refreshInbox();
            List<Message> inbox = user.getInbox();
            ui.showInbox(inbox);
            if (!inbox.isEmpty()) {
                String input = ui.prompt("Enter message number to view or 'a' for all");
                if (input.equalsIgnoreCase("a")) {
                    for (Message m : inbox) {
                        displayMessage(m);
                    }
                } else {
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx >= 0 && idx < inbox.size()) {
                            displayMessage(inbox.get(idx));
                        } else {
                            ui.showMessage("Invalid message number.");
                        }
                    } catch (NumberFormatException e) {
                        ui.showMessage("Invalid input.");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            ui.showMessage("Error reading inbox: " + e.getMessage());
        }
    }

    private void displayMessage(Message msg) {
        try {
            DecryptResult dr = msg.decrypt(user.getPrivateKey());
            if (dr.isSignatureVerified()) {
                ui.showMessage(String.format("\n[%s] From %s: %s", msg.getTimestamp(), dr.getSenderUsername(), new String(dr.getPlainText())));
            } else {
                ui.showMessage("Signature verification failed for message from " + dr.getSenderUsername());
            }
        } catch (CryptoException e) {
            ui.showMessage("Error decrypting message: " + e.getMessage());
        }
    }

    private void logout() {
        ui.showMessage("Logging out...");
        user = null;
    }

    private void exit() {
        ui.showMessage("Exiting CipherChat. Goodbye!");
        System.exit(0);
    }
}