package com.chat;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Session {
    private static User currentUser;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static ChatClient chatClient;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
    public static void setSocket(Socket s) { socket = s; }
    public static Socket getSocket() { return socket; }

    public static void setOut(PrintWriter writer) { out = writer; }
    public static PrintWriter getOut() { return out; }

    public static void setIn(BufferedReader reader) { in = reader; }
    public static BufferedReader getIn() { return in; }
    
    public static void setChatClient(ChatClient client) { chatClient = client; }
    public static ChatClient getChatClient() { return chatClient; }

}
