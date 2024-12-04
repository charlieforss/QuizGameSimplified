import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{

    private Socket socket;
    private static ObjectOutputStream out;
    private ObjectInputStream in;
    private GUI GUI;

    public Client(String serverAddress, int serverPort){
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            GUI = new GUI(this);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void Start(){
        questionListener();
        GUI.setVisible(true);
    }

    private void questionListener(){
        new Thread(() -> {
            try {
                while (true){
                    Question question = (Question) in.readObject();
                    GUI.displayQuestions(question);
                    System.out.println("Recieved question from server: " + question);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        ).start();
    }

    public static void sendAnswer(String answer){
        try {
            out.writeObject("SEND_ANSWER");
            out.writeObject(answer);
            out.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void recieveResult(String result){
        GUI.displayResault(result);
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 8080);
        client.Start();

    }

}