package com.lczarny.lsnplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lczarny.lsnplanner.data.common.model.LessonPlanModel
import com.lczarny.lsnplanner.data.common.model.VarArgsId
import com.lczarny.lsnplanner.data.local.entity.LessonPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonPlanDao {

    @Transaction
    @Query("SELECT EXISTS(SELECT 1 FROM lesson_plan WHERE is_active = 1 and profile_id = :profileId LIMIT 1)")
    suspend fun checkIfActivePlanExists(profileId: String): Boolean

    @Transaction
    @Query("SELECT  * FROM lesson_plan WHERE id = :id")
    suspend fun getSingle(id: Long): LessonPlan

    @Transaction
    @Query("SELECT * FROM lesson_plan WHERE is_active = 1 and profile_id = :profileId LIMIT 1")
    fun getActive(profileId: String): Flow<LessonPlan?>

    @Transaction
    @Query("SELECT * FROM lesson_plan where profile_id = :profileId")
    fun getAll(profileId: String): Flow<List<LessonPlan>>

    @Transaction
    @Query("UPDATE lesson_plan set is_active = 0 WHERE id != :lessonPlanId and profile_id = :profileId")
    suspend fun makeOtherPlansNotActive(lessonPlanId: Long, profileId: String)

    @Update(entity = LessonPlan::class)
    suspend fun update(lessonPlan: LessonPlanModel)

    @Insert(entity = LessonPlan::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(lessonPlan: LessonPlanModel): Long

    @Delete(entity = LessonPlan::class)
    suspend fun delete(vararg id: VarArgsId)
}