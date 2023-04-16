package com.example.drawboard.adapter

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.databinding.BindingAdapter

@BindingAdapter("shouldOpen")
fun View.shouldOpen(state:Boolean){
    if (state){
        this.translationY
        ObjectAnimator.ofFloat(this,"translationY",0f,-height.toFloat()).apply {
            duration=500
            interpolator = LinearInterpolator()
            start()
        }
    }else{
        this.translationY
        ObjectAnimator.ofFloat(this,"translationY",-height.toFloat(),0f).apply {
            duration=500
            interpolator = LinearInterpolator()
            start()
        }
    }
}