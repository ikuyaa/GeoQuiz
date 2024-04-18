package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0
    private var questionsAnswered = 0
    private var amtRight = 0
    private var amtWrong = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Setting True Button Listener Event
        binding.trueButton.setOnClickListener{view: View ->
            //Run this logic when the true button is clicked
            checkAnswer(true)
        }

        //Setting False Button Listener Event
        binding.falseButton.setOnClickListener { view: View ->
            //Run this logic when the false button is clicked
            checkAnswer(false)
        }

        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        binding.previousButton.setOnClickListener{
            if(currentIndex == 0) return@setOnClickListener

            currentIndex = (currentIndex - 1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause(){
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop(){
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion(){
        isQuizOver()
        val questionTextResId = questionBank[currentIndex].textResId
        isQuestionAnswered(currentIndex)
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        hideButtons()
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if(userAnswer == correctAnswer){
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        if(userAnswer == correctAnswer)
            amtRight++
        else
            amtWrong++

        questionBank[currentIndex].answered = true;
        questionsAnswered++
        Snackbar.make(this, this.findViewById(android.R.id.content), getString(messageResId), Snackbar.LENGTH_SHORT).show()

        //Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun isQuestionAnswered(index: Int){
        val isAnswered = questionBank[index].answered;
        binding.trueButton.isEnabled = !isAnswered;
        binding.falseButton.isEnabled = !isAnswered;

    }

    private fun hideButtons(){
        binding.trueButton.isEnabled = false;
        binding.falseButton.isEnabled = false;
    }

    private fun isQuizOver(){
        if(questionsAnswered >= questionBank.size){
            val percent = (amtRight * 100)  / questionBank.size
            val quizSnackBar = Snackbar.make(this, this.findViewById(android.R.id.content), "Quiz over. You got ${percent}% right!"
                , Snackbar.LENGTH_SHORT).addCallback(object: BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    for(question in questionBank){
                        question.answered = false
                    }
                    currentIndex = 0;
                    questionsAnswered = 0
                    amtRight = 0
                    amtWrong = 0
                    updateQuestion()
                }
                }).show();


        }

    }
}