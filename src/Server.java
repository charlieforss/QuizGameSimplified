import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    private final int PORT = 8080;

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected from: " + clientSocket.getInetAddress().getHostAddress());
                new Thread(new ClientHandler(clientSocket)).start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendQuestionToClient(){

    }

    private void recieveAnser(ClientId, answer){

    }

    private void evaluateAnswer(){

    }
}