package com.pro.cafy_theofficecafeteria

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.testfloral.RetrofitInstance
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.sifaa_activity_demand.*
import retrofit2.HttpException
import java.io.IOException


val labels = ArrayList<String>()
val entries = ArrayList<BarEntry>()
val yAxes = ArrayList<String>()
val xAxes = ArrayList<String>()
var y = 5
class DemandActivity : AppCompatActivity() {
    var seekBarNormal: SeekBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sifaa_activity_demand)
        xAxes.clear()
        yAxes.clear()
        labels.clear()
        entries.clear()
        seekBarNormal = findViewById(R.id.seekbar_Default)
        seekBarNormal?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int,
                fromUser: Boolean) {
                if (progress >= 5) {
                    if (progress <= 50) {
                        setBarChart(progress) } } }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Toast.makeText(applicationContext, "seekbar touch started!", Toast.LENGTH_SHORT).show() }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Toast.makeText(applicationContext, "seekbar touch stopped!", Toast.LENGTH_SHORT).show() }
        })
        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getTodos()
            } catch (e: IOException) {
                Log.e("TAG", "IOException, you might not have internet connection")
                return@launchWhenCreated
            } catch (e: HttpException) {
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {
                for (i in response.body()!!.indices) {
                    xAxes.add(response.body()!![i].num_orders)
                    yAxes.add(response.body()!![i].center_id)
                }
                for (i in 0..4) {
                    entries.add(BarEntry((xAxes[i]).toFloat(), i))
                    labels.add(yAxes[i])
                    val barDataSet = BarDataSet(entries, "Cells")
                    val data = BarData(labels, barDataSet)
                    barChartt.data = data // set the data and list of lables into chart
                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                    barChartt.animateY(5000)
                }
            }
        }
    }
    private fun setBarChart(y: Int) {
            entries.add(BarEntry((xAxes[y]).toFloat(), y))
            labels.add(yAxes[y])
            val barDataSet = BarDataSet(entries, "Cells")
            val data = BarData(labels, barDataSet)
            barChartt.data = data // set the data and list of lables into chart
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
            barChartt.animateY(0)
    }
}