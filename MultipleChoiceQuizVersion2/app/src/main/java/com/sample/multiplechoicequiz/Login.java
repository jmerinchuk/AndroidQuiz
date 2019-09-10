/*************************************************************************************
 * Program: MultipleChoiceQuizFinal
 * Class: Login
 * Programmer: Jayce Merinchuk
 * Date: November 30, 2018
 * Description: Initial Login Screen with Register and login buttons.
 *
 * Sources:
 *      Waterfall Picture:
 *      https://unsplash.com/search/photos/waterfalls
 *
 *      Waterfall Sound:
 *      Recorded by: Mike Koenig
 *      http://soundbible.com/2006-Bird-In-Rain.html
 *************************************************************************************/
package com.sample.multiplechoicequiz;

// Import Libraries for use in the Program
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/***************************************************************************************
 * Class: Login - First Activity of the Program - Login Screen
 ***************************************************************************************/
public class Login extends AppCompatActivity {

    // GUI Elements
    EditText txtUser;
    EditText txtPass;
    Button btnRegister;
    Button btnStartQuiz;

    // Global Variables
    private static final String KEY_USER = "keyuser";
    private static final String KEY_PASS = "keypass";
    private static final String KEY_SONG = "songIndex";
    private int mSongPosition = 0;  // hold the song position

    // Data Base
    MyDataBaseHelper myDataBaseHelper;
    private static final String DATABASE = "mydb.db";
    private static final int DATABASE_VERSION = 1;

    // Media Player for Background music
    private MediaPlayer mediaPlayer;

    /*************************************************************************************
     * Method: onCreate();
     * Description: Initial Screen of the program - shows a login screen with a register
     * button and login button.
     *************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        myDataBaseHelper = new MyDataBaseHelper(getApplicationContext(), DATABASE, null, DATABASE_VERSION);

        // Find GUI Views for use in the program
        txtUser = findViewById(R.id.edtxtName);
        txtPass = findViewById(R.id.edtxtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        // Create and start the music
        mediaPlayer = MediaPlayer.create(this, R.raw.waterfallsound);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        // Check if SavedInstanceState has something in the bundle (is not null)
        if (savedInstanceState != null) {
            // Get current values saved
            mSongPosition = savedInstanceState.getInt(KEY_SONG, 0);
            txtUser.setText(savedInstanceState.getString(KEY_USER, ""));
            txtPass.setText(savedInstanceState.getString(KEY_PASS, ""));
        }
    }

    /*************************************************************************************
     * Method: onClickRegisterUser()
     * Description: Ensures both boxes are filled in, then create a boolean to check if
     * the user is in the database.  If it's not found, send a toast message, else, create
     * the new user and add them to the database.
     *************************************************************************************/
    public void onClickRegisterUser(View view) {
        // Make sure the boxes aren't empty
        if (txtUser.getText().toString().equals("") || txtPass.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a Username and Password", Toast.LENGTH_LONG).show();
        }
        else {
            User tempUser;
            tempUser = myDataBaseHelper.searchUserObject(txtUser.getText().toString());

            // If the box is empty, show a toast message to state the error
            if (tempUser != null) {
                Toast.makeText(getApplicationContext(), "User " + txtUser.getText().toString() + " already exists.", Toast.LENGTH_LONG).show();
            }
            else {
                // Create the User Object
                User user = new User(txtUser.getText().toString(), txtPass.getText().toString());

                // Add user object to table
                myDataBaseHelper.addUser(user);

                // Advise the user was successfully added
                Toast.makeText(getApplicationContext(), "User " + txtUser.getText().toString() + " successfully registered", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*************************************************************************************
     * Method: onClickStartQuiz()
     * Description: Ensures both boxes are filled in, then creates a temporary user and
     * searches the database for a user with that username.  If it's not found, send a
     * toast message, else, check the password matches.
     * If it matches, start new activity and pass the user name to the next activity.
     *************************************************************************************/
    public void onClickStartQuiz(View view) {
        // If statement to ensure Username was entered into the box.
        if (txtUser.getText().toString().equals("") || txtPass.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter your UserName and Password First!", Toast.LENGTH_LONG).show();
        }
        else {
            User tempUser;
            // Check if the user already exists in the table
            tempUser = myDataBaseHelper.searchUserObject(txtUser.getText().toString());

            // If statement to ensure the user is in the Table before opening the quiz
            if (tempUser != null) {
                if (txtPass.getText().toString().equals(tempUser.getPass())) {
                    // Start the Quiz Activity and pass the user name into it.
                    Intent intent = new Intent(Login.this, QuizActivity.class);
                    intent.putExtra(KEY_USER, txtUser.getText().toString()); // pass the username to new activity
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), " Incorrect username or password", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), " Incorrect username or password", Toast.LENGTH_LONG).show();
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
        savedInstanceState.putString(KEY_USER, txtUser.getText().toString());
        savedInstanceState.putString(KEY_PASS, txtPass.getText().toString());
        savedInstanceState.putInt(KEY_SONG, mSongPosition);
    }

    /*************************************************************************************
     * Method: onRestoreInstanceState()
     * Description: Restore variables from the savedInstanceState Bundle and updates the
     * score and music.
     *************************************************************************************/
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        txtUser.setText(savedInstanceState.getString(KEY_USER, ""));
        txtPass.setText(savedInstanceState.getString(KEY_PASS, ""));
        mSongPosition = savedInstanceState.getInt(KEY_SONG, 0);
    }
}
