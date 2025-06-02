package com.chat;

// ChatClient.java
import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void listenForMessages(Consumer<String> onMessageReceived) {
        new Thread(() -> {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    onMessageReceived.accept(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }


}
