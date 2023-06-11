package com.example.testfloral

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pro.cafy_theofficecafeteria.R
import kotlinx.android.synthetic.main.activity_survey.*

class SurveyActivity : AppCompatActivity() {
    private val labels = ArrayList<String>()

    private var titleList = ArrayList<String>()
    private var descList = ArrayList<String>()
    private var count = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        cb01.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                count += 1
                if (count > 5){
                    cb01.isChecked = false
                }else{
                    countt(count)
                    test(1)
                }
            }
        }
        cb02.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                count += 1
                if (count > 5){
                    cb02.isChecked = false
                }else{
                    countt(count)
                    test(2)
                }
            }
        }
        cb03.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                count += 1
                if (count > 5){
                    cb03.isChecked = false
                }else{
                    countt(count)
                    test(3)
                }

            }
        }
        cb04.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                count += 1
                if (count > 5){
                    cb04.isChecked = false
                }else{
                    countt(count)
                    test(4)
                }
            }
        }
        cb05.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                count += 1
                if (count > 5){
                    cb05.isChecked = false
                }else{
                    countt(count)
                    test(5)
                }
            }
        }
        cb06.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                count += 1
                if (count > 5){
                    cb06.isChecked = false
                }else{
                    countt(count)
                    test(6)
                }
            }
        }
        cb07.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                count += 1
                if (count > 5){
                    cb07.isChecked = false
                }else{
                    countt(count)
                    test(7)
                }
            }
        }
        cb08.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                count += 1
                if (count > 5){
                    cb08.isChecked = false
                }else{
                countt(count)
                test(8)
                }
            }
        }


        val left = count




//        when {
//            cb01.onche -> {
//                tvGetSurvey.text = cb01.text
//            }
//            cb02.isChecked -> {
//                tvGetSurvey.text = cb02.text
//            }
//            cb03.isChecked -> {
//                tvGetSurvey.text = cb03.text
//            }
//            cb04.isChecked -> {
//                tvGetSurvey.text = cb04.text
//            }
//            cb05.isChecked -> {
//                tvGetSurvey.text = cb05.text
//            }
//        }
    }

    private fun test(i: Int){
                titleList.add(i.toString())
                descList.add(i.toString())


        rvGetSurvey.layoutManager = LinearLayoutManager(this)
        rvGetSurvey.adapter = SurveyAdapter(titleList,descList)
    }

    @SuppressLint("SetTextI18n")
    private fun countt(count: Int){
        val left = 5 - count
        if(left >= 0) {
            tvCountLeft.text = "You Can Select $left Items"
        }
        if(left <= 0)  {
            Toast.makeText(this, "Sorry, You Can't Select More Than 5 Items, Try Again Later!", Toast.LENGTH_SHORT).show()
        }
    }

}