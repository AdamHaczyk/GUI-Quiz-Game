import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                QuizGame.quizGame();

            }
        });
    }
}

class QuizGame {
//Variables first
public static int points = 0;
public static int currentQuestion = 0;
public static int qAnsweredCount = 0;
public static int qRemainingCount = Constants.MAX_QUESTION_COUNT;
public static Question [] question = new Question[Constants.MAX_QUESTION_COUNT];


public static void quizGame() {

    //DECLARATIONS OF JCOMPONENTS
    //JFrame
    JFrame gameFrame = new JFrame();
    gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    gameFrame.setSize(700, 400);
    gameFrame.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    //JPanel
    JPanel pointsPanel, picturePanel, questionPanel, answersPanel;
    pointsPanel = new JPanel();
    pointsPanel.setLayout(new BoxLayout(pointsPanel, BoxLayout.PAGE_AXIS));
    pointsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    picturePanel = new JPanel();
    picturePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    questionPanel = new JPanel();
    questionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    answersPanel = new JPanel();
    answersPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    answersPanel.setLayout(new GridLayout(3, 2));

    //ImageIcon
    ImageIcon Hubert = new ImageIcon("Hubert.jpg");

    //JLabel
    JLabel pointsLabel, qAnsweredLabel, qRemainingLabel, questionLabel, pictureLabel;
    pointsLabel = new JLabel("Your points: " + points);
    qAnsweredLabel = new JLabel("Answered questions: " + qAnsweredCount);
    qRemainingLabel = new JLabel("Remaining questions: " + qRemainingCount);
    questionLabel = new JLabel("Is this the current question?");
    pictureLabel = new JLabel(Hubert);

    //JRadioButton and ButtonGroup
    JRadioButton [] answerRadioButtons = new JRadioButton[4];

    ButtonGroup answerButtonGroup = new ButtonGroup();

    for(int i = 0 ; i <= 3 ; i++) {
        answerRadioButtons[i] = new JRadioButton("Default answer " + (i + 1));
        answerButtonGroup.add(answerRadioButtons[i]);
    }
    answerRadioButtons[0].setSelected(true);

    //JButton
    JButton quitButton, confirmAnswerButton;
    quitButton = new JButton("Quit Game");
    confirmAnswerButton = new JButton("Confirm Answer");

    //ADDING COMPONENTS TO THE PANELS
    pointsPanel.add(pointsLabel);
    pointsPanel.add(qAnsweredLabel);
    pointsPanel.add(qRemainingLabel);

    questionPanel.add(questionLabel);

    answersPanel.add(answerRadioButtons[0]);
    answersPanel.add(answerRadioButtons[1]);
    answersPanel.add(answerRadioButtons[2]);
    answersPanel.add(answerRadioButtons[3]);
    answersPanel.add(confirmAnswerButton);
    answersPanel.add(quitButton);

    picturePanel.add(pictureLabel);

    //ADDING COMPONENTS TO THE FRAME
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 1;
    c.anchor = GridBagConstraints.NORTHWEST;
    c.fill = GridBagConstraints.VERTICAL;
    gameFrame.add(pointsPanel, c);

    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    gameFrame.add(questionPanel, c);

    c.gridx = 0;
    c.gridy = 2;
    c.weightx = 1;
    c.weighty = 0;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.LAST_LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    gameFrame.add(answersPanel, c);

    c.gridx = 1;
    c.gridy = 0;
    c.weightx = 1000;
    c.weighty = 0;
    c.gridwidth = 1;
    c.anchor= GridBagConstraints.NORTHWEST;
    c.fill = GridBagConstraints.BOTH;
    gameFrame.add(picturePanel, c);

    //LOGIC OF THE GAME AND IT'S VARIABLES

    Question [] question = new Question[Constants.MAX_QUESTION_COUNT];
    question[currentQuestion] = new Question();

    String [] arr;
    arr = question[currentQuestion].getAnswers();
    for(int i = 0 ; i <= 3 ; i++) {
        answerRadioButtons[i].setText(arr[i]);
    }


    questionLabel.setText(question[currentQuestion].getQuestion());

    //JButton Action listeners declarations
    quitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int yesOrNoBox = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit game",JOptionPane.YES_NO_OPTION);
            if (yesOrNoBox == JOptionPane.YES_OPTION) {
                gameFrame.dispose();

            }
        }
    });

    confirmAnswerButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            char correctAnswer = question[currentQuestion].getCorrectAnswer();

            JRadioButton selectedButton = getSelectedButton(answerButtonGroup);

            if (selectedButton == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Please select an answer before continuing.",
                        "Please select an answer before continuing.",
                        JOptionPane.PLAIN_MESSAGE);
            }

            char selectedAnswer = parseCorrectAnswer(selectedButton.getText());


            if (selectedAnswer == correctAnswer) {

                pointsLabel.setText("Your points: " + ++points);
                qAnsweredLabel.setText("Answered questions: " + ++qAnsweredCount);
                qRemainingLabel.setText("Remaining questions: " + --qRemainingCount);

                JOptionPane.showMessageDialog(
                        null,
                        "Congratulations!. Moving to the next question.",
                        "Correct!",
                        JOptionPane.PLAIN_MESSAGE);


            }

            if (selectedAnswer != correctAnswer) {
                qAnsweredLabel.setText("Answered questions: " + ++qAnsweredCount);
                qRemainingLabel.setText("Remaining questions: " + --qRemainingCount);

                JOptionPane.showMessageDialog(
                        null,
                        "Maybe next time! Moving to the next question.",
                        "Wrong!",
                        JOptionPane.PLAIN_MESSAGE);

            }



            if(qRemainingCount == 0) {
                String gameFinishText = "You've finished the quiz." + "\nYour points:" + points;

                JOptionPane.showMessageDialog(
                        null,
                        gameFinishText,
                        "Game over",
                        JOptionPane.PLAIN_MESSAGE);

                gameFrame.dispose();
                return;

            }

            answerButtonGroup.clearSelection();

            question[++currentQuestion] = new Question();
            getNextQuestion(answerRadioButtons, questionLabel, question[currentQuestion]);
        }
    });

    gameFrame.setLocationRelativeTo(null);
    gameFrame.setVisible(true);


}
    //Below method is used to get the letter
    //corresponding to the correct answer
    public static char parseCorrectAnswer(String string) {
    char [] parsedAnswer = new char[string.length()];
    parsedAnswer = string.toCharArray();
    return parsedAnswer[0];

    }

    //Below method is used to isolate the currently selected radiobutton
    public static JRadioButton getSelectedButton(ButtonGroup buttonGroup) {
        Enumeration<AbstractButton> elements = buttonGroup.getElements();

        JRadioButton radioButton;
        while (elements.hasMoreElements()) {

            radioButton = (JRadioButton) elements.nextElement();

            if (radioButton.isSelected()) return radioButton;
        }

        return null;
    }


    public static void getNextQuestion(JRadioButton [] answerRadioButtons, JLabel questionLabel, Question question) {


        String [] arr;
        arr = question.getAnswers();
        for(int i = 0 ; i <= 3 ; i++) {
            answerRadioButtons[i].setText(arr[i]);
        }

        questionLabel.setText(question.getQuestion());

    }

}

class Question {

    private String question = "";     //stores current q
    public static String [] answer = new String[4];     //stores answers to the current q

    public static String correctAnswer = "";        //stores the correct answer to the current q

    public static int [] usedQuestions = new int[Constants.MAX_QUESTION_COUNT];    //to keep track of which q has already been used
    public static int usedQuestionCount = 0;    //to keep track of how many qs have been used

    //Below constructor loads the next question and it's corresponding
    //answer from file and parses them to Strings which can be fed to the main class
    public Question() {

        int hashtagCount = 0;         //for internal processing

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(Constants.QUESTION_FILE))) {
            String line;
            String temp = "";

            while((line = bufferedReader.readLine()) != null) {

                char [] lineArr;
                lineArr = line.toCharArray();


                if ((lineArr[0] - '0') == (usedQuestionCount + 1)) {

                    for (int i = 0; i <= line.length(); i++) {

                        if (i == 0) {                //getting the number of the question

                            usedQuestions[usedQuestionCount++] = lineArr[0];

                        }

                        if (i > 0) {                 //setting the value of main class variables

                            while (hashtagCount <= 5) {
                                temp += lineArr[i];

                                if (lineArr[i + 1] == '#') {

                                    switch (hashtagCount) {

                                        case 0:
                                            question = temp;
                                            hashtagCount++;
                                            temp = "";
                                            i++;
                                            break;

                                        case 1, 2, 3, 4:
                                            answer[hashtagCount - 1] = temp;
                                            hashtagCount++;
                                            temp = "";
                                            i++;
                                            break;

                                        case 5:
                                            correctAnswer = temp;
                                            hashtagCount++;
                                            temp = "";
                                            i++;
                                            break;

                                    }
                                }
                                i++;
                            }
                        }

                    }

                    break;
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //***Getter methods declarations
    public String getQuestion() {
        return question;
    }

    public String [] getAnswers() {
        return answer;
    }

    public char getCorrectAnswer(){

        return correctAnswer.charAt(0);
    }


}


/*
1. Build the interface of the game. That includes all the panels, buttons, icons, pictures etc. DONE
    a) Set answersPanel's layout to Grid Layout DONE
    b) Keep trying to model the 3 components on the frame DONE/COULD BE IMPROVED

2. Write the Question class DONE?
    a) This class is meant to handle questions for the Quiz Game class, which on the other hand
    is meant to handle the logic of the game.
    b) This class needs to contain all the questions and their corresponding answers and feed
    them to the Quiz Game class.
    c) The questions can be stored in .txt file to keep it simple.
    d) The answers can be stored in a .txt file too, the challenging part is making a logical
    connection between them. (c and d are both DONE by parsing lines from .txt files to Strings.)

3. Add components that enable user input. DONE
    a) Radio buttons to select answer. DONE
    b) Jbuttons to confirm answer selection and quit game. DONE
    c) Add action listeners DONE

4. Write the logic of the game 6/6 DONE
    a) Getting and setting answers in the game frame. DONE
    b) Getting and setting questions in the game frame. DONE
    c) Getting correct answer and checking if the selected answer is equal to correct one. DONE
    d) Counting points. DONE
    e) Counting used and remaining questions. DONE
    f) Quit and Confirm buttons functionality. DONE

 */