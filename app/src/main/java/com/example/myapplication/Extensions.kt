package com.example.myapplication

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast


fun Context.showShortToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Any.doLog(message: String) = Log.e(this.javaClass.simpleName, message)

fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)