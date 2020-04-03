package client;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private final int PORT = 3232;
    private final String WELCOME = "HELLO";
    private final String SUCCEED = "SUCCEED";
    private final String testMessage = "ThIS is a Tet";
    static final Logger LOG = Logger.getLogger(Client.class.getName());

    private Socket socket;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;

    public void start(){
        try{
            socket = new Socket("localhost",PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line;
            line = reader.readLine();
            if(!line.equals(WELCOME)){
                LOG.log(Level.SEVERE, "Incorrect greeting from server, closing connexion");
                throw new IOException("Incorrect greeting from server");
            }
            LOG.log(Level.INFO, "Received WELCOME message from server, sending testMessage for transformation");
            writer.write(testMessage + "\n");
            writer.flush();

            line = reader.readLine();
            if(!line.equals(testMessage.toUpperCase())){
                writer.write("Incorrect transformation\n");
                LOG.log(Level.INFO, "Server didnt transform testMessage correctly");
            }else{
                writer.write(SUCCEED + "\n");
                LOG.log(Level.INFO,"Server did transform testMessage correctly");
            }
            writer.flush();

            LOG.log(Level.INFO, "Cleaning up ressources...");
            writer.close();
            reader.close();
            socket.close();

        }catch(IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }finally{
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex1) {
                    LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex1) {
                    LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex1) {
                    LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                }
            }
        }
    }


}
