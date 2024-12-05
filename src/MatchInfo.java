public class MatchInfo {
    private String player1Id;
    private String player1Answer;
    private String player2Id;
    private String player2Answer;
    private Question currentQuestion;

    public MatchInfo(Question question) {
        this.currentQuestion = question;
    }

    public void setPlayer1(String playerId) {
        this.player1Id = playerId;
    }

    public void setPlayer2(String playerId) {
        this.player2Id = playerId;
    }

    public void setPlayer1Answer(String answer) {
        this.player1Answer = answer;
    }

    public void setPlayer2Answer(String answer) {
        this.player2Answer = answer;
    }

    public boolean bothPlayersAnswered() {
        return player1Answer != null && player2Answer != null;
    }

    public String getPlayer1Id() {
        return player1Id;
    }

    public String getPlayer2Id() {
        return player2Id;
    }

    public String getResult() {
        int player1Correct = currentQuestion.isCorrectAnswer(player1Answer);
        int player2Correct = currentQuestion.isCorrectAnswer(player2Answer);

        if (player1Correct == 1 && player2Correct == 1) {
            return "DRAW";
        } else if (player1Correct == 1 && player2Correct == 0) {
            return "PLAYER1_WIN";
        } else if (player1Correct == 0 && player2Correct == 1) {
            return "PLAYER2_WIN";
        } else {
            return "BOTH_LOSE";
        }
    }
}