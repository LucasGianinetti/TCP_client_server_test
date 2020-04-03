package server;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import Utils.UpperCaseFilterWriter;

public class Server {

    static final Logger LOG = Logger.getLogger(Server.class.getName());

    private final int PORT = 3232;
    private final String WELCOME = "HELLO\n";
    private final String SUCCEED = "SUCCEED\n";

    public void start(){

        ServerSocket serverSocket = null; //Socket qui va attendre les connections clients
        Socket clientSocket = null;       //Socket qui servira de pour communiquer avec le client
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;


        try{
            LOG.log(Level.INFO, "Creating a server socket and binding it on any of the available network intefaces" +
                    " and on port {0}", new Object[]{Integer.toString(PORT)});
            serverSocket = new ServerSocket(PORT);
            logServerSocketAddress(serverSocket);

            while(true){
                LOG.log(Level.INFO, "Waiting (blocking) for connection request on {0} : {1}", new Object[]{serverSocket.getInetAddress(),Integer.toString(serverSocket.getLocalPort())});
                clientSocket = serverSocket.accept(); //appel bloquant jusqu'à la connection d'un client

                LOG.log(Level.INFO, "A client has arrived. We now have a client socket with following attributes:");
                logSocketAddress(clientSocket);

                //On connecte un reader et un writer au client afin de communiquer avec lui
                //Il existe aussi les méthodes send() et receive() spécifique aux sockets qui pourraient être utilisées
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                printWriter = new PrintWriter(new UpperCaseFilterWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

                LOG.log(Level.INFO, "Sending WELCOME message to client");
                printWriter.write(WELCOME);

                String line;
                line = bufferedReader.readLine(); //bloquant jusqu'à ce qu'une ligne soit lue.

                LOG.log(Level.INFO, "Sending back the transformed String to client");
                printWriter.write(line);

                line = bufferedReader.readLine();
                if(!line.equals(SUCCEED)){
                    LOG.log(Level.INFO, "Error while transforming and sending back client's message");
                }

                LOG.log(Level.INFO, "Cleaning up ressources...");
                clientSocket.close();
                printWriter.close();
                bufferedReader.close();
            }

        }catch(IOException ex){
            LOG.log(Level.SEVERE, ex.getMessage(),ex);
        }
    }

    /**
     * A utility to print client socket information
     * @param clientSocket the socket that we want to log
     */
    private void logSocketAddress(Socket clientSocket) {
        LOG.log(Level.INFO, "       Local IP address: {0}", new Object[]{clientSocket.getLocalAddress()});
        LOG.log(Level.INFO, "             Local port: {0}", new Object[]{Integer.toString(clientSocket.getLocalPort())});
        LOG.log(Level.INFO, "  Remote Socket address: {0}", new Object[]{clientSocket.getRemoteSocketAddress()});
        LOG.log(Level.INFO, "            Remote port: {0}", new Object[]{Integer.toString(clientSocket.getPort())});
    }


    /**
     * A utility to print server socket information
     * @param serverSocket the socket that we want to log
     */
    private void logServerSocketAddress(ServerSocket serverSocket) {
        LOG.log(Level.INFO, "       Local IP address: {0}", new Object[]{serverSocket.getLocalSocketAddress()});
        LOG.log(Level.INFO, "             Local port: {0}", new Object[]{serverSocket.getLocalPort()});
        LOG.log(Level.INFO, "               is bound: {0}", new Object[]{serverSocket.isBound()});
    }
}
