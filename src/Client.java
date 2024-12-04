import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private GUI GUI;

    public Client(String serverAddress, int port) {
        try {
            // Connect to the server
            socket = new Socket(serverAddress, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            GUI = new GUI(this);
            GUI.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to connect to server", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void start() {
        listenForQuestions();
        requestQuestion();
    }

    private void listenForQuestions() {
        new Thread(() -> {
            try {
                while (true) {
                    Object receivedObject = inputStream.readObject();
                    if (receivedObject instanceof Question) {
                        Question question = (Question) receivedObject;
                        SwingUtilities.invokeLater(() -> GUI.displayQuestion(question));
                    } else if (receivedObject instanceof String) {
                        String result = (String) receivedObject;
                        SwingUtilities.invokeLater(() -> GUI.displayResult(result));
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> GUI.displayError("Disconnected from server."));
                //swing ej trådsäker, invoke later, updaterar gui i ordning,
            }
        }).start();
    }

    public void requestQuestion() {
        try {
            outputStream.writeObject("GET_QUESTION");
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            GUI.displayError("Failed to request question.");
        }
    }

    // Send answer to the server
    public void sendAnswer(String answer) {
        try {
            outputStream.writeObject("SEND_ANSWER");
            outputStream.writeObject(answer);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            GUI.displayError("Failed to send answer.");
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 8080);
        client.start();
    }
}