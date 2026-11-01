package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.model.*
import com.example.data.repository.CurriculumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppScreen {
    HOME,
    LESSON_DETAIL,
    QUIZ,
    QUIZ_RESULT
}

data class ArabicUiState(
    val selectedGrade: GradeLevel = GradeLevel.PRIMARY_1,
    val currentScreen: AppScreen = AppScreen.HOME,
    val activeTab: Int = 0, // 0: Lessons, 1: Practice Bank, 2: Comprehensive Exam, 3: Badges
    val selectedLesson: Lesson? = null,
    val units: List<CurriculumUnit> = emptyList(),
    val filteredLessons: List<Lesson> = emptyList(),
    val searchQuery: String = "",
    val isGradeSelectorOpen: Boolean = false,
    val quizState: QuizSessionState? = null,
    val starsCount: Int = 12,
    val pointsCount: Int = 140,
    val completedLessonIds: Set<String> = setOf("g1_u1_l1"),
    val bookmarkedLessonIds: Set<String> = emptySet(),
    val achievements: List<StudentAchievement> = emptyList()
)

class ArabicAppViewModel(
    private val repository: CurriculumRepository = CurriculumRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArabicUiState())
    val uiState: StateFlow<ArabicUiState> = _uiState.asStateFlow()

    init {
        loadGradeCurriculum(GradeLevel.PRIMARY_1)
        _uiState.update { it.copy(achievements = repository.getAchievements()) }
    }

    fun selectGrade(grade: GradeLevel) {
        _uiState.update {
            it.copy(
                selectedGrade = grade,
                isGradeSelectorOpen = false,
                searchQuery = "",
                currentScreen = AppScreen.HOME
            )
        }
        loadGradeCurriculum(grade)
    }

    private fun loadGradeCurriculum(grade: GradeLevel) {
        val units = repository.getUnitsForGrade(grade)
        val allLessons = units.flatMap { it.lessons }
        _uiState.update {
            it.copy(
                units = units,
                filteredLessons = allLessons
            )
        }
    }

    fun setGradeSelectorOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isGradeSelectorOpen = isOpen) }
    }

    fun setActiveTab(tabIndex: Int) {
        _uiState.update { it.copy(activeTab = tabIndex) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            val filtered = repository.searchLessons(currentState.selectedGrade, query)
            currentState.copy(searchQuery = query, filteredLessons = filtered)
        }
    }

    fun openLesson(lesson: Lesson) {
        _uiState.update {
            it.copy(
                selectedLesson = lesson,
                currentScreen = AppScreen.LESSON_DETAIL
            )
        }
    }

    fun navigateBackToHome() {
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.HOME,
                quizState = null
            )
        }
    }

    fun startLessonQuiz(lesson: Lesson) {
        if (lesson.questions.isEmpty()) return
        _uiState.update {
            it.copy(
                selectedLesson = lesson,
                quizState = QuizSessionState(
                    quizTitle = "أسئلة: ${lesson.title}",
                    questions = lesson.questions,
                    currentIndex = 0,
                    score = 0
                ),
                currentScreen = AppScreen.QUIZ
            )
        }
    }

    fun startComprehensiveExam() {
        val examQuestions = repository.getComprehensiveExamForGrade(_uiState.value.selectedGrade)
        if (examQuestions.isEmpty()) return
        _uiState.update {
            it.copy(
                quizState = QuizSessionState(
                    quizTitle = "الاختبار الشامل - ${_uiState.value.selectedGrade.shortName}",
                    questions = examQuestions,
                    currentIndex = 0,
                    score = 0
                ),
                currentScreen = AppScreen.QUIZ
            )
        }
    }

    fun selectQuizOption(optionIndex: Int) {
        val currentQuiz = _uiState.value.quizState ?: return
        if (currentQuiz.isSubmitted) return
        _uiState.update {
            it.copy(quizState = currentQuiz.copy(selectedOptionIndex = optionIndex))
        }
    }

    fun submitCurrentQuestionAnswer() {
        val currentQuiz = _uiState.value.quizState ?: return
        val selectedOption = currentQuiz.selectedOptionIndex ?: return
        if (currentQuiz.isSubmitted) return

        val currentQuestion = currentQuiz.questions[currentQuiz.currentIndex]
        val isCorrect = (selectedOption == currentQuestion.correctIndex)
        val updatedScore = if (isCorrect) currentQuiz.score + 1 else currentQuiz.score

        val newAnswer = UserAnswer(
            question = currentQuestion,
            selectedIndex = selectedOption,
            isCorrect = isCorrect
        )

        _uiState.update {
            it.copy(
                quizState = currentQuiz.copy(
                    isSubmitted = true,
                    score = updatedScore,
                    answersHistory = currentQuiz.answersHistory + newAnswer
                )
            )
        }
    }

    fun nextQuestionOrFinish() {
        val currentQuiz = _uiState.value.quizState ?: return
        val isLast = currentQuiz.currentIndex >= currentQuiz.questions.size - 1

        if (isLast) {
            val totalQuestions = currentQuiz.questions.size
            val earnedStars = if (currentQuiz.score >= (totalQuestions * 0.8)) 3 else if (currentQuiz.score >= (totalQuestions * 0.5)) 2 else 1
            val gainedPoints = currentQuiz.score * 15

            _uiState.update { state ->
                val updatedCompleted = state.selectedLesson?.let { lesson ->
                    state.completedLessonIds + lesson.id
                } ?: state.completedLessonIds

                state.copy(
                    starsCount = state.starsCount + earnedStars,
                    pointsCount = state.pointsCount + gainedPoints,
                    completedLessonIds = updatedCompleted,
                    quizState = currentQuiz.copy(isQuizFinished = true),
                    currentScreen = AppScreen.QUIZ_RESULT
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    quizState = currentQuiz.copy(
                        currentIndex = currentQuiz.currentIndex + 1,
                        selectedOptionIndex = null,
                        isSubmitted = false
                    )
                )
            }
        }
    }

    fun restartQuiz() {
        val currentQuiz = _uiState.value.quizState ?: return
        _uiState.update {
            it.copy(
                quizState = QuizSessionState(
                    quizTitle = currentQuiz.quizTitle,
                    questions = currentQuiz.questions,
                    currentIndex = 0,
                    score = 0
                ),
                currentScreen = AppScreen.QUIZ
            )
        }
    }

    fun toggleBookmark(lessonId: String) {
        _uiState.update { state ->
            val updated = if (state.bookmarkedLessonIds.contains(lessonId)) {
                state.bookmarkedLessonIds - lessonId
            } else {
                state.bookmarkedLessonIds + lessonId
            }
            state.copy(bookmarkedLessonIds = updated)
        }
    }
}
