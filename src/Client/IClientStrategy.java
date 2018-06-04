package Client;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The interface of a client strategy
 * @author Roee Sanker & Yossi Hekter
 */
public interface IClientStrategy {
    /**
     * This function communicates with the server according to the client strategy
     * @param inFromServer The stream from the server to the client
     * @param outToServer The stream from the client to the server
     */
    void clientStrategy(InputStream inFromServer, OutputStream outToServer);
}
