/*************************************************************************************
 * Program: MultipleChoiceQuizFinal
 * Class: QuestionBank
 * Programmer: Jayce Merinchuk
 * Date: November 30, 2018
 * Description: This class handles the database info for Questions
 *************************************************************************************/
package com.sample.multiplechoicequiz;

// Import Libraries for use in the Program
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/***************************************************************************************
 * Class: QuestionBank - Helps to manage to SQLite Databases - Question Table
 ***************************************************************************************/
public class QuestionBank {

    // Global Variables
    List<Question> list = new ArrayList<>();
    MyDataBaseHelper myDataBaseHelper;

    /************************************************************************************
     * Method: getLength()
     * Description: returns the current number of questions in the program.
     * @return
     ***********************************************************************************/
    public int getLength(){
        return list.size();
    }

    /************************************************************************************
     * Method: getQuestion()
     * Description: Returns question from the ArrayList at index a.
     * @return
     ***********************************************************************************/
    public String getQuestion(int a) {
        return list.get(a).getQuestion();
    }

    /************************************************************************************
     * Method: getChoice()
     * Description: Returns a single multiple choice item for question based on array
     * index, based on number of multiple choice item in the list - 1, 2, 3 or 4 as
     * an argument.
     * @return
     ***********************************************************************************/
    public String getChoice(int index, int num) {
        return list.get(index).getChoice(num - 1);
    }

    /************************************************************************************
     * Method: getCorrectAnswer()
     * Description: Returns correct answer for the question based on array index
     * @return answer
     ***********************************************************************************/
    public String getCorrectAnswer(int a) {
        return list.get(a).getAnswer();
    }

    /************************************************************************************
     * Method: initQuestions()
     * Description: Initialize Database in case it's empty at the very beginning
     ***********************************************************************************/
    public void initQuestions(Context context) {
        myDataBaseHelper = new MyDataBaseHelper(context, "mydb.db", null, 1);
        list = myDataBaseHelper.getAllQuestionsList();

        if (list.isEmpty()) {
            myDataBaseHelper.addInitialQuestion(new Question("When did Google acquire Android?",
                    new String[]{"2003", "2004", "2005", "2006"},"2005"));
            myDataBaseHelper.addInitialQuestion(new Question("What is the name of build toolkit for Android Studio?",
                    new String[]{"JVM", "Gradle", "Dalvik", "HAXM"},"Gradle"));
            myDataBaseHelper.addInitialQuestion(new Question("What widget can replace any use of radio buttons?",
                    new String[]{"Toggle", "Spinner", "Switch", "CheckBox"},"Spinner"));
            myDataBaseHelper.addInitialQuestion(new Question("What is the most recent Android OS?",
                    new String[]{"Oreo", "Nougat", "Pie", "Octopus"},"Pie"));
            myDataBaseHelper.addInitialQuestion(new Question("Which screen do you use to modify layouts?",
                    new String[]{"MainActivity.java", "R.java", "Activity.xml", "strings.xml"},"Activity.xml"));
            myDataBaseHelper.addInitialQuestion(new Question("Which attribute allows user to press buttons?",
                    new String[]{"onClick", "Editable", "Input Method", "Tag"},"onClick"));
            myDataBaseHelper.addInitialQuestion(new Question("This method is called when the user closes the app completely.",
                    new String[]{"onSaveInstanceState", "onPause", "onResume", "onDestroy"},"onDestroy"));
            myDataBaseHelper.addInitialQuestion(new Question("Android application implements the principle of ________",
                    new String[]{"least privileges", "most privileges", "unique privileges", "none of the above"},"least privileges"));
            myDataBaseHelper.addInitialQuestion(new Question("What is a widget in Android?",
                    new String[]{"reusable GUI element", "Layout for Activity", "name for a device", "an SQL Helper"},"reusable GUI element"));

            list = myDataBaseHelper.getAllQuestionsList();
        }
    }
}