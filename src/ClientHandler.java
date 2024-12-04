import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private QuizServer server;
    private String clientId;
    private Question currentQuestion;

    public ClientHandler(Socket socket, QuizServer server, String clientId) {
        this.clientSocket = socket;
        this.server = server;
        this.clientId = clientId;
        try {
            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.outputStream.flush();
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String action = (String) inputStream.readObject();
                if ("GET_QUESTION".equals(action)) {
                    sendQuestion();
                } else if ("SEND_ANSWER".equals(action)) {
                    String answer = (String) inputStream.readObject();
                    server.receiveAnswer(clientId, answer, currentQuestion);
                }
            }
        } catch (EOFException e) {
            System.out.println("Client " + clientId + " disconnected.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void sendQuestion() {
        try {
            currentQuestion = server.getNextQuestion();
            if (currentQuestion != null) {
                outputStream.writeObject(currentQuestion);
                outputStream.flush();
            } else {
                outputStream.writeObject("No more questions available.");
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendResult(String result) {
        try {
            outputStream.writeObject(result);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cleanup method to close connections
    private void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
    }
}