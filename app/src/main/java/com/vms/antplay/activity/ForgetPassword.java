package com.vms.antplay.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.vms.antplay.R;
import com.vms.antplay.api.APIClient;
import com.vms.antplay.api.RetrofitAPI;
import com.vms.antplay.model.requestModal.ForgotPassRequestModal;
import com.vms.antplay.model.requestModal.LoginRequestModal;
import com.vms.antplay.model.responseModal.ForgotPassResponseModal;
import com.vms.antplay.model.responseModal.LoginResponseModel;
import com.vms.antplay.utils.AppUtils;
import com.vms.antplay.utils.Const;
import com.vms.antplay.utils.SharedPreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {

    EditText etEmailForget;
    Button btnForgetPass;
    TextView tvTermsCondition;
    boolean isAllFieldChecked = false;
    private ProgressBar loadingPB;
    AlertDialog.Builder builder;
    private String TAG = "ForgotPassword Screen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);
        builder = new AlertDialog.Builder(this);
        etEmailForget = (EditText) findViewById(R.id.et_emailForget);
        btnForgetPass = (Button) findViewById(R.id.btn_resetPass);
        tvTermsCondition = (TextView) findViewById(R.id.tv_termsAndCondition_forget);
        loadingPB = (ProgressBar) findViewById(R.id.loadingForgot_progress_xml);

        tvTermsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPassword.this, TermsAndCondition.class);
                startActivity(intent);
            }
        });

        btnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldChecked = checkEmail();
                if (isAllFieldChecked) {
                    // Call Reset Password API
                    //  Toast.makeText(ForgetPassword.this, "Thank You", Toast.LENGTH_SHORT).show();
                    callForgotPass(etEmailForget.getText().toString());
                }
            }
        });

    }

    private void callForgotPass(String email) {
        loadingPB.setVisibility(View.VISIBLE);
        RetrofitAPI retrofitAPI = APIClient.getRetrofitInstance().create(RetrofitAPI.class);
        ForgotPassRequestModal forgotPassRequestModal = new ForgotPassRequestModal(email);
        Call<ForgotPassResponseModal> call = retrofitAPI.forgotPassword(forgotPassRequestModal);
        call.enqueue(new Callback<ForgotPassResponseModal>() {
            @Override
            public void onResponse(Call<ForgotPassResponseModal> call, Response<ForgotPassResponseModal> response) {
                if (response.code() == Const.SUCCESS_CODE_200) {
                    loadingPB.setVisibility(View.GONE);
                    Log.d(TAG, "" + response.body().getMessage());
                    String emailLink = response.body().getData().getEmailBody();
                    String[] splitString = emailLink.split("password-reset/");
                    /*Log.e("Hello email link forgot",""+splitString[0]+", "+splitString[1]);
                    Log.e("Hello  forgot_new--",""+splitString[1]);*/
                    builder.setMessage("We have sent you a link on your Email to reset your password")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.setTitle("Reset Password");
                    alert.show();


                } else if (response.code() == Const.ERROR_CODE_404) {
                    loadingPB.setVisibility(View.GONE);
                    AppUtils.showSnack(getWindow().getDecorView().getRootView(), R.color.black, response.message(), ForgetPassword.this);
                } else if (response.code() == Const.ERROR_CODE_500) {
                    loadingPB.setVisibility(View.GONE);
                    AppUtils.showSnack(getWindow().getDecorView().getRootView(), R.color.black, Const.NOT_REGISTERED_EMAIL, ForgetPassword.this);
                } else if (response.code() == Const.ERROR_CODE_400) {
                    loadingPB.setVisibility(View.GONE);
                    AppUtils.showSnack(getWindow().getDecorView().getRootView(), R.color.black, getString(R.string.wrong_email_id), ForgetPassword.this);
                } else {
                    loadingPB.setVisibility(View.GONE);
                    Log.e(TAG, "Else condition");
                    AppUtils.showToast(Const.no_records, ForgetPassword.this);
                }
            }

            @Override
            public void onFailure(Call<ForgotPassResponseModal> call, Throwable t) {
                Log.e(TAG, "" + t);
                loadingPB.setVisibility(View.GONE);
                AppUtils.showToast(Const.something_went_wrong, ForgetPassword.this);
            }
        });
    }

    private boolean checkEmail() {
        String emailPattern = "^(?:\\d{10}|\\w+@\\w+\\.\\w{2,3})$";
        if (etEmailForget.getText().toString().trim().length() == 0) {
            etEmailForget.setError(getString(R.string.error_email));
            return false;
        } else if (!etEmailForget.getText().toString().matches(emailPattern)) {
            etEmailForget.setError(getString(R.string.error_invalidEmail));
            return false;
        }
        return true;
    }
}