package com.example.testfloral

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.pro.cafy_theofficecafeteria.R
import kotlinx.android.synthetic.main.activity_survey.*
import kotlinx.android.synthetic.main.content_based_list.view.*

class SurveyAdapter( private  val titles: List<String>,  private  val details: List<String>) : RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder>() {

    inner class SurveyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.item_image_survey
        val textView1: TextView = itemView.item_name_survey
        val textView2: TextView = itemView.item_short_desc_survey
        val checkBox: CheckBox = itemView.checkBox
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.content_based_list, parent, false)
        return SurveyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {

        holder.textView1.text = titles[position]
        holder.textView2.text = details[position]

        holder.checkBox.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                Log.e(titles[position], "HttpException, unexpected response")
                }else{
                Log.e("", "HttpException, unexpected response")
                }
            }
        }


    override fun getItemCount() = titles.size
}
