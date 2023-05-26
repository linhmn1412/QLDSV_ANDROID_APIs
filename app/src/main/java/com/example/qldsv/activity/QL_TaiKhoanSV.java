package com.example.qldsv.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qldsv.R;
import com.example.qldsv.adapter.TaiKhoanAdapter;
import com.example.qldsv.api.ApiManager;
import com.example.qldsv.model.Lop;
import com.example.qldsv.model.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class QL_TaiKhoanSV extends AppCompatActivity {
    Button btnClickback;
    Button btnThemTaiKhoan ,btnSuaTaiKhoan, btnXoaTaiKhoan;
    RecyclerView rcv_TK;

    List<TaiKhoan> mListTK = new ArrayList<>();
    Spinner spinnerLop;

    SearchView searchTK;
    List<TaiKhoan> mListTKTheoLop = new ArrayList<>();
    ArrayList<String> listSpinnerLop = new ArrayList<>();

    List<Lop> mListLop = new ArrayList<>();
    int chosingIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qltaikhoansv);

        rcv_TK = (RecyclerView)  findViewById(R.id.rcv_taikhoan);
        rcv_TK.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this);
        rcv_TK.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcv_TK.addItemDecoration(itemDecoration);

        spinnerLop = (Spinner) findViewById(R.id.spinnerLop);
        searchTK = findViewById(R.id.searchTaiKhoan);


        loadLop();

        spinnerLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tenLop = listSpinnerLop.get(i);
                if(getMaLop(tenLop).equals("")){
                    loadTaiKhoan();
                    searchTaiKhoan();

                }
                else {

                    loadListTKTheoLop(getMaLop(tenLop));
                    searchTaiKhoanTheoLop();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnClickback = findViewById(R.id.btnClickback);
        btnClickback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QL_TaiKhoanSV.this, MainActivity.class);
                startActivity(intent);
            }

        });

        btnThemTaiKhoan = findViewById(R.id.btnThemTK);
        btnThemTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QL_TaiKhoanSV.this, TaoTaiKhoanSV.class);
                startActivity(intent);
                finish();
            }
        });

        btnSuaTaiKhoan = findViewById(R.id.btnSuaTK);
        btnSuaTaiKhoan.setEnabled(false);

        btnXoaTaiKhoan = findViewById(R.id.btnXoaTK);
        btnXoaTaiKhoan.setEnabled(false);
    }


    public void loadTaiKhoan() {

        mListTK.clear();
        ApiManager apiManager = ApiManager.getInstance();
        Call<List<TaiKhoan>> call = apiManager.getApiService().getListTK();
        call.enqueue(new Callback<List<TaiKhoan>>() {
            @Override
            public void onResponse(Call<List<TaiKhoan>> call, Response<List<TaiKhoan>> response) {
                if(response.body()!= null && response.isSuccessful()){

                        mListTK = response.body();

                        TaiKhoanAdapter taiKhoanAdapter = new TaiKhoanAdapter(mListTK, new TaiKhoanAdapter.ItemClickListener() {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            @Override
                            public void OnItemClick(View view, TaiKhoan taikhoan, int i) {
                                if(chosingIndex == -1) {
                                  view.setBackground(getDrawable(R.color.background2nd));


                                    btnSuaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                    btnSuaTaiKhoan.setEnabled(true);
                                    btnXoaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                    btnXoaTaiKhoan.setEnabled(true);


                                    chosingIndex = i;
                                }
                                else if (chosingIndex != i) {
                                    RecyclerView.LayoutManager layoutManager = rcv_TK.getLayoutManager();
                                    layoutManager.getChildAt(chosingIndex).setBackground(null);
                                    view.setBackground(getDrawable(R.color.background2nd));
                                    chosingIndex = i;
                                }
                                else {
                                        view.setBackground(null);
                                        btnSuaTaiKhoan.setEnabled(false);
                                        btnXoaTaiKhoan.setEnabled(false);
                                        chosingIndex = -1;
                                }
                                btnSuaTaiKhoan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(QL_TaiKhoanSV.this, CT_TaiKhoanSV.class);
                                        intent.putExtra("MaTk", mListTK.get(i).getMaTk());
                                        startActivity(intent);
                                    }
                                });


                                btnXoaTaiKhoan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        XoaTaiKhoan(i);
                                        btnXoaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                        btnSuaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                        view.setBackground(null);
                                    }
                                });
                            }
                        });
                        rcv_TK.setAdapter(taiKhoanAdapter);



                }

            }

            @Override
            public void onFailure(Call<List<TaiKhoan>> call, Throwable t) {
                Toast.makeText(QL_TaiKhoanSV.this, "call api fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getMaLop(String tenLop) {
        String maLop = "";
        for (int i = 0; i < mListLop.size(); i++) {
            if (tenLop.equals(mListLop.get(i).getTenLop())) {
                maLop = mListLop.get(i).getMaLop();
            }
        }
        return maLop;
    }

    public void loadLop() {
        mListLop.clear();
        listSpinnerLop.clear();
        listSpinnerLop.add("Chọn lớp");
        ApiManager apiManager = ApiManager.getInstance();
        Call<List<Lop>> call = apiManager.getApiService().getListLop();
        call.enqueue(new Callback<List<Lop>>() {
            @Override
            public void onResponse(Call<List<Lop>> call, Response<List<Lop>> response) {
                if(response.body()!= null && response.isSuccessful()){
                    mListLop = response.body();
                    for(int i = 0 ; i < mListLop.size(); i++){
                        listSpinnerLop.add(mListLop.get(i).getTenLop());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Lop>> call, Throwable t) {
                Toast.makeText(QL_TaiKhoanSV.this, "call api fail", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner,listSpinnerLop);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerLop.setAdapter(adapter);
    }

    public void loadListTKTheoLop( String maLop) {

        mListTKTheoLop.clear();
        ApiManager apiManager = ApiManager.getInstance();
        Call<List<TaiKhoan>> call = apiManager.getApiService().getListTKTheoLop(maLop);
        call.enqueue(new Callback<List<TaiKhoan>>() {
            @Override
            public void onResponse(Call<List<TaiKhoan>> call, Response<List<TaiKhoan>> response) {
                if(response.body()!= null && response.isSuccessful()){
                    mListTKTheoLop = response.body();
                    TaiKhoanAdapter taiKhoanAdapter = new TaiKhoanAdapter(mListTKTheoLop, new TaiKhoanAdapter.ItemClickListener() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void OnItemClick(View view, TaiKhoan taikhoan, int i) {
                            view.setBackground(getDrawable(R.color.background2nd));

                            btnSuaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                            btnSuaTaiKhoan.setEnabled(true);
                            btnSuaTaiKhoan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(QL_TaiKhoanSV.this, CT_TaiKhoanSV.class);
                                    intent.putExtra("MaTk", mListTK.get(i).getMaTk());
                                    startActivity(intent);
                                }
                            });

                            btnXoaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                            btnXoaTaiKhoan.setEnabled(true);
                            btnXoaTaiKhoan.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("UseCompatLoadingForDrawables")
                                @Override
                                public void onClick(View view) {
                                    XoaTaiKhoan(i);
                                    btnXoaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                    btnSuaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                    view.setBackground(null);
                                }
                            });
                        }
                    });
                    rcv_TK.setAdapter(taiKhoanAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<TaiKhoan>> call, Throwable t) {
                Toast.makeText(QL_TaiKhoanSV.this, "call api fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchTaiKhoan() {
        searchTK.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                List<TaiKhoan> listSearchTK = new ArrayList<>();
                for (int i = 0; i < mListTK.size(); i++) {
                    if (mListTK.get(i).getHoTen().toLowerCase().contains(newText.toLowerCase()) || mListTK.get(i).getMaTk().toLowerCase().contains(newText.toLowerCase())) {
                        listSearchTK.add(mListTK.get(i));

                    }
                    TaiKhoanAdapter taiKhoanAdapter = new TaiKhoanAdapter(listSearchTK, new TaiKhoanAdapter.ItemClickListener() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void OnItemClick(View view, TaiKhoan taikhoan, int i) {
                            if(chosingIndex == -1) {
                                view.setBackground(getDrawable(R.color.background2nd));


                                btnSuaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                btnSuaTaiKhoan.setEnabled(true);
                                btnXoaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                btnXoaTaiKhoan.setEnabled(true);


                                chosingIndex = i;
                            }
                            else if (chosingIndex != i) {
                                RecyclerView.LayoutManager layoutManager = rcv_TK.getLayoutManager();
                                layoutManager.getChildAt(chosingIndex).setBackground(null);
                                view.setBackground(getDrawable(R.color.background2nd));
                                chosingIndex = i;
                            }
                            else {
                                view.setBackground(null);
                                btnSuaTaiKhoan.setEnabled(false);
                                btnXoaTaiKhoan.setEnabled(false);
                                chosingIndex = -1;
                            }
                            btnSuaTaiKhoan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(QL_TaiKhoanSV.this, CT_TaiKhoanSV.class);
                                    intent.putExtra("MaTk", mListTK.get(i).getMaTk());
                                    startActivity(intent);
                                }
                            });


                            btnXoaTaiKhoan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    XoaTaiKhoan(i);
                                    btnXoaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                    btnSuaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                    view.setBackground(null);
                                }
                            });
                        }
                    });
                    rcv_TK.setAdapter(taiKhoanAdapter);
                }
                return false;
            }
        });
    }

        public void searchTaiKhoanTheoLop() {
            searchTK.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    List<TaiKhoan> listSearchTK = new ArrayList<>();
                    for(int i = 0; i < mListTKTheoLop.size(); i++){
                        if (mListTKTheoLop.get(i).getHoTen().toLowerCase().contains(newText.toLowerCase())) {
                            listSearchTK.add(mListTKTheoLop.get(i));

                        }
                        TaiKhoanAdapter taiKhoanAdapter = new TaiKhoanAdapter(listSearchTK, new TaiKhoanAdapter.ItemClickListener() {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            @Override
                            public void OnItemClick(View view, TaiKhoan taikhoan, int i) {
                                if(chosingIndex == -1) {
                                    view.setBackground(getDrawable(R.color.background2nd));


                                    btnSuaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                    btnSuaTaiKhoan.setEnabled(true);
                                    btnXoaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                    btnXoaTaiKhoan.setEnabled(true);


                                    chosingIndex = i;
                                }
                                else if (chosingIndex != i) {
                                    RecyclerView.LayoutManager layoutManager = rcv_TK.getLayoutManager();
                                    layoutManager.getChildAt(chosingIndex).setBackground(null);
                                    view.setBackground(getDrawable(R.color.background2nd));
                                    chosingIndex = i;
                                }
                                else {
                                    view.setBackground(null);
                                    btnSuaTaiKhoan.setEnabled(false);
                                    btnXoaTaiKhoan.setEnabled(false);
                                    chosingIndex = -1;
                                }
                                btnSuaTaiKhoan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(QL_TaiKhoanSV.this, CT_TaiKhoanSV.class);
                                        intent.putExtra("MaTk", mListTK.get(i).getMaTk());
                                        startActivity(intent);
                                    }
                                });


                                btnXoaTaiKhoan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        XoaTaiKhoan(i);
                                        btnXoaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                        btnSuaTaiKhoan.setBackground(getDrawable(R.drawable.buttonmain));
                                        view.setBackground(null);
                                    }
                                });
                            }
                        });
                        rcv_TK.setAdapter(taiKhoanAdapter);
                    }
                    return false;
                }
            });
    }


    public void XoaTaiKhoan(int i) {
        String lop = spinnerLop.getSelectedItem().toString();
        AlertDialog.Builder bulider = new AlertDialog.Builder(QL_TaiKhoanSV.this);
        bulider.setMessage("Xác nhận xoá tài khoản này?");
        bulider.setCancelable(true);
        bulider.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String matk = mListTK.get(i).getMaTk();
                        ApiManager apiManager = ApiManager.getInstance();
                        Call<Object> call = apiManager.getApiService().xoaTaiKhoan(matk);
                        call.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                if(response.body()!= null && response.isSuccessful()){
                                    if(getMaLop(lop) == ""){
                                        loadTaiKhoan();
                                    }
                                    else{
                                        loadListTKTheoLop(getMaLop(lop));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                Toast.makeText(QL_TaiKhoanSV.this, "call api getInfoTK fail", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

        bulider.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = bulider.create();
        alert.show();
        btnXoaTaiKhoan.setEnabled(false);
        btnSuaTaiKhoan.setEnabled(false);
    }


}
