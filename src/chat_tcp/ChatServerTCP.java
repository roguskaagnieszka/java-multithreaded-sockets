package chat_tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP chat server:
 * - Accepts one client connection at a time
 * - Enables full duplex communication using two threads (send / receive)
 */
public class ChatServerTCP {

    public static final int PORT = 7777;

    // Console input used by server operator
    private static final Scanner CONSOLE = new Scanner(System.in);

    // Output stream bound to current client session
    private static volatile PrintWriter activeOut = null;

    // Indicates active client connection state
    private static volatile boolean clientConnected = false;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("SERVER STARTED | Listening on port " + PORT);
            System.out.println("INFO: Type /quit to shut down the server.");

            // Thread responsible for outbound messages
            startSenderThread();

            // Main server loop – waits for client connections
            while (true) {

                System.out.println("\nWAITING FOR CLIENT...");
                Socket socket = serverSocket.accept();

                final Socket clientSocket = socket;

                // Initialize client session
                activeOut = new PrintWriter(clientSocket.getOutputStream(), true);
                clientConnected = true;

                System.out.println("CLIENT CONNECTED -> "
                        + clientSocket.getInetAddress().getHostAddress()
                        + ":" + clientSocket.getPort());

                // Thread responsible for inbound messages
                Thread receiver = new Thread(() -> {

                    try (BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()))) {

                        String msg;
                        while ((msg = in.readLine()) != null) {
                            System.out.println("CLIENT: " + msg);
                        }

                    } catch (IOException ignored) {}

                    // Client termination detected
                    System.out.println("CLIENT DISCONNECTED");
                    clientConnected = false;
                    activeOut = null;

                    try {
                        clientSocket.close();
                    } catch (IOException ignored) {}
                });

                receiver.start();
                receiver.join();
            }

        } catch (Exception e) {
            System.out.println("SERVER ERROR: " + e.getMessage());
        }
    }

    /**
     * Handles console input and forwards messages to active client
     */
    private static void startSenderThread() {

        Thread sender = new Thread(() -> {

            while (true) {

                String msg = CONSOLE.nextLine();

                // Administrative shutdown command
                if ("/quit".equalsIgnoreCase(msg.trim())) {
                    System.out.println("SERVER SHUTDOWN");
                    System.exit(0);
                }

                // Skip sending when no active client session
                if (!clientConnected || activeOut == null) {
                    System.out.println("No client connected. Waiting for client...");
                    continue;
                }

                activeOut.println(msg);
            }
        });

        sender.start();
    }
}
