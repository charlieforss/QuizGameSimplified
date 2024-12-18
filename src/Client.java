/*
Utgick en del från första projectet vilket har gjort att jag kanske har mer
klasser än vad som behövs när jag hade kunnat smälla in allt i 3 klasser.
Jag valde dock att fortsätta med flera för jag tycker det blir lättare att läsa.

Detta har också gjort så jag tagit inspiration från den andra projektet och därmed
är detta halvbyggt för att lätt kunna utöka och förbättra med t.ex. en databas eller
ett dokument med frågor.

Ville få klart detta inför inlämningen men det kommer vara något jag sitter och gör på
fritiden framöver så därför ligger en del onödig kod kvar så ber om ursäkt för det.

Denna kod fungerar enligt beskrivingen men som sagt är inte helt nöjd själv och ska sitta och
jobba mer på detta då threads och server/clients känns som att jag verkligen måste ha stenkoll på.

om något är oklart så är det bara att fråga!
 */

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
            System.out.println("Attempting to connect to server at " + serverAddress + ":" + port);
            socket = new Socket(serverAddress, port);
            System.out.println("Connected to server successfully");
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            GUI = new GUI(this);
            GUI.setVisible(true);
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to connect to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void start() {
        waitForMatch();
        listenForQuestions();
        requestQuestion();
    }

    public void waitForMatch() {
        SwingUtilities.invokeLater(() -> {
            GUI.displayWaitingMessage("Waiting for another player to join...");
            GUI.disableButtons();
        });
    }

    //missledande namn, tar även in svaren och uppdaterar GUI med resultatet
    //vill dock inte gå in och ändra pga rädd för att det kommer stöka till det
    //i början lyssnade den bara efter frågor men märkte att det va enklast att
    //lägga till svaren här också

    //Invoke later används för att det är en swing inte är trådsäkert.
    //Den används för att lägga till saker i en event queue som körs i bakgrunden
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
                        if (result.equals("PLAYER_WIN"))
                            SwingUtilities.invokeLater(() -> GUI.displayResult("PLAYER_WIN"));
                        else if (result.equals("PLAYER_LOSE"))
                            SwingUtilities.invokeLater(() -> GUI.displayResult("PLAYER_LOSE"));
                        else if (result.equals("BOTH_LOSE"))
                            SwingUtilities.invokeLater(() -> GUI.displayResult("BOTH_LOSE"));
                        else if (result.equals("DRAW"))
                            SwingUtilities.invokeLater(() -> GUI.displayResult("DRAW"));
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    GUI.displayError("Disconnected from server.");
                    GUI.disableButtons();
                });
            }
        }).start();
    }

    //halv använd, ligger kvar en del från tidigare variationer när man vill
    //ha in flera frågor, låter den ligga kvar för framtida förbättringar
    public void requestQuestion() {
        try {
            outputStream.writeObject("GET_QUESTION");
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            GUI.displayError("Failed to request question.");
        }
    }

    //ej använd, dock ville låta ligga kvar pga framtida förbättringar
    public void getResult() {
        try {
            outputStream.writeObject("GET_RESULT");
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            GUI.displayError("Failed to request result.");
        }
    }

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