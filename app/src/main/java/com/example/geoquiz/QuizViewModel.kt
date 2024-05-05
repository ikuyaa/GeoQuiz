package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.lang.Exception

const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val CURRENT_QUESTIONS_ANSWERED_KEY = "CURRENT_QUESTIONS_ANSWERED_KEY"
const val AMT_RIGHT_KEY = "AMT_RIGHT_KEY"
const val AMT_WRONG_KEY = "AMT_WRONG_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val ANSWER_CHEATED_KEY = "ANSWER_CHEATED_KEY"
class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
     val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY)?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)
    var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY)?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    var questionsAnswered: Int
        get() = savedStateHandle.get(CURRENT_QUESTIONS_ANSWERED_KEY)?: 0
        set(value) = savedStateHandle.set(CURRENT_QUESTIONS_ANSWERED_KEY, value)

    var amtRight: Int
        get() = savedStateHandle.get(AMT_RIGHT_KEY)?: 0
        set(value) = savedStateHandle.set(AMT_RIGHT_KEY, value)

    var amtWrong: Int
        get() = savedStateHandle.get(AMT_WRONG_KEY)?: 0
        set(value) = savedStateHandle.set(AMT_WRONG_KEY, value)
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val isQuestionAnwered: Boolean
        get() = questionBank[currentIndex].answered

    val isQuestionCheated: Boolean
        get() = questionBank[currentIndex].cheated
    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious(){
        currentIndex = (currentIndex - 1) % questionBank.size
    }

    fun setQuestionAnswered(){
        questionBank[currentIndex].answered = true
    }

    fun resetCurrentIndex(){
        currentIndex = 0
    }

    fun cheatedOnAnswer(){
        questionBank[currentIndex].cheated = true
    }
}