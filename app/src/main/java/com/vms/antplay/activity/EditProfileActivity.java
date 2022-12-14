package com.vms.antplay.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.vms.antplay.R;
import com.vms.antplay.api.APIClient;
import com.vms.antplay.api.RetrofitAPI;
import com.vms.antplay.model.requestModal.ChangePasswordRequestModal;
import com.vms.antplay.model.requestModal.UserUpdateRequestModal;
import com.vms.antplay.model.responseModal.ChangePasswordResponseModal;
import com.vms.antplay.model.responseModal.UserDetailsModal;
import com.vms.antplay.model.responseModal.UserUpdateResponseModal;
import com.vms.antplay.utils.AppUtils;
import com.vms.antplay.utils.Const;
import com.vms.antplay.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private String TAG = "ANT_PLAY";
    LinearLayout linearLayout;
    EditText edTxtName, edTxtUserName, edTxtPhoneNumber, edTxtEmail, edTxtAge, edTxtCity, edTxtAddress, edTxtState, editTextPinCode;
    Button buttonUpdateProfile;
    Spinner spinnerStateList;
    private ProgressBar progressBar;
    List<String> stateList = new ArrayList<String>();
    String st_state;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark_light, this.getTheme()));

        edTxtName = findViewById(R.id.edTxtFullName);
        edTxtUserName = findViewById(R.id.edTxtUserName);
        edTxtPhoneNumber = findViewById(R.id.edTxtPhoneNumber);
        edTxtEmail = findViewById(R.id.edTxtEmail);
        edTxtAge = findViewById(R.id.edTxtAge);
        edTxtCity = findViewById(R.id.edTxtCity);
        edTxtAddress = findViewById(R.id.edTxtAddress);
        editTextPinCode = findViewById(R.id.edTxtPinCode);

        spinnerStateList = findViewById(R.id.spinnerStateList);


        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);
        progressBar = (ProgressBar) findViewById(R.id.progressBarEditProfile);

       // setData();
        populateStateList();
        linearLayout = (LinearLayout) findViewById(R.id.back_linear_edit);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (edTxtCity.getText().toString().trim().length() ==0 ) {
                    edTxtCity.setError(getString(R.string.remove_whitespace));
                }*/
                if (edTxtCity.getText().toString().trim().length() == 0) {
                    AppUtils.showSnack(v, R.color.black, Const.city_should_not_empty, EditProfileActivity.this);
                } else if (edTxtAddress.getText().toString().trim().length() == 0) {
                    AppUtils.showSnack(v, R.color.black, Const.address_should_not_empty, EditProfileActivity.this);
                } else if (!editTextPinCode.getText().toString().matches(Const.pinCodeRegex)) {
                    AppUtils.showSnack(v, R.color.black, Const.enter_valid_picCode, EditProfileActivity.this);
                } else {
                    updateUserProfile();
                }

            }
        });
    }

    private void populateStateList() {
        // Spinner Drop down elements
        stateList.add("Andaman and Nicobar Islands");
        stateList.add("Andhra Pradesh");
        stateList.add("Arunachal Pradesh");
        stateList.add("Assam");
        stateList.add("Bihar");
        stateList.add("Chandigarh");
        stateList.add("Chhattisgarh");
        stateList.add("Dadra and Nagar Haveli");
        stateList.add("Daman and Diu");
        stateList.add("Delhi");
        stateList.add("Goa");
        stateList.add("Gujarat");
        stateList.add("Haryana");
        stateList.add("Himachal Pradesh");
        stateList.add("Jammu and Kashmir");
        stateList.add("Jharkhand");
        stateList.add("Karnataka");
        stateList.add("Kerala");
        stateList.add("Ladakh");
        stateList.add("Lakshadweep");
        stateList.add("Madhya Pradesh");
        stateList.add("Maharashtra");
        stateList.add("Manipur");
        stateList.add("Meghalaya");
        stateList.add("Mizoram");
        stateList.add("Nagaland");
        stateList.add("Odisha");
        stateList.add("Puducherry");
        stateList.add("Punjab");
        stateList.add("Rajasthan");
        stateList.add("Sikkim");
        stateList.add("Tamil Nadu");
        stateList.add("Telangana");
        stateList.add("Tripura");
        stateList.add("Uttar Pradesh");
        stateList.add("Uttarakhand");
        stateList.add("West Bengal");

        setStateAdopter();
        setData();
    }

    private void setStateAdopter() {
        // Creating adapter for spinner
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateList);

        // Drop down layout style - list view with radio button
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerStateList.setAdapter(stateAdapter);
        spinnerStateList.setPrompt("your state here");

        spinnerStateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                st_state = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
               // Toast.makeText(parent.getContext(), "Selected: " + st_state, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setData() {
        String fullName = SharedPreferenceUtils.getString(EditProfileActivity.this, Const.FULL_NAME);
        String email = SharedPreferenceUtils.getString(EditProfileActivity.this, Const.USER_EMAIL);
        String phoneNumber = SharedPreferenceUtils.getString(EditProfileActivity.this, Const.PHONE);
        String address = SharedPreferenceUtils.getString(EditProfileActivity.this, Const.ADDRESS);
        String state = SharedPreferenceUtils.getString(EditProfileActivity.this, Const.STATE);
        String city = SharedPreferenceUtils.getString(EditProfileActivity.this, Const.CITY);
        String userName = SharedPreferenceUtils.getString(EditProfileActivity.this, Const.USER_NAME);
        String pinCode = SharedPreferenceUtils.getString(EditProfileActivity.this, Const.PIN_CODE);
        Log.d(TAG, "" + phoneNumber+" "+email);

        if (fullName != null) {
            edTxtName.setText(fullName);
        }
        if (userName != null) {
            edTxtUserName.setText(userName);
        }
        if (phoneNumber != null) {
            edTxtPhoneNumber.setText(phoneNumber);
        }
        if (email != null) {
            edTxtEmail.setText(email);
        }
        if (city != null) {
            edTxtCity.setText(city);
        }
        if (address != null) {
            edTxtAddress.setText(address);
        }
        if (pinCode != null) {
            editTextPinCode.setText(pinCode);
        }

      //  spinnerStateList.setSelection(state.);

        for (int i = 0; i < stateList.size(); i++) {
            if (stateList.get(i).equals(state)) {
                spinnerStateList.setSelection(i);
            }
        }

        // edTxtState.setText(state);
    }


    private void updateUserProfile() {
        progressBar.setVisibility(View.VISIBLE);
        String access_token = SharedPreferenceUtils.getString(EditProfileActivity.this, Const.ACCESS_TOKEN);
        RetrofitAPI retrofitAPI = APIClient.getRetrofitInstance().create(RetrofitAPI.class);
        UserUpdateRequestModal updateRequestModal = new UserUpdateRequestModal(
                edTxtAddress.getText().toString().trim(),
                st_state,
                edTxtCity.getText().toString().trim(),
                editTextPinCode.getText().toString());
        Call<UserUpdateResponseModal> call = retrofitAPI.userUpdate("Bearer " + access_token, updateRequestModal);
        call.enqueue(new Callback<UserUpdateResponseModal>() {
            @Override
            public void onResponse(Call<UserUpdateResponseModal> call, Response<UserUpdateResponseModal> response) {
                if (response.code() == Const.SUCCESS_CODE_200) {
                    progressBar.setVisibility(View.GONE);
                    getUserDetails();
                    AppUtils.showSnack(getWindow().getDecorView().getRootView(), R.color.black, Const.profile_updated_success, EditProfileActivity.this);
                    finish();

                } else if (response.code() == Const.ERROR_CODE_400) {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Else condition");
                    AppUtils.showSnack(getWindow().getDecorView().getRootView(), R.color.black, Const.enter_valid_data, EditProfileActivity.this);
                } else if (response.code() == Const.ERROR_CODE_500) {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Else condition");
                    AppUtils.showSnack(getWindow().getDecorView().getRootView(), R.color.black, Const.enter_valid_data, EditProfileActivity.this);
                } else {
                    progressBar.setVisibility(View.GONE);
                    AppUtils.showSnack(getWindow().getDecorView().getRootView(), R.color.black, Const.something_went_wrong, EditProfileActivity.this);
                }
            }

            @Override
            public void onFailure(Call<UserUpdateResponseModal> call, Throwable t) {
                Log.d("BILLING_PLAN", "Failure");
                progressBar.setVisibility(View.GONE);
                AppUtils.showSnack(getWindow().getDecorView().getRootView(), R.color.black, Const.something_went_wrong, EditProfileActivity.this);

            }
        });
    }

    private void getUserDetails() {
        String access_token = SharedPreferenceUtils.getString(EditProfileActivity.this, Const.ACCESS_TOKEN);
        RetrofitAPI retrofitAPI = APIClient.getRetrofitInstance().create(RetrofitAPI.class);
        Call<UserDetailsModal> call = retrofitAPI.getUserDetails("Bearer " + access_token);
        call.enqueue(new Callback<UserDetailsModal>() {
            @Override
            public void onResponse(Call<UserDetailsModal> call, Response<UserDetailsModal> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "" + response.body());
                    progressBar.setVisibility(View.GONE);
                    SharedPreferenceUtils.saveString(EditProfileActivity.this, Const.FULL_NAME, response.body().getFirstName() + " " + response.body().getLastName());
                    SharedPreferenceUtils.saveString(EditProfileActivity.this, Const.USER_EMAIL, response.body().getEmail());
                    SharedPreferenceUtils.saveString(EditProfileActivity.this, Const.PHONE, response.body().getPhoneNumber());
                    SharedPreferenceUtils.saveString(EditProfileActivity.this, Const.ADDRESS, response.body().getAddress());
                    SharedPreferenceUtils.saveString(EditProfileActivity.this, Const.STATE, response.body().getState());
                    SharedPreferenceUtils.saveString(EditProfileActivity.this, Const.CITY, response.body().getCity());
                    SharedPreferenceUtils.saveString(EditProfileActivity.this, Const.USER_NAME, response.body().getUsername());
                    SharedPreferenceUtils.saveString(EditProfileActivity.this, Const.PIN_CODE, response.body().getPincode());
                    setData();

                } else {
                    Log.e(TAG, "Else condition");
                    progressBar.setVisibility(View.GONE);
                    AppUtils.showToast(Const.no_records, EditProfileActivity.this);
                }
            }

            @Override
            public void onFailure(Call<UserDetailsModal> call, Throwable t) {
                Log.e(TAG, "" + t);
                progressBar.setVisibility(View.GONE);
                AppUtils.showToast(Const.something_went_wrong, EditProfileActivity.this);
            }
        });

    }

}