package chat_udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * UDP Chat Server:
 * - Maintains logical client session using handshake packets
 * - Enables full duplex communication via threads
 */
public class ChatServerUDP {

    public static final int PORT = 7777;
    private static final int BUFFER_SIZE = 1024;

    private static final String CONNECT_MSG = "__CONNECT__";

    // Server console input
    private static final Scanner CONSOLE = new Scanner(System.in);

    // Active client endpoint
    private static volatile InetAddress clientAddress = null;
    private static volatile int clientPort = -1;

    // Logical client session state
    private static volatile boolean clientConnected = false;

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket(PORT)) {

            System.out.println("SERVER STARTED | UDP listening on port " + PORT);
            System.out.println("INFO: Waiting for client connection.");

            startReceiverThread(socket);
            startSenderThread(socket);

            while (true) Thread.sleep(1000);

        } catch (Exception e) {
            System.out.println("SERVER ERROR: " + e.getMessage());
        }
    }

    /**
     * Receives datagrams and maintains client session state
     */
    private static void startReceiverThread(DatagramSocket socket) {

        Thread receiver = new Thread(() -> {

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {

                try {
                    DatagramPacket packet =
                            new DatagramPacket(buffer, buffer.length);

                    // Wait for incoming datagram
                    socket.receive(packet);

                    clientAddress = packet.getAddress();
                    clientPort = packet.getPort();

                    String msg =
                            new String(packet.getData(), 0, packet.getLength()).trim();

                    // Handshake packet from new client
                    if (CONNECT_MSG.equals(msg)) {

                        if (!clientConnected) {
                            clientConnected = true;

                            System.out.println("CLIENT CONNECTED -> "
                                    + clientAddress.getHostAddress()
                                    + ":" + clientPort);
                        }

                        continue;
                    }

                    // Client initiated disconnect
                    if ("/quit".equalsIgnoreCase(msg)) {

                        clientConnected = false;
                        clientAddress = null;
                        clientPort = -1;

                        System.out.println("CLIENT DISCONNECTED");
                        continue;
                    }

                    // Regular chat message
                    System.out.println("CLIENT: " + msg);

                } catch (Exception ignored) {}
            }
        });

        receiver.start();
    }

    /**
     * Sends server console input to connected client
     */
    private static void startSenderThread(DatagramSocket socket) {

        Thread sender = new Thread(() -> {

            while (true) {

                try {
                    String msg = CONSOLE.nextLine();

                    // Server shutdown command
                    if ("/quit".equalsIgnoreCase(msg.trim())) {

                        if (clientConnected) {
                            send(socket, msg);
                        }

                        System.out.println("SERVER SHUTDOWN");
                        System.exit(0);
                    }

                    // Prevent sending when no client session exists
                    if (!clientConnected) {

                        System.out.println("No client connected. Waiting for client...");
                        continue;
                    }

                    send(socket, msg);

                } catch (Exception ignored) {}
            }
        });

        sender.start();
    }

    /**
     * Sends UDP packet to active client
     */
    private static void send(DatagramSocket socket,
                             String msg) throws Exception {

        byte[] data = msg.getBytes();

        DatagramPacket packet =
                new DatagramPacket(data, data.length,
                        clientAddress, clientPort);

        socket.send(packet);
    }
}
