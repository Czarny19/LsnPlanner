package com.lczarny.lsnplanner.data.local.repository

import com.lczarny.lsnplanner.data.local.dao.ToDoDao
import com.lczarny.lsnplanner.data.local.model.ToDoModel
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import com.lczarny.lsnplanner.data.local.model.mapToModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ToDoRepository(private val toDoDao: ToDoDao) {

    fun allToDos(lessonPlanId: Long): Flow<List<ToDoModel>> = toDoDao.getAllToDos(lessonPlanId).map { it.map { it.mapToModel() } }

    suspend fun insert(toDo: ToDoModel) {
        toDoDao.insertToDo(toDo)
    }

    suspend fun update(toDo: ToDoModel) {
        toDoDao.updateToDo(toDo)
    }

    suspend fun delete(id: Long) {
        toDoDao.deleteToDo(VarArgsId(id))
    }

    suspend fun deleteAllHistorical() {
        toDoDao.deleteHistoricalToDos()
    }

    suspend fun markAsComplete(id: Long) {
        toDoDao.markToDoAsComplete(id)
    }
}