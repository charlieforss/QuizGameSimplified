import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI extends JFrame {

    private Client client;
    private JLabel questionLabel;
    private JButton[] optionButtons;
    private JLabel resultLabel;

    public GUI(Client client){
        this.client = client;

        setTitle("FIGHT");
        setSize(400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        questionLabel = new JLabel("Question here!");
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2,2));
        optionButtons = new JButton[4];

        for (int i = 0; i < 4; i++){
            optionButtons[i] = new JButton();
            optionButtons[i].addActionListener(new OptionButtonListener(i));
            optionsPanel.add(optionButtons[i]);
        }

        add(optionsPanel, BorderLayout.CENTER);

        resultLabel = new JLabel("");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(resultLabel, BorderLayout.SOUTH);

    }

    public void displayQuestions(Question question){
        questionLabel.setText(question.getText());

        List<String> options = question.getOptions();
        for (int i = 0; i < optionButtons.length; i++){
            optionButtons[i].setText(options.get(i));
        }
    }

    public void displayResault(String result){
        resultLabel.setText(result);
    }


    private class OptionButtonListener implements ActionListener{
        private int optionIndex;

        public OptionButtonListener (int  optionIndex){
            this.optionIndex = optionIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            String selectedAnswer = optionButtons[optionIndex].getText();
            Client.sendAnswer(selectedAnswer);

        }
    }
}
