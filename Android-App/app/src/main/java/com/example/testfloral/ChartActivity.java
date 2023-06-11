//package com.example.testfloral;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//import com.pro.cafy_theofficecafeteria.R;
//
//public class ChartActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chart);
//
//        String method = getIntent().getStringExtra("method");
//        ChartFragment chartFragment = new ChartFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("method",method);
//        chartFragment.setArguments(bundle);
//
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,chartFragment).commit();
//    }
//}