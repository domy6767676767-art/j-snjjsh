package com.example.data.repository

import com.example.data.model.*

class CurriculumRepository {

    fun getUnitsForGrade(gradeLevel: GradeLevel): List<CurriculumUnit> {
        return when (gradeLevel) {
            GradeLevel.PRIMARY_1 -> GradeData1to3.getGrade1Units()
            GradeLevel.PRIMARY_2 -> GradeData1to3.getGrade2Units()
            GradeLevel.PRIMARY_3 -> GradeData1to3.getGrade3Units()
            GradeLevel.PRIMARY_4 -> GradeData4to6.getGrade4Units()
            GradeLevel.PRIMARY_5 -> GradeData4to6.getGrade5Units()
            GradeLevel.PRIMARY_6 -> GradeData4to6.getGrade6Units()
        }
    }

    fun getAllLessonsForGrade(gradeLevel: GradeLevel): List<Lesson> {
        return getUnitsForGrade(gradeLevel).flatMap { it.lessons }
    }

    fun getComprehensiveExamForGrade(gradeLevel: GradeLevel): List<Question> {
        val allQuestions = getAllLessonsForGrade(gradeLevel).flatMap { it.questions }
        return if (allQuestions.isNotEmpty()) {
            allQuestions.shuffled().take(6)
        } else {
            emptyList()
        }
    }

    fun searchLessons(gradeLevel: GradeLevel, query: String): List<Lesson> {
        if (query.isBlank()) return getAllLessonsForGrade(gradeLevel)
        val cleanQuery = query.trim().lowercase()
        return getAllLessonsForGrade(gradeLevel).filter { lesson ->
            lesson.title.contains(cleanQuery, ignoreCase = true) ||
            lesson.summary.contains(cleanQuery, ignoreCase = true) ||
            lesson.ruleText.contains(cleanQuery, ignoreCase = true)
        }
    }

    fun getAchievements(): List<StudentAchievement> {
        return listOf(
            StudentAchievement(
                id = "ach_first_lesson",
                title = "فارس الانطلاق 🚀",
                description = "أكمل أول درس بنجاح في مادة اللغة العربية.",
                iconEmoji = "🌱",
                requiredStars = 1
            ),
            StudentAchievement(
                id = "ach_grammar_master",
                title = "أمير النحو والبيان 👑",
                description = "حصل على 5 نجوم في أسئلة وتدريبات القواعد.",
                iconEmoji = "⭐",
                requiredStars = 5
            ),
            StudentAchievement(
                id = "ach_quiz_champ",
                title = "بطل التحديات الذهبي 🏆",
                description = "أجاب على 10 أسئلة إجابة صحيحة متتالية.",
                iconEmoji = "🥇",
                requiredStars = 10
            ),
            StudentAchievement(
                id = "ach_comprehensive_pro",
                title = "عبقري المرحلة الابتدائية 🎓",
                description = "اجتاز الاختبار الشامل للسنة الدراسية بامتياز.",
                iconEmoji = "🌟",
                requiredStars = 15
            )
        )
    }
}
