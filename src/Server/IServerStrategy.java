package Server;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The interface of a server strategy
 * @author Roee Sanker & Yossi Hekter
 */
public interface IServerStrategy {
    /**
     * This function handles a client's request according to the server strategy
     * @param inFromClient The stream from the client to the server
     * @param outToClient The stream from the server to the client
     * @param configurations Properties file
     */
    void serverStrategy(InputStream inFromClient, OutputStream outToClient, Server.Configurations configurations);
}
