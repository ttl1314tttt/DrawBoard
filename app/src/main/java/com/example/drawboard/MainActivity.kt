package com.example.drawboard

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.drawboard.adapter.ClickEvents
import com.example.drawboard.databinding.ActivityMainBinding
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    val model:MainViewModel by viewModels()
    lateinit var binding:ActivityMainBinding
    var isWritable = false
    var isReadable = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.colorList.add(binding.color1)
        model.colorList.add(binding.color2)
        model.colorList.add(binding.color3)
        model.colorList.add(binding.color4)

        binding.model = model
        binding.lifecycleOwner = this
        binding.clickEvents = ClickEvents()
        binding.draw = binding.drawView

        //检测是否有权限
        checkPermissions()
        //请求权限
        requestPermission()

        contentResolver
    }
    fun checkPermissions(){
        isReadable = ContextCompat.checkSelfPermission(
            this,android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isWritable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){ //29
            true
        }else {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    fun requestPermission(){
        //保存需要请求的权限
        val permissionsArray = arrayListOf<String>()
        //创建请求权限的对象
        val resultLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            //获取请求结果
            isReadable = it[android.Manifest.permission.READ_EXTERNAL_STORAGE]?:isReadable
            isWritable = it[android.Manifest.permission.WRITE_EXTERNAL_STORAGE]?:isWritable
        }
        if (!isReadable){
            permissionsArray.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!isWritable){
            permissionsArray.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        //发起请求
        resultLauncher.launch(permissionsArray.toTypedArray())
    }
}












