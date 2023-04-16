package com.example.drawboard

import android.content.Context
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

fun Context.dp2px (dp:Int) = resources.displayMetrics.density*dp

val colorsArray = arrayOf(
    "#EF9817","#F14780","#586CDF","#B2FF58","#ffffff","#FF000000"
)

//权限检测
fun Context.checkReadablePermission():Boolean {
    ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
    return true
}





























