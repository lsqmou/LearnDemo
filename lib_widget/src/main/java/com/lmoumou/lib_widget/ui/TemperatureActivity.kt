package com.lmoumou.lib_widget.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lmoumou.lib_widget.R

/**
 * @author Lmoumou
 * @date : 2019/10/24 16:29
 */
class TemperatureActivity:AppCompatActivity() {

    companion object {
        fun startThis(context: Context){
            context.startActivity(Intent(context.applicationContext, TemperatureActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)
    }
}