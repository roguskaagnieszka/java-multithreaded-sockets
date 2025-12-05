package chat_udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * UDP Chat Client:
 * - Performs handshake on startup
 * - Enables full duplex messaging via threads
 * - Terminates immediately on server disconnect
 */
public class ChatClientUDP {

    public static final int SERVER_PORT = 7777;
    public static final int BUFFER_SIZE = 1024;

    private static final String CONNECT_MSG = "__CONNECT__";

    private static volatile boolean clientInitiatedDisconnect = false;

    public static void main(String[] args) {

        try {

            String host = "localhost";
            if (args.length > 0) host = args[0];

            InetAddress serverAddress = InetAddress.getByName(host);

            DatagramSocket socket = new DatagramSocket();

            System.out.println("CLIENT STARTED");

            // Initial handshake with server
            send(socket, serverAddress, CONNECT_MSG);

            System.out.println("CONNECTED");
            System.out.println("CHAT STARTED");
            System.out.println("INFO: Type /quit to disconnect.");

            Scanner scanner = new Scanner(System.in);

            // Receives inbound messages from server
            Thread receiver = new Thread(() -> {

                byte[] buffer = new byte[BUFFER_SIZE];

                while (true) {

                    try {
                        DatagramPacket packet =
                                new DatagramPacket(buffer, buffer.length);

                        socket.receive(packet);

                        String msg =
                                new String(packet.getData(), 0, packet.getLength()).trim();

                        // Server initiated shutdown
                        if ("/quit".equalsIgnoreCase(msg)) {

                            System.out.println("SERVER DISCONNECTED");
                            socket.close();

                            System.out.println("CLIENT DISCONNECTED");
                            System.exit(0);
                        }

                        System.out.println("SERVER: " + msg);

                    } catch (Exception ignored) {}
                }
            });

            receiver.start();

            // Sends keyboard input to server
            Thread sender = new Thread(() -> {

                try {

                    while (!socket.isClosed()) {

                        String msg = scanner.nextLine();

                        // Client initiated disconnect
                        if ("/quit".equalsIgnoreCase(msg.trim())) {

                            clientInitiatedDisconnect = true;
                            send(socket, serverAddress, msg);

                            System.out.println("CLIENT DISCONNECTED");

                            socket.close();
                            System.exit(0);
                        }

                        send(socket, serverAddress, msg);
                    }

                } catch (Exception ignored) {}
            });

            sender.start();

            receiver.join();
            sender.join();

        } catch (Exception e) {
            System.out.println("CLIENT ERROR: " + e.getMessage());
        }
    }

    /**
     * Sends UDP datagram to server endpoint
     */
    private static void send(DatagramSocket socket,
                             InetAddress serverAddress,
                             String msg) throws Exception {

        byte[] data = msg.getBytes();

        DatagramPacket packet =
                new DatagramPacket(data, data.length,
                        serverAddress, SERVER_PORT);

        socket.send(packet);
    }
}
