//package com.example.testfloral;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.components.Description;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.utils.ColorTemplate;
//import com.pro.cafy_theofficecafeteria.R;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
//public class ChartFragment extends Fragment {
//
//    private BarChart mBarChart;
//    //private PieChart mPieChart;
//
//
//    public ChartFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_chart, container, false);
//
//        mBarChart = view.findViewById(R.id.barChart);
//      //  mBarChart = view.findViewById(R.id.pieChart);
//        assert getArguments() != null;
//        getGrowthChart(getArguments().getString("method"));
//
//
//        return view;
//
//    }
//
//    private void getGrowthChart(String method){
//        Call<List<Growth>> call = ApiClient.getApiClient().create(ApiInterface.class).getGrowthInfo();
//        call.enqueue(new Callback<List<Growth>>() {
//            @Override
//            public void onResponse(@NotNull Call<List<Growth>> call, @NotNull Response<List<Growth>> response) {
//                if(response.body() != null)
//                {
//                    if(method.equals("bars")){
//                        List<BarEntry> barEntries = new ArrayList<>();
//                        for(Growth growth : response.body()){
//                            barEntries.add(new BarEntry(growth.getYear(),growth.getGrowth()));
//                        }
//
//                        BarDataSet barDataSet = new BarDataSet(barEntries,"Growth");
//                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//
//                        BarData barData = new BarData(barDataSet);
//                        barData.setBarWidth(0.9f);
//
//                        mBarChart.setVisibility(View.VISIBLE);
//                        mBarChart.animateY(5000);
//                        mBarChart.setData(barData);
//                        mBarChart.setFitBars(true);
//
//                        Description description = new Description();
//                        description.setText("Growth Rate Per Year");
//                        mBarChart.setDescription(description);
//                        mBarChart.invalidate();
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<List<Growth>> call, @NotNull Throwable t) {
//
//            }
//        });
//    }
//}