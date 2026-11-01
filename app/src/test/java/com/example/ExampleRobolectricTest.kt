package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.GradeLevel
import com.example.data.repository.CurriculumRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("عربي ابتدائي", appName)
  }

  @Test
  fun `curriculum repository provides all 6 grades`() {
    val repo = CurriculumRepository()
    for (grade in GradeLevel.entries) {
      val units = repo.getUnitsForGrade(grade)
      assertTrue("Grade ${grade.shortName} should have units", units.isNotEmpty())
      val lessons = repo.getAllLessonsForGrade(grade)
      assertTrue("Grade ${grade.shortName} should have lessons", lessons.isNotEmpty())
      val exam = repo.getComprehensiveExamForGrade(grade)
      assertTrue("Grade ${grade.shortName} should have exam questions", exam.isNotEmpty())
    }
  }
}
