package com.vms.antplay.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.vms.antplay.R;
import com.vms.antplay.adapter.PaymentHistory_Adapter;
import com.vms.antplay.api.APIClient;
import com.vms.antplay.api.RetrofitAPI;
import com.vms.antplay.model.responseModal.Payment;
import com.vms.antplay.model.responseModal.PaymentHistory_modal;
import com.vms.antplay.utils.AppUtils;
import com.vms.antplay.utils.Const;
import com.vms.antplay.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistory extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Payment> paymentHistory_modals;
    LinearLayout linear_back;
    private ProgressBar loadingPB;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark_light, this.getTheme()));

        recyclerView=(RecyclerView) findViewById(R.id.recyclerView_payment);
        linear_back= (LinearLayout) findViewById(R.id.back_linear_payment);
        loadingPB = (ProgressBar) findViewById(R.id.loading_getPaymentHistory);
        linear_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        paymentHistory_modals = new ArrayList<com.vms.antplay.model.responseModal.Payment>();

        callPaymentHistoryAPI();


    }


    private void callPaymentHistoryAPI() {
        loadingPB.setVisibility(View.VISIBLE);
        RetrofitAPI retrofitAPI = APIClient.getRetrofitInstance().create(RetrofitAPI.class);
        Call<PaymentHistory_modal> call = retrofitAPI.getPaymentHistory("Bearer " + SharedPreferenceUtils.getString(PaymentHistory.this, Const.ACCESS_TOKEN));
        call.enqueue(new Callback<PaymentHistory_modal>() {
            @Override
            public void onResponse(Call<PaymentHistory_modal> call, Response<PaymentHistory_modal> response) {

                if (response.isSuccessful()) {
                        loadingPB.setVisibility(View.GONE);
                        paymentHistory_modals = response.body().getData();
                        PaymentHistory_Adapter paymentHistory_adapter = new PaymentHistory_Adapter(PaymentHistory.this, paymentHistory_modals);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(PaymentHistory.this, 1);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(paymentHistory_adapter);

                } else {
                    loadingPB.setVisibility(View.GONE);
                    AppUtils.showToast(Const.no_records, PaymentHistory.this);
                }
            }

            @Override
            public void onFailure(Call<PaymentHistory_modal> call, Throwable t) {
                Log.e("Hello Get VM", "Failure");
               // loadingPB.setVisibility(View.GONE);
                AppUtils.showToast(Const.something_went_wrong, PaymentHistory.this);

            }
        });
    }
}