import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class QuizServer{
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private Question currentQuestion;
    private MatchInfo currentMatch;
    private Queue<ClientHandler> waitingClients;


    public QuizServer (int port){
        try {
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
            waitingClients = new ConcurrentLinkedQueue<>();
            currentQuestion = getCurrentQuestion();
            loadQuestion();
            System.out.println("Server started");
        }
        catch (IOException e){
            System.out.println("Server failed to start");
        }
    }


    public synchronized void addClientToWaitingRoom(ClientHandler client) {
        waitingClients.offer(client);
        if (waitingClients.size() >= 2) {
            startNewMatch();
        }
    }

    private void startNewMatch() {
        if (waitingClients.size() >= 2) {
            ClientHandler player1 = waitingClients.poll();
            ClientHandler player2 = waitingClients.poll();
            player1.sendQuestion(currentQuestion);
            player2.sendQuestion(currentQuestion);
            currentMatch = new MatchInfo(currentQuestion);
            currentMatch.setPlayer1(player1.getClientId());
            currentMatch.setPlayer2(player2.getClientId());
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

    //gjort det lite krångligt med mening, men det är för att jag bara ville få klart det
    //man bör kunna koppla detta till en databas eller liknande relativt enkelt
    private void loadQuestion() {
        currentQuestion = new Question("What is the capital of Sweden?", List.of("Stockholm", "Oslo", "Gothenburg", "Malmö"), "Stockholm");
    }

    public synchronized Question getCurrentQuestion() {
        return currentQuestion;
    }

    public synchronized void receiveAnswer(String clientId, String answer) {
        if (currentMatch == null) return;

        if (currentMatch.getPlayer1Id().equals(clientId)) {
            currentMatch.setPlayer1Answer(answer);
        } else if (currentMatch.getPlayer2Id().equals(clientId)) {
            currentMatch.setPlayer2Answer(answer);
        }

        if (currentMatch.bothPlayersAnswered()) {
            String result = currentMatch.getResult();
            sendResultToClients(result, currentMatch.getPlayer1Id(), currentMatch.getPlayer2Id());
            startNewMatch();
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
    //bara anpassat för 2 spelare, snabblösning
    private void sendResultToClients(String result, String player1Id, String player2Id) {
        if ("DRAW".equals(result)) {
            sendResultToClient(player1Id, "DRAW");
            sendResultToClient(player2Id, "DRAW");
        } else if ("PLAYER1_WIN".equals(result)) {
            sendResultToClient(player1Id, "PLAYER_WIN");
            sendResultToClient(player2Id, "PLAYER_LOSE");
        } else if ("PLAYER2_WIN".equals(result)) {
            sendResultToClient(player1Id, "PLAYER_LOSE");
            sendResultToClient(player2Id, "PLAYER_WIN");
        } else if ("BOTH_LOSE".equals(result)) {
            sendResultToClient(player1Id, "BOTH_LOSE");
            sendResultToClient(player2Id, "BOTH_LOSE");
        }
    }

    public static void main(String[] args) {
        QuizServer server = new QuizServer(8080);
        server.loadQuestion();
        server.start();
    }
}