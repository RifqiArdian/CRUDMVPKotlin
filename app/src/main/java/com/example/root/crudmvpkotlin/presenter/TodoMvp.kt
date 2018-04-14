package com.example.root.crudmvpkotlin.presenter

import com.example.root.crudmvpkotlin.model.Todo

class TodoMvp {

    interface TodoView{
        fun setData(listTodos: List<Todo>)
        fun setEmpty()
        fun setResult(message: String)
        fun onLoad(isLoad: Boolean)

    }

    interface TodoPresenter{
        fun insertData(todo: Todo)
        fun deleteData(id: Int)
        fun updateData(todo: Todo)
        fun getAllData()
    }
}