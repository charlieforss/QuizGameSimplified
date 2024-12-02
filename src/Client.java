import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ClientGUI ClientGUI

    public Client(String serverAddress, int serverPort){
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            CLientGUI = new ClientGUI(this);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void Start(){
        questionListener();
        ClientGUI.setVisible(true);
    }

    private void questionListener(){
        new Thread(() -> {
            try {
                while (true){
                    Question question = (Question) in.readObject();
                    System.out.println("Recieved question from server: " + question);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        ).start();
    }

    private void sendAnswer(){
        try {
            out.writeObject("Answer");
            out.writeObject("Correct answer");
            out.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void recieveResault(){
        try {
            String resault = (String) in.readObject();
            System.out.println("Recieved result from server: " + resault);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 8080);
        client.Start();

    }

}