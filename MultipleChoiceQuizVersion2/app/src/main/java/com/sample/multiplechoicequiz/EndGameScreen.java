/*************************************************************************************
 * Program: MultipleChoiceQuizFinal
 * Class: EndGameScreen
 * Programmer: Jayce Merinchuk
 * Date: November 30, 2018
 * Description: This class handles the Activity screen showing options to restart,
 * check highscores, or log out.
 *
 * Sources:
 *      Diddy Kong Racing - Dino Domain.mp3
 *************************************************************************************/
package com.sample.multiplechoicequiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/***************************************************************************************
 * Class: EndGameScreen - Ending Activity of the Program
 ***************************************************************************************/
public class EndGameScreen extends AppCompatActivity {

    // GUI Elements
    TextView txtCurrentScore;
    TextView txtHighestScore;

    // Global Variables
    private String username;
    private String highScoreName;
    private int score;
    private int highScore;
    private static final String KEY_SONG = "songIndex";
    private int mSongPosition = 0;  // hold the song position

    // Shared Preferences editor
    SharedPreferences mypref;
    SharedPreferences.Editor editor;

    // Keys for shared Preferences
    private static final String KEY_USER = "keyuser";
    private static final String KEY_HIGH_SCORE_USER = "keyuserhighscore";
    private static final String KEY_CURRENT_SCORE = "currentscore";
    private static final String KEY_HIGH_SCORE = "highscore";

    // Data Base Helper
    MyDataBaseHelper myDataBaseHelper;
    private static final String DATABASE = "mydb.db";
    private static final int DATABASE_VERSION = 1;

    // Media Player for Background music
    private MediaPlayer mediaPlayer;

    /************************************************************************************
     * Method: onCreate()
     * Description: Initializes the HighestScore Activity with GUI elements, gets the
     * Intent extra (current score).
     ************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game_screen);
        myDataBaseHelper = new MyDataBaseHelper(getApplicationContext(), DATABASE, null, DATABASE_VERSION);

        // Create, start the music, and loop it
        mediaPlayer = MediaPlayer.create(this, R.raw.dinodomain);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        // Check if SavedInstanceState has something in the bundle (is not null)
        if (savedInstanceState != null) {
            // Get current values saved
            mSongPosition = savedInstanceState.getInt(KEY_SONG, 0);
        }

        // Get the username passed from Login
        Intent intent = getIntent();
        score = intent.getIntExtra(KEY_CURRENT_SCORE, 0);
        username = intent.getStringExtra(KEY_USER);

        // Find GUI elements for use in the program
        txtCurrentScore = findViewById(R.id.txtCurrentScore);
        txtHighestScore = findViewById(R.id.txtHighScore);

        // Use Shared Preferences to get the best score achieved
        mypref = getPreferences(MODE_PRIVATE);
        highScore = mypref.getInt(KEY_HIGH_SCORE, 0);
        highScoreName = mypref.getString(KEY_HIGH_SCORE_USER, "");

        txtCurrentScore.setText("Your Score: " + score);

        // If statement to see if current score is better than high score
        if (highScore >= score) {
            txtHighestScore.setText("Highest Score: \n" +
            "User: " + highScoreName + ":  " + highScore);
        }
        else {
            txtHighestScore.setText("New Highest Score: \n" +
                    "User: " + username + ": " + score);

            // Open the shared preferences editor and save the new high score
            editor = mypref.edit();
            editor.putString(KEY_HIGH_SCORE_USER, username);
            editor.putInt(KEY_HIGH_SCORE, score);
            editor.commit();
        }

        // Get the User from the SQLiteDatabase
        User tempUser;
        tempUser = myDataBaseHelper.searchUserObject(username);

        // If statement to make sure we found the right User
        if (tempUser != null) {
            // Set users score in the Database
            tempUser.setScore(score);
            myDataBaseHelper.ModifyScore(tempUser,score);
        }
        else {
            Toast.makeText(this, "Could not update highscore", Toast.LENGTH_LONG).show();
        }
    }

    /************************************************************************************
     * Method: onClickPlayAgain()
     * Description: Restarts the quiz portion when the button is pressed.
     ************************************************************************************/
    public void onClickPlayAgain(View view) {
        Intent intent = new Intent(EndGameScreen.this, QuizActivity.class);
        intent.putExtra(KEY_USER, username); // pass the username to new activity
        startActivity(intent);
    }

    /************************************************************************************
     * Method: onClickLogOut()
     * Description: Returns to Opening Log In Screen
     ************************************************************************************/
    public void onClickLogOut(View view) {
        Intent intent = new Intent(EndGameScreen.this, Login.class);
        startActivity(intent);
    }

    /************************************************************************************
     * Method: onClickHighScores()
     * Description: Moves to view the High Scores Screen
     ************************************************************************************/
    public void onClickHighScores(View view) {
        Intent HSintent = new Intent(EndGameScreen.this, HighScores.class);
        mSongPosition = mediaPlayer.getCurrentPosition();
        HSintent.putExtra(KEY_SONG, mSongPosition);
        startActivityForResult(HSintent, 1);
    }

    /************************************************************************************
     * Method: onActivityResult()
     * Description: Moves song to current position from Activity
     ************************************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == HighScores.RESULT_OK){
                mSongPosition = data.getIntExtra(KEY_SONG, 0);
                mediaPlayer.seekTo(mSongPosition);
            }
            if (resultCode == HighScores.RESULT_CANCELED) {
                mSongPosition = data.getIntExtra(KEY_SONG, 0);
                mediaPlayer.seekTo(mSongPosition);
            }
        }
    }

    /*************************************************************************
     * Method: onResume()
     * Description:  Overridden method called by OS when the App is
     * in the foreground. Use for starting music.
     ************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
        mediaPlayer.seekTo(mSongPosition);
    }

    /*************************************************************************
     * Method: onPause()
     * Description:  Overridden method called by OS when the App is paused.
     * Used for pausing music.
     ************************************************************************/
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        mSongPosition = mediaPlayer.getCurrentPosition();
    }

    /*************************************************************************
     * Method: onDestroy
     * Description:  Overridden method called by OS when the App is destroyed.
     * Used for stopping music.
     ************************************************************************/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    /*************************************************************************************
     * Method: onSaveInstanceState()
     * Description: Save variables in the savedInstanceState Bundle to help with rotation.
     *************************************************************************************/
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_SONG, mSongPosition);
    }

    /*************************************************************************************
     * Method: onRestoreInstanceState()
     * Description: Restore variables from the savedInstanceState Bundle and updates the
     * song position.
     *************************************************************************************/
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSongPosition = savedInstanceState.getInt(KEY_SONG, 0);
    }
}