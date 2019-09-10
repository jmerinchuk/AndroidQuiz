/*************************************************************************************
 * Program: MultipleChoiceQuizFinal
 * Class: User
 * Programmer: Jayce Merinchuk
 * Date: November 30, 2018
 * Description: This class handles users data - username and password.
 *************************************************************************************/
package com.sample.multiplechoicequiz;

/***************************************************************************************
 * Class: User - Handles creating, getting, and setting users
 ***************************************************************************************/
public class User {

    // Global Variables
    private String username;
    private String password;
    private int score;

    /***********************************************************************************
     * Method: Constructor for User() - no parameters
     * Description: Blank constructor
     ***********************************************************************************/
    public User() { }

    /***********************************************************************************
     * Method: Constructor for User() - three parameters
     * Description: Takes a username and password and score is 0 until later.
     ***********************************************************************************/
    public User(String pUser, String pPass) {
        this.username = pUser;
        this.password = pPass;
        this.score = 0;
    }

    /************************************************************************************
     * Method: getUser()
     * Description: Returns user from database
     * @return user
     ************************************************************************************/
    public String getUser() {
        return this.username;
    }

    /************************************************************************************
     * Method: setUser()
     * Description: Sets user in database
     ************************************************************************************/
    public void setUser(String pUser) { this.username = pUser; }

    /************************************************************************************
     * Method: getPass()
     * Description: Returns pass from database
     * @return password
     ************************************************************************************/
    public String getPass() {
        return this.password;
    }

    /************************************************************************************
     * Method: setPass()
     * Description: Sets password in database
     ************************************************************************************/
    public void setPass(String pPass) { this.password = pPass; }

    /************************************************************************************
     * Method: getScore()
     * Description: Returns pass from database
     * @return password
     ************************************************************************************/
    public int getScore() {
        return this.score;
    }

    /************************************************************************************
     * Method: setScore()
     * Description: Sets score in database
     ************************************************************************************/
    public void setScore(int pScore) { this.score = pScore; }
}
