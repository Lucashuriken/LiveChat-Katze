package com.chat;

import java.util.HashMap;
import java.util.Map;

public class ChatWindowManager {
    private static final Map<String, ChatWindow> windows = new HashMap<>();

    public static void register(String contactName, ChatWindow window) {
        windows.put(contactName, window);
    }

    public static ChatWindow getWindow(String contactName) {
        return windows.get(contactName);
    }
}
