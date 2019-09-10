/*************************************************************************************
 * Program: MultipleChoiceQuizFinal
 * Class: EndGameScreen
 * Programmer: Jayce Merinchuk
 * Date: November 30, 2018
 * Description: This class handles the Activity screen showing all user high scores.
 *************************************************************************************/
package com.sample.multiplechoicequiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***************************************************************************************
 * Class: MyDataBaseHelper - Helps to manage to SQLite Databases
 ***************************************************************************************/
public class MyDataBaseHelper extends SQLiteOpenHelper {

    // Global Variables
    private static final String TABLE_QUESTION = "QuestionBank";
    private static final String TABLE_USER = "UserBank";

    // Fields for the Question table
    private static final String KEY_ID = "id";
    private static final String QUESTION = "question";
    private static final String CHOICE1 = "choice1";
    private static final String CHOICE2 = "choice2";
    private static final String CHOICE3 = "choice3";
    private static final String CHOICE4 = "choice4";
    private static final String ANSWER =  "answer";

    // Fields for the User Table
    private static final String USER = "user";
    private static final String PASS = "pass";
    private static final String SCORE = "score";

    // Create the Question Table
    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE "
            + TABLE_QUESTION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + QUESTION
            + " TEXT, " + CHOICE1 + " TEXT, " + CHOICE2 + " TEXT, " + CHOICE3 + " TEXT, "
            + CHOICE4 + " TEXT, " + ANSWER + " TEXT);";

    // Create the User Table
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER
            + " TEXT, " + PASS + " TEXT, " + SCORE + " TEXT);";

    /*************************************************************************************
     * Method: MyDataBaseHelper() - Constructor
     * Description: Constructor with several parameters to initialize the DataBaseHelper - QUESTION
     * @param context
     * @param name
     * @param factory
     * @param version
     ************************************************************************************/
    public MyDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*************************************************************************************
     * Method: onCreate()
     * Description: Runs when the program starts up this class.
     * @param db
     ************************************************************************************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTION);  // Creates the Question Table
        db.execSQL(CREATE_TABLE_USER);      // Creates the User Table
    }

    /*************************************************************************************
     * Method: onUpgrade()
     * Description: Drops the Question Table if it already exists.
     * Drops the User table if it already exists.
     * @param db
     * @param oldVersion
     * @param newVersion
     ************************************************************************************/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table if it already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    /**************************************************************************************
     * Method: addInitialQuestion()
     * Description: If the question database starts empty, The questions are added to the DB.
     * @param question
     * @return insert
     *************************************************************************************/
    public void addInitialQuestion(Question question) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(QUESTION, question.getQuestion());
        values.put(CHOICE1, question.getChoice(0));
        values.put(CHOICE2, question.getChoice(1));
        values.put(CHOICE3, question.getChoice(2));
        values.put(CHOICE4, question.getChoice(3));
        values.put(ANSWER, question.getAnswer());

        // Insert the new row to the table.
        db.insert(TABLE_QUESTION, null, values);
    }

    /**************************************************************************************
     * Method: getAllQuestionsList()
     * Description: Gets all questions from the Question Table using SQL Stateemnts.
     *************************************************************************************/
    public List<Question> getAllQuestionsList() {
        List<Question> questionArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Loop through all records
        if (c.moveToFirst()) {
            do {
                Question question = new Question();

                String questText = c.getString(c.getColumnIndex(QUESTION));
                question.setQuestion(questText);

                String choice1Text = c.getString(c.getColumnIndex(CHOICE1));
                question.setChoice(0, choice1Text);

                String choice2Text = c.getString(c.getColumnIndex(CHOICE2));
                question.setChoice(1, choice2Text);

                String choice3Text = c.getString(c.getColumnIndex(CHOICE3));
                question.setChoice(2, choice3Text);

                String choice4Text = c.getString(c.getColumnIndex(CHOICE4));
                question.setChoice(3, choice4Text);

                String answerText = c.getString(c.getColumnIndex(ANSWER));
                question.setAnswer(answerText);

                questionArrayList.add(question);
            }
            while (c.moveToNext());
            Collections.shuffle(questionArrayList);
        }
        return questionArrayList;
    }

    /**************************************************************************************
     * Method: AddUser()
     * Description: Gets all questions from the Question Table using SQL Stateemnts.
     *************************************************************************************/
    public long addUser(User user) {
        // Make database writeable so we can add user/pass
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Add Values
        values.put(USER, user.getUser());
        values.put(PASS, user.getPass());
        values.put(SCORE,0);

        // Insert the new row to the table.
        long insert = db.insert(TABLE_USER, null, values);
        return insert;
    }


    /**************************************************************************************
     * Method: searchUserObject()
     * Description: Searches the user table to see if a user already exists and returns
     * the object if so, otherwise it returns a null object.
     * @return tempUser
     * @return null
     *************************************************************************************/
    public User searchUserObject(String uName) {
        User tempUser = new User();

        String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + USER + " = \"" + uName + "\"";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            tempUser.setUser(c.getString(c.getColumnIndex(USER)));
            tempUser.setPass(c.getString(c.getColumnIndex(PASS)));
            tempUser.setScore(c.getInt(c.getColumnIndex(SCORE)));
            return tempUser;
        }
        return null;
    }

    /**************************************************************************************
     * Method: StringOfUsers()
     * Description: Searches and adds all users to a string and returns it.
     * @return userString
     *************************************************************************************/
    public String StringOfUsers() {
        String users = "";
        String selectQuery = "SELECT * FROM " + TABLE_USER + " ORDER BY " + SCORE + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                users = users + ((c.getString(c.getColumnIndex(USER))) + ": " + c.getInt(c.getColumnIndex(SCORE)) + "\n");
            }
            while (c.moveToNext());
        }
        return users;
    }

    /**************************************************************************************
     * Method: ModifyScore()
     * Description: Modifies score of user in database.
     * @return userString
     *************************************************************************************/
    public void ModifyScore(User tempUser, int pScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCORE, pScore);
        db.update(TABLE_USER, values, "user= \"" + tempUser.getUser() + "\"", null);
    }
}
