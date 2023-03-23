package com.example.hellokittyquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.example.hellokittyquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity";
//initialize player score to 0
var score = 0

private lateinit var true_button:Button
private lateinit var false_button: Button
//initialize arrays to size of questionBank to keep track of indicies where question was cheated on and/or already answered
var cheatArray = BooleanArray(QuizViewModel().questionBank.size)
var answeredArray = BooleanArray(QuizViewModel().questionBank.size)

//variables to track number of questions answered and index
var questionsanswered = 0
var index = 0

fun main() {
    for(i in 1..QuizViewModel().questionBank.size){
        cheatArray[i]= false
    }
}

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
            result ->
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        // hook up the button to its id
        //true_button = findViewById(R.id.true_button)
        //false_button = findViewById(R.id.false_button)

        // what happen if you click on those buttons
        binding.trueButton.setOnClickListener { view: View ->
            // Do something if you click on true button
            // have a correct toast that pops up

            checkAnswer(true)
        }

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        binding.cheatButton.setOnClickListener{ view: View ->
            // start cheat activity
            //val intent = Intent(this, CheatActivity::class.java)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            // startActivity(intent)

            cheatLauncher.launch(intent)
        }
        // onset listener for the next button
        // ie what happen if you press the next button
        binding.nextButton.setOnClickListener {
            //currentIndex = (currentIndex + 1)%questionBank.size
            quizViewModel.moveToNext()
            updateQuestion()
        }

        // this will get you the id for the current question in the question bank
        updateQuestion()

        val stringScore = "Current Score is $score out of $questionsanswered"
        binding.scoreTextView.setText(stringScore)
        val timertext = ("seconds remaining: " + 20000 / 1000)
        binding.timerTextView.setText(timertext)


    }








    private fun beginTimer() {
        val timer = object : CountDownTimer(30000, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                binding.timerTextView.setText("seconds remaining: " + millisUntilFinished / 1000)
            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                binding.timerTextView.setText("done!")
            }
        }.start()
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() is called")
    }

    private fun updateScore(){
        val stringScore = "Current Score is $score out of $questionsanswered"
        binding.scoreTextView.setText(stringScore)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() is called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() is called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() is called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() is called")
    }

    private fun updateQuestion(){
        //val questionTextResId = questionBank[currentIndex].textResId
        index = (index + 1) % QuizViewModel().questionBank.size
        //      Log.d(TAG,"conditional breakpoint", Exception())

        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)

        beginTimer()

    }


    private fun checkAnswer(userAnswer:Boolean){
        if (cheatArray[index]){
            //if cheated, check if already answered, if not update score and mark as answered
            Toast.makeText(this, R.string.cheated_string, Toast.LENGTH_SHORT).show()
            if (!answeredArray[index]){
                questionsanswered += 1
                updateScore()
                answeredArray[index] = true
            }


        }
        else {
            val correctAnswer = quizViewModel.currentQuestionAnswer

            val messageResId = if (userAnswer == correctAnswer) {
                if (!answeredArray[index]) {
                    //if not an answered question, update score, and mark as answered
                    score += 1
                    questionsanswered += 1
                    updateScore()

                    answeredArray[index] = true

                    R.string.correct_string
                }
                else{
                    R.string.correct_string
                }
            }
            else {
                //if not an answered question, update score, and mark as answered
                if (!answeredArray[index]) {
                    questionsanswered += 1
                    updateScore()
                    answeredArray[index] = true

                    R.string.incorrect_string
                }
                else{
                    R.string.incorrect_string
                }
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        }
    }



}