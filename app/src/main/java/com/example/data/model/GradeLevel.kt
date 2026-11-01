package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class GradeLevel(
    val gradeNumber: Int,
    val title: String,
    val shortName: String,
    val tagLine: String,
    val accentColor: Color,
    val lightColor: Color,
    val iconEmoji: String
) {
    PRIMARY_1(
        gradeNumber = 1,
        title = "الصف الأول الابتدائي",
        shortName = "أولى ابتدائي",
        tagLine = "حروف الهجاء والحركات والمدود والتنوين",
        accentColor = Grade1Color,
        lightColor = ArabicRedLight,
        iconEmoji = "🌱"
    ),
    PRIMARY_2(
        gradeNumber = 2,
        title = "الصف الثاني الابتدائي",
        shortName = "تانية ابتدائي",
        tagLine = "الضمائر وأساليب الأمر والنهي والمفرد والجمع",
        accentColor = Grade2Color,
        lightColor = ArabicBlueLight,
        iconEmoji = "⭐"
    ),
    PRIMARY_3(
        gradeNumber = 3,
        title = "الصف الثالث الابتدائي",
        shortName = "تالتة ابتدائي",
        tagLine = "أدوات الاستفهام وظرف الزمان والمكان وأنواع الأفعال",
        accentColor = Grade3Color,
        lightColor = ArabicGreenLight,
        iconEmoji = "🚀"
    ),
    PRIMARY_4(
        gradeNumber = 4,
        title = "الصف الرابع الابتدائي",
        shortName = "رابعة ابتدائي",
        tagLine = "أقسام الكلمة، أركان الجملة الاسمية والجملة الفعلية",
        accentColor = Grade4Color,
        lightColor = ArabicPurpleLight,
        iconEmoji = "💡"
    ),
    PRIMARY_5(
        gradeNumber = 5,
        title = "الصف الخامس الابتدائي",
        shortName = "خامسة ابتدائي",
        tagLine = "إعراب الفاعل والمفعول والمثنى وأنواع الجموع والهمزات",
        accentColor = Grade5Color,
        lightColor = ArabicOrangeLight,
        iconEmoji = "🏆"
    ),
    PRIMARY_6(
        gradeNumber = 6,
        title = "الصف السادس الابتدائي",
        shortName = "ساتة ابتدائي",
        tagLine = "كان وأخواتها، إن وأخواتها، الأسماء والأفعال الخمسة",
        accentColor = Grade6Color,
        lightColor = ArabicBlueLight,
        iconEmoji = "🎓"
    );

    companion object {
        fun fromGradeNumber(number: Int): GradeLevel {
            return entries.firstOrNull { it.gradeNumber == number } ?: PRIMARY_1
        }
    }
}
