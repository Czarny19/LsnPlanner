package com.lczarny.lsnplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lczarny.lsnplanner.data.local.entity.LessonPlan
import com.lczarny.lsnplanner.data.local.model.LessonPlanModel
import com.lczarny.lsnplanner.data.local.model.LessonPlanWithClasses
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonPlanDao {

    @Transaction
    @Query("SELECT EXISTS(SELECT 1 FROM lesson_plan WHERE isDefault = 1 LIMIT 1)")
    fun checkIfDefaultPlanExists(): Flow<Boolean>

    @Transaction
    @Query("SELECT  * FROM lesson_plan WHERE id = :id")
    fun getLessonPlan(id: Long): Flow<LessonPlan>

    @Transaction
    @Query("SELECT * FROM lesson_plan WHERE isDefault = 1 LIMIT 1")
    fun getDefaultLessonPlanWithClasses(): Flow<LessonPlanWithClasses>

    @Transaction
    @Query("SELECT  * FROM lesson_plan WHERE id = :id")
    fun getLessonPlanWithClasses(id: Long): Flow<LessonPlanWithClasses>

    @Insert(entity = LessonPlan::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun insertLessonPlan(lessonPlan: LessonPlanModel)

    @Delete(entity = LessonPlan::class)
    suspend fun deleteLessonPlan(vararg id: VarArgsId)
}