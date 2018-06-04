package Client;

import java.net.InetAddress;
import java.net.Socket;

/**
 * This class represents a client
 * @author Roee Sanker & Yossi Hekter
 */
public class Client {
    /**
     * The IP address of the server to which we want to connect
     */
    private InetAddress serverIP;
    /**
     * The port of the server to connect to
     */
    private int serverPort;
    /**
     * Client strategy
     */
    private IClientStrategy clientStrategy;

    /**
     * Constructor
     * @param serverIP The IP address of the server
     * @param serverPort The port of the server
     * @param clientStrategy The Client strategy
     */
    public Client(InetAddress serverIP, int serverPort, IClientStrategy clientStrategy) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.clientStrategy = clientStrategy;
    }

    /**
     * This function starts communication with the server
     */
    public void communicateWithServer() {
        try {
            Socket theServer = new Socket(serverIP, serverPort);
            System.out.println(String.format("Client is connected to server (IP: %s, port: %s)", serverIP, serverPort));
            clientStrategy.clientStrategy(theServer.getInputStream(), theServer.getOutputStream());
            theServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
