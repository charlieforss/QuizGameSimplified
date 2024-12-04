import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QuizServer{
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private Queue<Question> questions;

    public QuizServer (int port){
        try {
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
            questions = new ConcurrentLinkedQueue<>();
            loadQuestions();
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
                ClientHandler clientHandler = new ClientHandler(socket, this, clientId);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
                System.out.println("Client connected: " + clientId);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    private void loadQuestions(){
        questions.add (new Question("What is the capital of Sweden?", List.of("Stockholm", "Oslo", "Gothenburg", "Malmö"), "Stockholm"));
        questions.add (new Question("What is the capital of Norway?", List.of("Oslo", "Bergen", "Trondheim", "Stavanger"), "Oslo"));
        questions.add (new Question("What is the capital of Denmark?", List.of("Copenhagen", "Aarhus", "Odense", "Aalborg"), "Copenhagen"));
        questions.add (new Question("What is the capital of Germany?", List.of("Berlin", "Munich", "Hamburg", "Cologne"), "Berlin"));
    }

    public Question getNextQuestion() {
        return questions.poll();
    }

    public void receiveAnswer(String clientId, String answer, Question currentQuestion){
        String result;
        if (currentQuestion != null) {
            if (currentQuestion.isCorrectAnswer(answer)) {
                result = "VICTORY";
                System.out.println("Player " + clientId + " won the game!");
            } else {
                result = "DEFEAT";
                System.out.println("Player " + clientId + " lost the game!");
            }

            sendResultToClient(clientId, result);
        }
    }

    private void sendResultToClient(String clientId, String result) {
        for (ClientHandler client : clients) {
            if (client.getClientId().equals(clientId)) {
                client.sendResult(result);
                break;
            }
        }
    }

    public static void main(String[] args) {
        QuizServer server = new QuizServer(8080);
        server.loadQuestions();
        server.start();
    }
}