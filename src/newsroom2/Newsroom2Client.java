
package newsroom2;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Newsroom2Client {
    public static void main(String[] args) {
        try {
//            Scanner s = new Scanner(System.in);
//            System.out.println("Do you want to start recieving messages:");
//            String decision=s.nextLine();
            Socket socket=null;
            PrintWriter out= null;
            
            
                // Connect to the server
             socket = new Socket("localhost", 8080);

            // Send a connection request to the server
             out = new PrintWriter(socket.getOutputStream(), true);
            out.println("CONNECT");
            
            
            

            // Receive the server's response
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();

            // Check if the connection request was accepted
            if (response.equals("ACCEPTED")) {
                System.out.println("Connected to the server. You can start receiving messages.");

                // Start a separate thread to continuously receive messages from the server
                Thread receiveThread = new Thread(() -> {
                    try {
                        String message;
                        while ((message = in.readLine()) != null) {
                            System.out.println("Received message: " + message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                receiveThread.start();

                // Read user input and send messages to the server
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                String input;
                while ((input = userInput.readLine()) != null) {
                    if (input.equalsIgnoreCase("LIST")) {
                        // Request a list of connected clients
                        out.println("LIST");
                    } else if (input.startsWith("SENDTO:")) {
                        // Send a message to specific clients
                        out.println(input);
                    } else {
                        // Send a message to all clients
                        out.println("SEND " + input);
                    }
                }

                // Close the connections
                receiveThread.interrupt();
                out.close();
                in.close();
                socket.close();
            } else {
                System.out.println("Connection request rejected by the server.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


