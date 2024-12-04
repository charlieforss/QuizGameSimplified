import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI extends JFrame {
    private Client client;
    private JLabel questionLabel;
    private JButton[] optionsButtons;
    private JLabel resultLabel;

    public GUI(Client client) {
        this.client = client;

        setTitle("Quiz Game Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        questionLabel = new JLabel("Question will appear here");
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2, 2));
        optionsButtons = new JButton[4];

        for (int i = 0; i < 4; i++) {
            optionsButtons[i] = new JButton();
            optionsButtons[i].addActionListener(new OptionButtonListener(i));
            optionsPanel.add(optionsButtons[i]);
        }

        add(optionsPanel, BorderLayout.CENTER);

        resultLabel = new JLabel("");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(resultLabel, BorderLayout.SOUTH);
        SwingUtilities.invokeLater(() -> client.requestQuestion());

    }

    public void displayQuestion(Question question) {
        if (question != null) {
            questionLabel.setText(question.getText());

            List<String> options = question.getOptions();
            for (int i = 0; i < optionsButtons.length; i++) {
                optionsButtons[i].setText(options.get(i));
                optionsButtons[i].setEnabled(true);
            }

            resultLabel.setText("");
        } else {
            questionLabel.setText("No more questions.");
            for (JButton button : optionsButtons) {
                button.setEnabled(false);
            }
        }
    }

    public void displayResult(String result) {
        resultLabel.setText(result);
    }

    public void displayError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class OptionButtonListener implements ActionListener {
        private final int optionIndex;

        public OptionButtonListener(int optionIndex) {
            this.optionIndex = optionIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedAnswer = optionsButtons[optionIndex].getText();
            client.sendAnswer(selectedAnswer);

            for (JButton button : optionsButtons) {
                button.setEnabled(false);
            }
        }
    }
}