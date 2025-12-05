package chat_tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP chat client:
 * - Connects to server
 * - Runs independent send / receive threads
 * - Distinguishes disconnect initiator
 */
public class ChatClientTCP {

    public static final int PORT = 7777;

    // Tracks if disconnection was initiated locally
    private static volatile boolean clientInitiatedDisconnect = false;

    public static void main(String[] args) {

        try {

            String host = "localhost";
            if (args.length > 0) host = args[0];

            System.out.println("CONNECTING TO SERVER...");

            Socket socket = new Socket(InetAddress.getByName(host), PORT);
            final Socket clientSocket = socket;

            System.out.println("CONNECTED");
            System.out.println("CHAT STARTED");
            System.out.println("INFO: Type /quit to disconnect from server.");

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            // Thread responsible for inbound messages
            Thread receiver = new Thread(() -> {

                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println("SERVER: " + msg);
                    }

                } catch (IOException ignored) {}

                // Remote disconnect detection
                if (!clientInitiatedDisconnect) {
                    System.out.println("SERVER DISCONNECTED");
                }

                try {
                    clientSocket.close();
                } catch (IOException ignored) {}
            });

            receiver.start();

            // Thread responsible for outbound messages
            Thread sender = new Thread(() -> {

                try {

                    while (!clientSocket.isClosed()) {

                        String msg = scanner.nextLine();

                        // Local disconnect command
                        if ("/quit".equalsIgnoreCase(msg.trim())) {
                            clientInitiatedDisconnect = true;
                            clientSocket.close();
                            break;
                        }

                        out.println(msg);
                    }

                } catch (Exception ignored) {}
            });

            sender.start();

            receiver.join();
            sender.join();

            // Final termination confirmation
            System.out.println("CLIENT DISCONNECTED");

        } catch (IOException e) {

            // Server unavailable at startup
            System.out.println("ERROR: Server is not running. Start server first.");

        } catch (InterruptedException ignored) {}
    }
}
