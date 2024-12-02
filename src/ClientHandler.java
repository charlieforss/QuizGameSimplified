import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Server server;
    private String clientId;


    private void handleClient(Socket socket, Server server, String clientId){
        this.clientSocket = socket;
        this.server = server;
        this.clientId = clientId;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("Error in client handler: " + e.getMessage());
        }

    }

    private void sendQuestion(){

    }

    private void recieveAnswer(){

    }

    @Override
    public void run() {
        handleClient();
    }
}