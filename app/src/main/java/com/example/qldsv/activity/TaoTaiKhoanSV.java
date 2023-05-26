package com.example.qldsv.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qldsv.R;
import com.example.qldsv.adapter.TaiKhoanAdapter;
import com.example.qldsv.api.ApiManager;
import com.example.qldsv.model.Lop;
import com.example.qldsv.model.TaiKhoan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaoTaiKhoanSV extends AppCompatActivity {
    EditText txtTenTK, txtMatKhau;
    Button btnClickBack, btnThem;
    Spinner spinnerMaSV;
    List<String> mlistMaSVChuaCoTK = new ArrayList<>();
    List<String> listSpinnerMaSV = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taotaikhoansv);

        spinnerMaSV = (Spinner)findViewById(R.id.spinnerMaSV) ;
        btnClickBack = findViewById(R.id.btnClickBack);
        btnThem = findViewById(R.id.btnThem);
        txtTenTK = findViewById(R.id.txtTenTK);
        txtMatKhau = findViewById(R.id.txtMatKhau);

        loadSpinnerMSSV();

        btnClickBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaoTaiKhoanSV.this, QL_TaiKhoanSV.class);
                startActivity(intent);
            }

        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maSV = spinnerMaSV.getSelectedItem().toString();
                if(maSV.equals("Chọn mã sinh viên")){
                    AlertDialog.Builder bulider = new AlertDialog.Builder(TaoTaiKhoanSV.this);
                    bulider.setMessage("Vui lòng chọn mã sinh viên.");
                    bulider.setCancelable(true);
                    bulider.setPositiveButton("Đồng ý",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = bulider.create();
                    alert.show();
                }
                else if(checkEmpty(txtTenTK)) {
                    txtTenTK.setError("Vui lòng nhập tên tài khoản.");
                }
                else if(checkEmpty(txtMatKhau)) {
                    txtMatKhau.setError("Vui lòng nhập mật khẩu.");
                }
                else {
                        taoTaiKhoan(maSV,txtTenTK.getText().toString().trim(), txtMatKhau.getText().toString().trim());


                }
            }
        });
    }

    public void loadSpinnerMSSV (){
        mlistMaSVChuaCoTK.clear();
        listSpinnerMaSV.clear();
        listSpinnerMaSV.add("Chọn mã sinh viên");
        ApiManager apiManager = ApiManager.getInstance();
        Call<List<String>> call = apiManager.getApiService().getListSVChuaCoTK();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.body()!= null && response.isSuccessful()){
                    mlistMaSVChuaCoTK = response.body();
                    for(int i = 0 ; i < mlistMaSVChuaCoTK.size() ; i++){
                        listSpinnerMaSV.add(mlistMaSVChuaCoTK.get(i));

                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(TaoTaiKhoanSV.this, "call api fail", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner,listSpinnerMaSV);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerMaSV.setAdapter(adapter);
    }



    public void taoTaiKhoan(String MaTk, String TenTaiKhoan, String MatKhau) {
         boolean check = false;
        ApiManager apiManager = ApiManag.er.getInstance();
        Call<Object> call = apiManager.getApiService().taoTaiKhoan(MaTk, TenTaiKhoan, MatKhau);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.body() != null && response.isSuccessful()){
                    alertSuccess();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(TaoTaiKhoanSV.this, "call api TaoTaiKhoan  fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    boolean checkEmpty(EditText edt) {
        CharSequence check = edt.getText().toString();
        return TextUtils.isEmpty(check);
    }

    public void alertSuccess() {
        AlertDialog.Builder bulider = new AlertDialog.Builder(TaoTaiKhoanSV.this);
        bulider.setMessage("Thêm tài khoản thành công.");
        bulider.setCancelable(true);
        bulider.setPositiveButton("Đồng ý",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intent = new Intent(TaoTaiKhoanSV.this, QL_TaiKhoanSV.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alert = bulider.create();
        alert.show();
    }
}
