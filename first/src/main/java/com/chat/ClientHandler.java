package com.chat;

// ClientHandler.java
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.scene.control.TextField;

// ClientHandler ch = new ClientHandler(p1,p2) ao atribuir em uma variável; 
public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private List<ClientHandler> clients;
    private String username;
    private TextField message;

    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {        
        this.socket = socket;
        this.clients = clients;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run() {
        try {
            out.println(username);
            username = in.readLine();

            String msg;
            while ((msg = in.readLine()) != null) {
                broadcast(username + ": " + msg);
            }
        } catch (IOException e) {
            System.out.println("Conexão perdida com " + username);
        } finally {
            try {
                clients.remove(this);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcast(String msg) {
        for (ClientHandler client : clients) {
            client.out.println(msg);
        }
    }
}
