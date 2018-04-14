package com.example.root.crudmvpkotlin

import android.app.Application
import android.content.Context
import com.example.root.crudmvpkotlin.repository.DbHelper

class TodoApplication : Application(){

    public var db: DbHelper = DbHelper(this)
    override fun onCreate() {
        super.onCreate()

    }

}