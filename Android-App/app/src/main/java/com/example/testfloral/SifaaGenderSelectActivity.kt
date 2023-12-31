package com.pro.cafy_theofficecafeteria

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.FirebaseDatabase


class SifaaGenderSelectActivity : AppCompatActivity() {

    private lateinit var maleTextviewSelected: TextView
    private lateinit var femaleTextviewSelected: TextView

    private lateinit var maleSelectedBorder: ImageView
    private lateinit var femaleSelectedBorder: ImageView

    private var gender = "male"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sifaa_activity_gender)

        maleTextviewSelected = findViewById(R.id.male_selected_tv)
        femaleTextviewSelected = findViewById(R.id.female_selected_tv)

        maleSelectedBorder = findViewById(R.id.male_selected_circle)
        femaleSelectedBorder = findViewById(R.id.female_selected_circle)

        val name = intent?.getStringExtra("name")!!
        val uid = intent?.getStringExtra("uid")!!

        findViewById<TextView>(R.id.gender_name_tv).text = "${name.split(" ")[0]}" //displaying first name

        findViewById<ExtendedFloatingActionButton>(R.id.gender_save_btn).setOnClickListener{
            saveGenderToDatabase(uid)
        }
    }

    private fun saveGenderToDatabase(uid: String) {
        val dbRef = FirebaseDatabase.getInstance().reference.child("employees").child(uid)
        dbRef.child("gender").setValue(this.gender)

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun onSelectMale(view: View) {
        gender = "male"
        maleTextviewSelected.setTextColor(resources.getColor(R.color.purple_enabled_color))
        femaleTextviewSelected.setTextColor(resources.getColor(R.color.purple_disabled_color))

        maleSelectedBorder.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.purple_enabled_color
            ), android.graphics.PorterDuff.Mode.MULTIPLY
        )
        femaleSelectedBorder.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.purple_disabled_color
            ), android.graphics.PorterDuff.Mode.MULTIPLY
        )
    }

    fun onSelectFemale(view: View) {
        gender = "female"
        femaleTextviewSelected.setTextColor(resources.getColor(R.color.purple_enabled_color))
        maleTextviewSelected.setTextColor(resources.getColor(R.color.purple_disabled_color))

        femaleSelectedBorder.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.purple_enabled_color
            ), android.graphics.PorterDuff.Mode.MULTIPLY
        )
        maleSelectedBorder.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.purple_disabled_color
            ), android.graphics.PorterDuff.Mode.MULTIPLY
        )
    }

}