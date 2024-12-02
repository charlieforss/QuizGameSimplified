import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class QuizServer{
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private List<Question> questions;

    public QuizServer (int port){
        try {
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
            questions = new ArrayList<>();
            System.out.println("Server started");

        }
        catch (IOException e){
            System.out.println("Server failed to start");
        }
    }

    public void start(){
        try {
            while (true){
                Socket socket = serverSocket.accept();
                String clientId = "Client" + (clients.size() + 1);
                ClientHandler clientHandler = new ClientHandler(clientId, socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    private void loadQuestions(){
        questions.add (new Question("What is the capital of Sweden?", List.of("Stockholm", "Oslo", "Gothenburg", "Malmö", "Helsingborg"), "Stockholm"));
        questions.add (new Question("What is the capital of Norway?", List.of("Oslo", "Bergen", "Trondheim", "Stavanger", "Tromsø"), "Oslo"));
    }

    private void recieveAnwser(clientId, answer){
        System.out.println("Recieved answer from " + clientId + ": "+ answer);
    }

    private void evaluateAnswer(){

    }

    public static void main(String[] args) {
        QuizServer server = new QuizServer(8080);
        server.start();

    }
}