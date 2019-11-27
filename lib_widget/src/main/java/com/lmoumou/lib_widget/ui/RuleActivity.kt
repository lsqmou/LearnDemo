package com.lmoumou.lib_widget.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lmoumou.lib_widget.R

/**
 * @author Lmoumou
 * @date : 2019/11/27 17:02
 */
class RuleActivity:AppCompatActivity() {

    companion object {
        fun startThis(context: Context){
            context.startActivity(Intent(context.applicationContext,RuleActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rule)
    }
}