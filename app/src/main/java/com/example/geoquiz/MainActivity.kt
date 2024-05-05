package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.example.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.lang.Integer.parseInt


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        //Handle the results
        if(result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)?: false

            //This is where we set the question to be cheated on
            quizViewModel.questionBank[quizViewModel.currentIndex].cheated = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

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
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.previousButton.setOnClickListener{
            if(quizViewModel.currentIndex == 0) return@setOnClickListener

            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener{
            //Start Cheat Activity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        binding.questionTextView.setOnClickListener(){
            quizViewModel.moveToNext()
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
        val questionTextResId = quizViewModel.currentQuestionText
        isQuizOver()
        isQuestionAnswered(quizViewModel.currentIndex)
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        hideButtons()
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val currentAnswer = quizViewModel.questionBank[quizViewModel.currentIndex]

        val messageResId = when {
            currentAnswer.cheated -> R.string.judgment_toast

            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        if(userAnswer == correctAnswer)
            quizViewModel.amtRight++
        else
            quizViewModel.amtWrong++

        quizViewModel.setQuestionAnswered()
        quizViewModel.questionsAnswered++
        Snackbar.make(this, this.findViewById(android.R.id.content), getString(messageResId), Snackbar.LENGTH_SHORT).show()

        //Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun isQuestionAnswered(index: Int){
        val isAnswered = quizViewModel.isQuestionAnwered;
        binding.trueButton.isEnabled = !isAnswered;
        binding.falseButton.isEnabled = !isAnswered;

    }

    private fun hideButtons(){
        binding.trueButton.isEnabled = false;
        binding.falseButton.isEnabled = false;
    }

    private fun isQuizOver(){
        if(quizViewModel.questionsAnswered >= quizViewModel.questionBank.size){
            val percent = (quizViewModel.amtRight * 100)  / quizViewModel.questionBank.size
            val quizSnackBar = Snackbar.make(this, this.findViewById(android.R.id.content), "Quiz over. You got ${percent}% right!"
                , Snackbar.LENGTH_SHORT).addCallback(object: BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    for(question in quizViewModel.questionBank){
                        question.answered = false
                    }
                    quizViewModel.resetCurrentIndex()
                    quizViewModel.questionsAnswered = 0
                    quizViewModel.amtRight = 0
                    quizViewModel.amtWrong = 0
                    updateQuestion()
                }
                }).show();
        }
    }
}