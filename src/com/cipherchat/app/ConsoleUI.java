package com.cipherchat.app;

import com.cipherchat.model.Message;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);

    public void showStartupMenu() {
        System.out.println("\n== Welcome to CipherChat ==");
        System.out.println("1. Create new user");
        System.out.println("2. Load existing user");
        System.out.println("3. Exit");
    }

    public void showMainMenu(String username) {
        System.out.println("\n== Main Menu (" + username + ") ==");
        System.out.println("1. Send encrypted message");
        System.out.println("2. Read inbox");
        System.out.println("3. Logout");
        System.out.println("4. Exit");
    }

    public String prompt(String message) {
        System.out.print(message + ": ");
        return scanner.nextLine().trim();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showInbox(List<Message> inbox) {
        if (inbox.isEmpty()) {
            showMessage("Inbox is empty.");
            return;
        }
        System.out.println("== Inbox ==");
        for (int i = 0; i < inbox.size(); i++) {
            Message m = inbox.get(i);
            System.out.printf("%d. From %s at %s%n", i + 1, m.getSenderUsername(), m.getTimestamp());
        }
    }
}