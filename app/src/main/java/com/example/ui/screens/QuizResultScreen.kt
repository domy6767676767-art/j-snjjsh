package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GradeLevel
import com.example.data.model.QuizSessionState
import com.example.ui.theme.ArabicAmberDark
import com.example.ui.theme.ArabicAmberSecondary
import com.example.ui.theme.ArabicGreenLight
import com.example.ui.theme.ArabicGreenSuccess
import com.example.ui.theme.ArabicRedError
import com.example.ui.theme.ArabicRedLight

@Composable
fun QuizResultScreen(
    quizState: QuizSessionState,
    currentGrade: GradeLevel,
    onRestart: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackToHome()
    }

    val totalQuestions = quizState.questions.size
    val score = quizState.score
    val percentage = if (totalQuestions > 0) (score * 100) / totalQuestions else 0

    val (gradeTitle, starsCount, motivationalQuote) = when {
        percentage >= 90 -> Triple("عبقري اللغة العربية 🥇", 3, "ما شاء الله! أداء استثنائي وتفوق رائع في قواعد ولغة القرآن!")
        percentage >= 70 -> Triple("بطل ممتاز ومجتهد ⭐", 2, "أحسنت صنعاً! إجاباتك تدل على فهم عميق واهتمام كبير.")
        percentage >= 50 -> Triple("محاولة جيدة وجديرة بالتشجيع 👍", 1, "عمل طيب! راجع الدروس مرة أخرى لتصبح الأول دائماً.")
        else -> Triple("فرصة رائعة للمراجعة والتعلم 💡", 1, "لا بأس يا بطل، راجع الشرح وحاول مرة ثانية لتتفوق!")
    }

    Scaffold(
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onRestart,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("result_restart_btn"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("إعادة الاختبار", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onBackToHome,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("result_home_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = currentGrade.accentColor
                        )
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("قائمة الدروس", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero Celebration Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Stars Row
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(3) { index ->
                                val isFilled = index < starsCount
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (isFilled) ArabicAmberSecondary else MaterialTheme.colorScheme.outlineVariant,
                                    modifier = Modifier.size(if (index == 1) 48.dp else 36.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = gradeTitle,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = currentGrade.accentColor
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = motivationalQuote,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Score Pill
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = currentGrade.lightColor
                        ) {
                            Text(
                                text = "درجتك: $score من $totalQuestions  ($percentage%)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = currentGrade.accentColor,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            // Answer History / Review Title
            item {
                Text(
                    text = "📝 مراجعة الأسئلة والإجابات:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // List of questions and user answers
            itemsIndexed(quizState.answersHistory) { index, userAnswer ->
                val q = userAnswer.question
                val isCorrect = userAnswer.isCorrect

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) ArabicGreenLight.copy(alpha = 0.5f) else ArabicRedLight.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "السؤال ${index + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) ArabicGreenSuccess else ArabicRedError
                            )

                            Icon(
                                imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (isCorrect) ArabicGreenSuccess else ArabicRedError
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = q.text,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val selectedText = q.options.getOrNull(userAnswer.selectedIndex) ?: ""
                        val correctText = q.options.getOrNull(q.correctIndex) ?: ""

                        if (!isCorrect) {
                            Text(
                                text = "إجابتك: $selectedText ❌",
                                style = MaterialTheme.typography.bodySmall,
                                color = ArabicRedError
                            )
                        }

                        Text(
                            text = "الإجابة الصحيحة: $correctText ✓",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = ArabicGreenSuccess
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "السبب: ${q.explanation}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}
