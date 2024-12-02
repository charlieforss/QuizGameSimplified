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

    private void handleServerMessage(Message message) {
        try {
            switch (message.getType()) {
                case "WELCOME":
                break;

                case "GAME_START":
                break;

                case "QUESTION":
                break;

                case "ANSWER_RESULT":
                break;

                case "GAME_END":
                break;


        }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error processing server message: " + e.getMessage());
        }
    }

    private void createQuestionPanel() {
        questionPanel = new JPanel(new BorderLayout());
        ArrayList<JButton> list = new ArrayList<JButton>();
        for (JButton button : list) {
            questionPanel.add(button);
        }
        mainPanel.add(questionPanel, BorderLayout.CENTER);
        questionPanel.setLayout(new GridLayout(2, 2, 10, 10));
        questionPanel.add(questionLabel);
    }

    public static void main(String[] args) {
        serverConnect();
        createGUI();
        startMessageListener();
    }
}
