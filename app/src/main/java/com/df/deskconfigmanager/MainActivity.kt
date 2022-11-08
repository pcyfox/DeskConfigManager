package com.df.deskconfigmanager

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.df.lib_config.DeskConfig
import com.df.lib_config.DeskConfigManager

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()
    }

    private fun iniView() {
        DeskConfigManager.getInstance().updateData()
        DeskConfig.getInstance()?.run {
            findViewById<TextView>(R.id.tvXY).text = "$deskLine-$deskColumn"
            findViewById<EditText>(R.id.tvDeskNumber).let {
                Log.d(TAG, "iniView() called desk config :${this}")
                it.setText(deskNumber)
            }
        }
    }


    private fun requestPermission() {
        // 先判断有没有权限
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            iniView()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 19
            );
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            iniView()
        }
    }

    fun onBtnSaveClick(view: View) {
        findViewById<EditText>(R.id.tvDeskNumber).run {
            DeskConfigManager.getInstance().setDeskNumber(text.toString())
        }
    }
}