package com.example.drawboard.adapter

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.example.drawboard.*
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

class ClickEvents {

    fun dealWithColorMenu(view: View,model: MainViewModel){
        if (model.isColorOpen.value == false){
            model.colorList.forEachIndexed { index, floatingActionButton ->
                setOpenColor(index,model)
            }
            model.isColorOpen.postValue(true)
        }else{
            model.colorList.forEachIndexed { index, floatingActionButton ->
                getCloseColor(index,model)
            }
            model.isColorOpen.postValue(false)
        }
    }

    private fun setOpenColor(index: Int,model: MainViewModel){
        val color = model.colorList[index]
        color.translationY
        val transAnimator = ObjectAnimator.ofFloat(
            color,"translationY",-color.width*(index+1)*1.5f
        )
        transAnimator.apply {
                duration=500
                interpolator= LinearInterpolator()
                start()
        }
    }
    private fun getCloseColor(index: Int,model: MainViewModel){
        val color = model.colorList[index]
        color.translationY
        val transAnimator = ObjectAnimator.ofFloat(
            color,"translationY",0f
        )
        transAnimator.apply {
                duration=500
                interpolator= LinearInterpolator()
                start()
        }
    }

    fun changePenSizeVisiblwState(view: View,model: MainViewModel,draw: Draw){
        changeColor(view, draw)
        if (model.showPanSize.value == true){
            model.showPanSize.postValue(false)
        }else{
            model.showPanSize.postValue(true)
        }
    }


    private fun changeColor(view: View,draw: Draw){
        val index = (view.tag as String).toInt()
        val colorString = colorsArray[index-1]
        //更改画笔颜色
        draw.mColor = Color.parseColor(colorString)
    }

    fun changePaintColor(view: View,draw: Draw){
        changeColor(view, draw)
    }

    fun changeStrokeWidth(view: View,draw: Draw){
        //获取tag值
        val widthDp = (view.tag as String).toInt()
        val widthPx = view.context.dp2px(widthDp)
        draw.mStrokeWidth = widthPx
    }

    fun undo(view: View,draw: Draw){
        draw.undo()
    }

    fun savePictureToGallary(view: View,draw: Draw,model: MainViewModel){
       //检测是否有权限
       val readPerission = view.context.checkReadablePermission()
        if (readPerission){
            val bitmap = convertViewToBitmap(draw)
            //保存图片
            saveBitmap(bitmap,draw){ uri->
                if (uri !=null){
                    model.imageUri = uri
                    Toast.makeText(view.context,"save ok",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(view.context,"save fail",Toast.LENGTH_LONG).show()
                }
            }
        }else{
            Toast.makeText(view.context,"no permission",Toast.LENGTH_LONG).show()
        }
    }
    //将视图转化为图片
    //将view的内容重新绘制到一个我们提供的bitmap上 方便拿去下载
    private fun convertViewToBitmap(view: Draw):Bitmap{
        val bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return  bitmap
    }
    //保存图片
    private fun saveBitmap(bitmap: Bitmap,view: Draw,callBack:(Uri?)->Unit){
        //获取view所在的activity -> ContextWrapper（contentResolver）
        val activity = view.context as MainActivity

        //图片所在的文件夹的路径
        val  uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }else{
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        //配置图片信息
        val contentValue = ContentValues().apply {
            //用时间作为文件名
            put(MediaStore.Images.Media.DISPLAY_NAME,getName())
            //图片格式
            put(MediaStore.Images.Media.MIME_TYPE,"image/jpg")
            //宽度
            put(MediaStore.Images.Media.WIDTH,bitmap.width)
            //高度
            put(MediaStore.Images.Media.HEIGHT,bitmap.height)
        }
        //定位到这个图片的文件路径
        val imageUri = activity.contentResolver.insert(uri,contentValue)

        //进行保存
        if (imageUri != null){
            activity.contentResolver.openOutputStream(imageUri).use {os->
                //通过输出流将bitmap写出
                val result = bitmap.compress(Bitmap.CompressFormat.JPEG,100,os)
                if (result){
                    callBack(imageUri)
                }else{
                    callBack(null)
                    //Toast.makeText(view.context,"save file",Toast.LENGTH_LONG).show()
                }
            }
        }else{
            throw NullPointerException("获取路径失败")
        }
    }
    //将时间转化为名字
    @SuppressLint("SimpleDateFormat")
    private fun getName():String{
        val timeStr = SimpleDateFormat("yyyMMddHHmmss").format(Date())
        return  "$timeStr.jpg"
    }


    fun shareImage(view: View,model: MainViewModel,draw: Draw){
        if (model.imageUri != null && draw.pathModelList.isNotEmpty()){
            //隐式跳转
            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM,model.imageUri)
                putExtra(Intent.EXTRA_TEXT,"我的图片")
                type = "image/jpg"
            }
            view.context.startActivity(intent)
        }else{
            Toast.makeText(view.context,"not save,can't send",Toast.LENGTH_LONG).show()
        }
    }
}










