package com.htech.eh_ho

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup

fun AppCompatActivity.isFirstTimeCreated(savedInstanceSate: Bundle?): Boolean = savedInstanceSate == null

fun ViewGroup.inflate(idLayout: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(this.context).inflate(idLayout, this, attachToRoot)
