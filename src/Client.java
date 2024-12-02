import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{

    private final String serverAddress = "localHost";
    private final int serverPort = 8080;


    private void connectToServer(){
        try {
            Socket socket = new Socket(serverAddress, serverPort);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Can not connect");
            System.exit(1);
        }
    }

    private void sendAnswer(){

    }

    private void recieveResault(){

    }

    public static void main(String[] args) {
        Client client = new Client();
        new Client();
    }

}