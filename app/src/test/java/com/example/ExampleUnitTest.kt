package com.example

import com.example.data.model.SubjectCategory
import com.example.data.repository.EducationalContent
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun waterCycleProcess_hasAllStages() {
    val process = EducationalContent.WaterCycleProcess
    assertNotNull(process)
    assertEquals("process_water_cycle", process.id)
    assertEquals(6, process.stages.size)

    val stageNames = process.stages.map { it.name }
    assertTrue(stageNames.any { it.contains("Evaporation", ignoreCase = true) })
    assertTrue(stageNames.any { it.contains("Transpiration", ignoreCase = true) })
    assertTrue(stageNames.any { it.contains("Condensation", ignoreCase = true) })
    assertTrue(stageNames.any { it.contains("Advection", ignoreCase = true) || it.contains("Transport", ignoreCase = true) })
    assertTrue(stageNames.any { it.contains("Precipitation", ignoreCase = true) })
    assertTrue(stageNames.any { it.contains("Infiltration", ignoreCase = true) || it.contains("Runoff", ignoreCase = true) })
  }

  @Test
  fun waterCycleCheckpoints_haveValidQuestions() {
    val stages = EducationalContent.WaterCycleProcess.stages
    for (stage in stages) {
      val q = stage.keyCheckpointQuestion
      assertNotNull(q.question)
      assertTrue(q.options.size >= 3)
      assertTrue(q.correctOptionIndex in 0 until q.options.size)
      assertTrue(q.explanation.isNotBlank())
    }
  }

  @Test
  fun curriculumLessons_areAvailableAcrossSubjects() {
    val lessons = EducationalContent.Lessons
    assertTrue(lessons.isNotEmpty())

    val categories = lessons.map { it.subjectCategory }.toSet()
    assertTrue(categories.contains(SubjectCategory.SCIENCE))
    assertTrue(categories.contains(SubjectCategory.TECHNOLOGY))
    assertTrue(categories.contains(SubjectCategory.MATHEMATICS))
  }

  @Test
  fun formulaHub_containsFormulas() {
    val formulas = EducationalContent.Formulas
    assertTrue(formulas.size >= 4)
    val waterCycleLatentHeat = formulas.find { it.id == "form_wc_heat" }
    assertNotNull(waterCycleLatentHeat)
    assertEquals("Q = m * L_v", waterCycleLatentHeat?.formula)
  }
}
