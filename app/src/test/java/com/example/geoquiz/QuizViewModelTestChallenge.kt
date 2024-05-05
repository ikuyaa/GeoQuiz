package com.example.geoquiz

import org.junit.Assert.*
import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Test

class QuizViewModelTestChallenge{

    @Test
    fun isQuestionTrue(){
        val savedStateHandle = SavedStateHandle(mapOf(CURRENT_QUESTIONS_ANSWERED_KEY to 5))
        val quizViewModel = QuizViewModel(savedStateHandle)
        assertTrue(quizViewModel.currentQuestionAnswer)
    }
}