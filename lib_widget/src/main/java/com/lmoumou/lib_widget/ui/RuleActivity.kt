package com.lmoumou.lib_widget.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lmoumou.lib_widget.R
import com.lmoumou.lib_widget.entity.RuleBeen
import kotlinx.android.synthetic.main.activity_rule.*

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
        val testData= mutableListOf(
            RuleBeen("1", 0, false),
            RuleBeen("2", 10, false),
            RuleBeen("3", 5, false),
            RuleBeen("4", 7, false),
            RuleBeen("5", 9, false),
            RuleBeen("6", 3, false),
            RuleBeen("7", 0, false),
            RuleBeen("8", 22, false),
            RuleBeen("9", 28, false),
            RuleBeen("10", 13, false),
            RuleBeen("11", 11, false),
            RuleBeen("12", 29, false),
            RuleBeen("13", 14, true),
            RuleBeen("14", 14, false),
            RuleBeen("15", 30, false)
        )
        mRuleView.setData(testData)
    }
}