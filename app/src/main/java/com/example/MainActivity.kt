package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LessonDetailScreen
import com.example.ui.screens.QuizResultScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.theme.ArabicPrimaryTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ArabicAppViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ArabicAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArabicPrimaryTheme {
                // Ensure natural and standard RTL (Right-to-Left) direction for Arabic language
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        ArabicAppContent(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ArabicAppContent(viewModel: ArabicAppViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.currentScreen) {
        AppScreen.HOME -> {
            HomeScreen(
                uiState = uiState,
                viewModel = viewModel
            )
        }
        AppScreen.LESSON_DETAIL -> {
            uiState.selectedLesson?.let { lesson ->
                LessonDetailScreen(
                    lesson = lesson,
                    isBookmarked = uiState.bookmarkedLessonIds.contains(lesson.id),
                    onBack = { viewModel.navigateBackToHome() },
                    onStartQuiz = { viewModel.startLessonQuiz(it) },
                    onToggleBookmark = { viewModel.toggleBookmark(it) }
                )
            } ?: run {
                HomeScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
        AppScreen.QUIZ -> {
            uiState.quizState?.let { quizState ->
                QuizScreen(
                    quizState = quizState,
                    currentGrade = uiState.selectedGrade,
                    onSelectOption = { viewModel.selectQuizOption(it) },
                    onSubmitAnswer = { viewModel.submitCurrentQuestionAnswer() },
                    onNextQuestion = { viewModel.nextQuestionOrFinish() },
                    onBack = { viewModel.navigateBackToHome() }
                )
            } ?: run {
                HomeScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
        AppScreen.QUIZ_RESULT -> {
            uiState.quizState?.let { quizState ->
                QuizResultScreen(
                    quizState = quizState,
                    currentGrade = uiState.selectedGrade,
                    onRestart = { viewModel.restartQuiz() },
                    onBackToHome = { viewModel.navigateBackToHome() }
                )
            } ?: run {
                HomeScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
    }
}
