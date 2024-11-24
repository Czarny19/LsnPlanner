package com.lczarny.lsnplanner.data.local.repository

import com.lczarny.lsnplanner.data.local.dao.ToDoDao
import com.lczarny.lsnplanner.data.local.entity.ToDo
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import kotlinx.coroutines.flow.Flow

class ToDoRepository(private val toDoDao: ToDoDao) {

    fun allToDos(lessonPlanId: Long): Flow<List<ToDo>> = toDoDao.getAllToDos(lessonPlanId)

    suspend fun insert(toDo: ToDo) {
        toDoDao.insertToDo(toDo)
    }

    suspend fun delete(id: Long) {
        toDoDao.deleteToDo(VarArgsId(id))
    }
}