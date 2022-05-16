package com.iplizi.firstcomposeapp

import kotlinx.coroutines.delay

class Repository {

    private val remoteDataSource = (1..100).map{
        RequestListItem(
            title = "Item $it",
            description = "Description $it"
        )
    }


    /*
    Функция ниже симулирует запрос из какого-то апи, к примеру.
    В своем списке я прошу первые 10, к примеру, элементов. Я ввожу
    0 страницу и 10 размер  страницы. Так оно возьмет первые 10 элементов по индексу, с помощью
    .slice(..)
    Параметры отбора элементов могут быть любыми
     */

    suspend fun getItems(page: Int, pageSize: Int): Result<List<RequestListItem>>{
        delay(2000L)
        val startingIndex = page * pageSize
        return if(startingIndex + pageSize <= remoteDataSource.size){
            Result.success(
                remoteDataSource.slice(startingIndex until startingIndex + pageSize)
            )
        } else Result.success(emptyList())
    }

}