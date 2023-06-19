
package newsroom2;

//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@ServerEndpoint("/newsroom")
//public class Newsroom2Server {
//    private static List<Session> clients = new ArrayList<>();
//
//    @OnOpen
//    public void onOpen(Session session) {
//        System.out.println("Client connected: " + session.getId());
//        clients.add(session);
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        System.out.println("Received message from " + session.getId() + ": " + message);
//
//        if (message.equals("CONNECT")) {
//            try {
//                // Accept the connection request
//                session.getBasicRemote().sendText("ACCEPTED");
//                session.getBasicRemote().sendText("Welcome to the newsroom!");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            // Broadcast the message to all connected clients
//            for (Session client : clients) {
//                try {
//                    client.getBasicRemote().sendText("Message from " + session.getId() + ": " + message);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        System.out.println("Client disconnected: " + session.getId());
//        clients.remove(session);
//    }
//
//    @OnError
//    public void onError(Session session, Throwable error) {
//        System.out.println("Error occurred for client: " + session.getId());
//        error.printStackTrace();
//    }
//}

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Newsroom2Server {
    private static List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server started. Waiting for clients to connect...");

            while (true) {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Start a new thread to handle the client
                Thread clientThread = new Thread(() -> {
                    try {
                        // Create the reader and writer for the client
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                        // Read the client's request
                        String request = in.readLine();

                        // Check if the client is requesting to connect
                        if (request.equals("CONNECT")) {
                            // Accept the connection request
                            out.println("ACCEPTED");
                            clients.add(out);

                            // Send a welcome message to the client
                            out.println("Welcome to the newsroom!");

                            // Continuously read messages from the client
                            String message;
                            while ((message = in.readLine()) != null) {
                                if (message.equalsIgnoreCase("LIST")) {
                                    // Send a list of connected clients
                                    out.println("Connected Clients:");
                                    for (PrintWriter client : clients) {
                                        out.println("- " + client);
                                    }
                                } else if (message.startsWith("SENDTO:")) {
                                    // Send a message to specific clients
                                    String[] parts = message.split(":", 2);
                                    String recipient = parts[1].trim();

                                    for (PrintWriter client : clients) {
                                        if (client.toString().equals(recipient)) {
                                            client.println("Message from " + clientSocket + ": " + parts[0]);
                                            break;
                                        }
                                    }
                                } else {
                                    // Broadcast the message to all connected clients
                                    for (PrintWriter client : clients) {
                                        client.println("Message from " + clientSocket + ": " + message);
                                    }
                                }
                            }
                        } else {
                            // Reject the connection request
                            out.println("REJECTED");
                        }

                        // Client disconnected
                        System.out.println("Client disconnected: " + clientSocket);
                        clients.remove(out);

                        // Close the connections
                        out.close();
                        in.close();
                        clientSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                clientThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



//    public static void main(String[] args) {
//        Newsroom2Server server = new Newsroom2Server();
//        server.start();
//    }
//    }

   

