package com.example.testfloral;

import com.google.gson.annotations.SerializedName;

public class Growth {

    @SerializedName("payment_type")
    private int payment_type;
    @SerializedName("num_orders")
    private Float num_orders;

    public int getYear() {
        return payment_type;
    }

    public Float getGrowth() {
        return num_orders;
    }
}
