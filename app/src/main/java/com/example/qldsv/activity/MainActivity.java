package com.example.qldsv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.qldsv.R;

public class MainActivity extends AppCompatActivity {

    Button btnTaikhoansv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pgv_trangchu);

        btnTaikhoansv = findViewById(R.id.btnTaikhoansv);
        btnTaikhoansv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QL_TaiKhoanSV.class);
                startActivity(intent);
                finish();
            }
        });
    }
}