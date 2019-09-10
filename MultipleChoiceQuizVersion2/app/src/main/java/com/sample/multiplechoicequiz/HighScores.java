/***************************************************************************************
 * Program: MultipleChoiceQuizFinal
 * Class: HighScores
 * Programmer: Jayce Merinchuk
 * Date: January 31, 2019
 * Description: Displays all high scores currently in the system.
 *
 * Sources:
 *      Diddy Kong Racing - Dino Domain.mp3
 ***************************************************************************************/
package com.sample.multiplechoicequiz;

// Import Libraries for use within the program
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/************************************************************************************
 * Class: HighScores
 * Description: Displays all current High Scores in the system
 ***********************************************************************************/
public class HighScores extends AppCompatActivity {

    // GUI Elements
    TextView txtUserScores;
    Button btnBack;

    // Global Variables
    MyDataBaseHelper myDataBaseHelper;
    private static final String DATABASE = "mydb.db";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_SONG = "songIndex";
    private int mSongPosition = 0;  // hold the song position

    // Media Player for Background music
    private MediaPlayer mediaPlayer;

    /********************************************************************************
     * Method: onCreate()
     * Description: Finds views for the program and displays all current high scores.
     *******************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        myDataBaseHelper = new MyDataBaseHelper(getApplicationContext(), DATABASE, null, DATABASE_VERSION);

        // Find views for use within the program
        txtUserScores = findViewById(R.id.txtUserScores);
        btnBack = findViewById(R.id.btnBack);

        // Print all the user scores to the Text View
        txtUserScores.setText(myDataBaseHelper.StringOfUsers());

        // Create and start the music
        mediaPlayer = MediaPlayer.create(this, R.raw.dinodomain);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        mSongPosition = 0;

        // Check if SavedInstanceState has something in the bundle (is not null)
        if (savedInstanceState != null) {
            // Get current values saved
            mSongPosition = savedInstanceState.getInt(KEY_SONG, 0);
        }

        // Get the song data passed from EndGameScreen
        Intent HSintent = getIntent();
        mSongPosition = HSintent.getIntExtra(KEY_SONG, 0);
    }

    /********************************************************************************
     * Method: onClickGoBack()
     * Description: Returns to previous screen.
     *******************************************************************************/
    public void onClickGoBack(View view) {
        Intent resultIntent = new Intent();
        mSongPosition = mediaPlayer.getCurrentPosition();
        resultIntent.putExtra(KEY_SONG, mSongPosition);
        setResult(HighScores.RESULT_OK, resultIntent);
        finish();
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
