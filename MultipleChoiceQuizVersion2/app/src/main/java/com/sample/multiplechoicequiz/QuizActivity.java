/***************************************************************************************
 * Program: MultipleChoiceQuizFinal
 * Programmer: Jayce Merinchuk
 * Date: November 30, 2018
 * Description:
 *
 * Sources:
 *      Star Picture Background:
 *      https://jooinn.com/starfield-6.html
 *
 *      Background Music:
 *      http://www.orangefreesounds.com/thinking-music-full-version/
 *
 ***************************************************************************************/
package com.sample.multiplechoicequiz;

// Import Libraries for use in the Program
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/***************************************************************************************
 * Class: QuizActivity - Main Activity of the Program
 ***************************************************************************************/
public class QuizActivity extends AppCompatActivity {

    // GUI Elements
    private TextView mScoreView;   // view for current total score
    private TextView mQuestionView;  //current question to answer
    private Button mButtonChoice1; // multiple choice 1 for mQuestionView
    private Button mButtonChoice2; // multiple choice 2 for mQuestionView
    private Button mButtonChoice3; // multiple choice 3 for mQuestionView
    private Button mButtonChoice4; // multiple choice 4 for mQuestionView

    // Global Variables
    private String username;
    private QuestionBank mQuestionLibrary = new QuestionBank();
    private String mAnswer;  // correct answer for question in mQuestionView
    private int mScore = 0;  // current total score
    private int mQuestionsAnswered = 0; // keeps track of number of questions answered
    private int mQuestionNumber = 0; // current question number
    private int mSongPosition = 0;  // hold the song position

    // Media Player for Background music
    private MediaPlayer mediaPlayer;

    // Variables for storing data between App close and rotation
    private static final String KEY_USER = "keyuser";
    private static final String KEY_SONG = "songIndex";
    private static final String KEY_CURRENT_SCORE = "currentscore"; // Holds current Score
    private static final String KEY_QUESTION_NUMBER = "currentquestionnumber"; // Holds current question number
    private static final String KEY_QUESTIONS_ANSWERED = "currentquestionsanswered"; // Holds current number of questions answered
    private static final String KEY_SCORE = "currentscore"; // Holds current score

    /***************************************************************************************
     * Method: onCreate()
     * Description: Finds views of GUI Elements for use in the program, checks the
     * savedInstanceState, then updates the score and updates the question.
     * @param savedInstanceState
     ***************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Find GUI elements for use within the program.
        mScoreView = findViewById(R.id.score);
        mQuestionView = findViewById(R.id.question);
        mButtonChoice1 = findViewById(R.id.choice1);
        mButtonChoice2 = findViewById(R.id.choice2);
        mButtonChoice3 = findViewById(R.id.choice3);
        mButtonChoice4 = findViewById(R.id.choice4);

        // Get the username passed from Login
        Intent intent = getIntent();
        username = intent.getStringExtra(KEY_USER);

        // Create and start the music
        mediaPlayer = MediaPlayer.create(this, R.raw.thinkingmusic);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        // Check if SavedInstanceState has something in the bundle (is not null)
        if (savedInstanceState != null) {
            // Get current values saved
            mSongPosition = savedInstanceState.getInt(KEY_SONG, 0);
            mQuestionNumber = savedInstanceState.getInt(KEY_QUESTION_NUMBER, 0);
            mQuestionsAnswered = savedInstanceState.getInt(KEY_QUESTIONS_ANSWERED, 0);
            mScore = savedInstanceState.getInt(KEY_SCORE, 0);
        }

        mQuestionLibrary.initQuestions(getApplicationContext());

        // show current total score for the user
        updateScore(mScore);
        // Update the screen to the correct question
        updateQuestion();
    }

    /***************************************************************************************
     * Method: updateQuestion()
     * Description: Checks to ensure we are not trying to access a question outside the array,
     * then sets the next appropriate question to the screen, otherwise a toast is displayed
     * to advise the quiz is done.
     ***************************************************************************************/
    private void updateQuestion(){
        // check if we are not outside array bounds for questions
        if(mQuestionNumber < mQuestionLibrary.getLength()){
            // set the text for new question, and new 4 alternative to answer on four buttons
            mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
            mButtonChoice1.setText(mQuestionLibrary.getChoice(mQuestionNumber, 1));
            mButtonChoice2.setText(mQuestionLibrary.getChoice(mQuestionNumber, 2));
            mButtonChoice3.setText(mQuestionLibrary.getChoice(mQuestionNumber, 3));
            mButtonChoice4.setText(mQuestionLibrary.getChoice(mQuestionNumber,4));
            mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
       }
        else {
            Toast.makeText(QuizActivity.this, "It was the last question!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QuizActivity.this, EndGameScreen.class);
            intent.putExtra(KEY_CURRENT_SCORE, mScore); // pass the current score to new activity
            intent.putExtra(KEY_USER, username); // pass the username to new activity
            startActivity(intent);
        }
    }

    /***************************************************************************************
     * Method: updateScore()
     * Description: Shows the current total score for the user.
     * @param point
     ***************************************************************************************/
    private void updateScore(int point) {
        mScoreView.setText("" + mScore + "/" + mQuestionLibrary.getLength());
    }

    /***************************************************************************************
     * Method: onClick()
     * Description: onClick method to see which button is pressed, outputs appropriate toast,
     * updates the score, then updates the question.
     * @param view
     ***************************************************************************************/
    public void onClick(View view) {
        if (mQuestionsAnswered == mQuestionLibrary.getLength()) {
            Toast.makeText(QuizActivity.this, "You are done the Quiz!", Toast.LENGTH_SHORT).show();
        }
        else {
            // All logic for all answers buttons in one method
            Button answer = (Button) view;
            // if the answer is correct, increase the score
            if (answer.getText().equals(mAnswer)) {
                mScore++;
                Toast.makeText(QuizActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(QuizActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
            // show current total score for the user
            updateScore(mScore);
            // updates number of questions answered so we don't go beyond questions length.
            mQuestionsAnswered++;
            // Update the current question number
            mQuestionNumber++;
            // once user answer the question, we move on to the next one
            updateQuestion();
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

    /***************************************************************************************
     * Method: onSaveInstanceState()
     * Description: Save variables in the savedInstanceState Bundle to help with rotation.
     * @param savedInstanceState
     ***************************************************************************************/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_QUESTION_NUMBER, mQuestionNumber);
        savedInstanceState.putInt(KEY_QUESTIONS_ANSWERED, mQuestionsAnswered);
        savedInstanceState.putInt(KEY_SCORE, mScore);
        savedInstanceState.putInt(KEY_SONG, mSongPosition);
    }

    /***************************************************************************************
     * Method: onRestoreInstanceState()
     * Description: Restore variables from the savedInstanceState Bundle and updates the
     * scores and music.
     * @param savedInstanceState
     ***************************************************************************************/
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSongPosition = savedInstanceState.getInt(KEY_SONG, 0);
        mQuestionNumber = savedInstanceState.getInt(KEY_QUESTION_NUMBER, 0);
        mQuestionsAnswered = savedInstanceState.getInt(KEY_QUESTIONS_ANSWERED, 0);
        mScore = savedInstanceState.getInt(KEY_SCORE, 0);

        // Set TextViews with current values
        mScoreView.setText("" + mScore + "/" + mQuestionLibrary.getLength());
    }
}