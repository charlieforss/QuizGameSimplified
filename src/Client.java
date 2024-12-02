import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.List;

public class Client {
    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8080;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel questionPanel;
    private List<JButton> answerButtons;
    private JLabel questionLabel;
    private JLabel scoreLabel;

    private void createGUI() {
        frame = new JFrame("Quiz Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);
    }

    public void serverConnect() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to server!");
            System.exit(1);
        }
    }

    private void startMessageListener() {
        new Thread(() -> {
            try {
                while (true) {
                    Object message = in.readObject();
                    if (message instanceof Message) {
                        handleServerMessage((Message) message);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error in client handler: " + e.getMessage());
            }
        }).start();
    }

    private void createQuestionPanel() {
        questionPanel = new JPanel(new BorderLayout());
        ArrayList<JButton> list = new ArrayList<JButton>();
        for (JButton button : list) {
            questionPanel.add(button);
        }
    }

    public static void main(String[] args) {
        serverConnect();
        createGUI();
        startMessageListener();
    }
}
