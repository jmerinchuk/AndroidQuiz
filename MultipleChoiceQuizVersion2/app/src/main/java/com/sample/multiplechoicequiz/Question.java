/*************************************************************************************
 * Program: MultipleChoiceQuizFinal
 * Class: Question
 * Programmer: Jayce Merinchuk
 * Date: November 30, 2018
 * Description: This class handles creating a new Question Object
 *************************************************************************************/
package com.sample.multiplechoicequiz;

/***************************************************************************************
 * Class: Question - Creates a new Question Object
 ***************************************************************************************/
public class Question {

    // Global Variables
    private String question;
    private String[] choice = new String[4];
    private String answer;

    /***********************************************************************************
     * Method: Constructor for QuestionBank() - no parameters
     * Description: Blank constructor
     ***********************************************************************************/
    public Question() { }

    /***********************************************************************************
     * Method: Constructor for QuestionBank() - 3 parameters
     * Description: Constructor for a question and multiple choice answers
     ***********************************************************************************/
    public Question(String question, String[] choices, String answer) {
        this.question = question;
        this.choice[0] = choices[0];
        this.choice[1] = choices[1];
        this.choice[2] = choices[2];
        this.choice[3] = choices[3];
        this.answer = answer;
    }

    /************************************************************************************
     * Method: getQuestion()
     * Description: Returns question from database
     * @return
     ************************************************************************************/
    public String getQuestion() {
        return question;
    }

    /************************************************************************************
     * Method: getChoice()
     * Description: returns choices for the current question
     * @param i
     * @return choice[i]
     ***********************************************************************************/
    public String getChoice(int i) {
        return choice[i];
    }

    /************************************************************************************
     * Method: getAnswer()
     * Description: Returns the correct answer to the current question
     * @return
     ***********************************************************************************/
    public String getAnswer() {
        return answer;
    }

    /************************************************************************************
     * Method: setQuestion()
     * Description: Sets the question on the question object
     * @return
     ************************************************************************************/
    public void setQuestion(String question) {
        this.question = question;
    }

    /************************************************************************************
     * Method: setChoice()
     * Description: Sets the choice in the question object
     * @param i
     * @return choice[i]
     ***********************************************************************************/
    public void setChoice(int i, String choice) {
        this.choice[i] = choice;
    }

    /************************************************************************************
     * Method: setAnswer()
     * Description: sets the answer for the question object
     * @return
     ***********************************************************************************/
    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
