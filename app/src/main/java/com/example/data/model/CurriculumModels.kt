package com.example.data.model

data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val category: String = "نحو وقواعد"
)

data class VocabularyItem(
    val word: String,
    val meaning: String,
    val oppositeOrPlural: String = "",
    val sentenceExample: String = ""
)

data class Lesson(
    val id: String,
    val gradeLevel: GradeLevel,
    val unitId: String,
    val unitTitle: String,
    val title: String,
    val summary: String,
    val ruleText: String,
    val examples: List<String>,
    val vocabulary: List<VocabularyItem> = emptyList(),
    val questions: List<Question> = emptyList(),
    val iconEmoji: String = "📖",
    val readTimeMinutes: Int = 4
)

data class CurriculumUnit(
    val id: String,
    val gradeLevel: GradeLevel,
    val unitNumber: Int,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val lessons: List<Lesson>
)

data class StudentAchievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val requiredStars: Int
)

data class QuizSessionState(
    val quizTitle: String,
    val questions: List<Question>,
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isSubmitted: Boolean = false,
    val score: Int = 0,
    val answersHistory: List<UserAnswer> = emptyList(),
    val isQuizFinished: Boolean = false
)

data class UserAnswer(
    val question: Question,
    val selectedIndex: Int,
    val isCorrect: Boolean
)
