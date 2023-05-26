package com.example.qldsv.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qldsv.R;
import com.example.qldsv.api.ApiManager;
import com.example.qldsv.model.Lop;
import com.example.qldsv.model.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CT_TaiKhoanSV extends AppCompatActivity {
    Connection conn;
    String MaTk;
    Button btnCapNhat, btnClickBack;
    TextView txtMaTK;
    EditText txtTenTK, txtMatKhau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ct_taikhoansv);
        Intent intent = getIntent();
        MaTk = intent.getStringExtra("MaTk");
        loadInfoTaiKhoan(MaTk);

        txtMaTK = findViewById(R.id.txtMaTK);
        txtTenTK = findViewById(R.id.txtTenTK);
        txtMatKhau = findViewById(R.id.txtMatKhau);

        btnClickBack = findViewById(R.id.btnClickBack);
        btnCapNhat = findViewById(R.id.btnCapNhat);



        btnClickBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CT_TaiKhoanSV.this, QL_TaiKhoanSV.class);
                startActivity(intent);
                finish();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmpty(txtTenTK)) {
                    txtTenTK.setError("Tài khoản không được bỏ trống.");
                } else if (checkEmpty(txtMatKhau)) {
                    txtMatKhau.setError("Mật khẩu không được bỏ trống.");
                } else {

                    ApiManager apiManager = ApiManager.getInstance();
                    Call<Object> call = apiManager.getApiService().suaTaiKhoan(MaTk,txtTenTK.getText().toString().trim(), txtMatKhau.getText().toString().trim());
                    call.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            if(response.body()!= null && response.isSuccessful()){
                                alertSuccess();
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Toast.makeText(CT_TaiKhoanSV.this, "call api getInfoTK fail", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

    }
    public void loadInfoTaiKhoan(String MaTk) {
        ApiManager apiManager = ApiManager.getInstance();
        Call<TaiKhoan> call = apiManager.getApiService().getInfoTaiKhoan(MaTk);
        call.enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                if(response.body()!= null && response.isSuccessful()){
                    TaiKhoan tk = response.body();
                    txtMaTK.setText(tk.getMaTk());
                    txtTenTK.setText(tk.getTenTaiKhoan());
                    txtMatKhau.setText(tk.getMatKhau());
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable t) {
                Toast.makeText(CT_TaiKhoanSV.this, "call api getInfoTK fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean checkEmpty(EditText edt) {
        CharSequence check = edt.getText().toString();
        return TextUtils.isEmpty(check);
    }
    public void alertSuccess() {
        AlertDialog.Builder bulider = new AlertDialog.Builder(CT_TaiKhoanSV.this);
        bulider.setMessage("Cập nhật tài khoản thành công.");
        bulider.setCancelable(true);
        bulider.setPositiveButton("Đồng ý",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intent = new Intent(CT_TaiKhoanSV.this, QL_TaiKhoanSV.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alert = bulider.create();
        alert.show();
    }
}
