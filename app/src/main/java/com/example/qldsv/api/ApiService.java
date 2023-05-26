package com.example.qldsv.api;

import com.example.qldsv.model.Lop;
import com.example.qldsv.model.TaiKhoan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("api/v1/sinhVien/getDDDSCoTK")
    Call<List<TaiKhoan>> getListTK();

    @GET("api/v1/lop/getDSLop")
    Call<List<Lop>> getListLop();

    @FormUrlEncoded
    @POST("api/v1/sinhVien/getDSSVCoTKTheoMaLop")
    Call<List<TaiKhoan>> getListTKTheoLop(@Field("MaLop") String MaLop );

    @GET("api/v1/sinhVien/getDSSVChuaCoTK")
    Call<List<String>> getListSVChuaCoTK();

    @FormUrlEncoded
    @POST("api/v1/taiKhoan/TaoTaiKhoan")
    Call<Object> taoTaiKhoan(@Field("MaTk") String MaTk,
                                     @Field("TenTaiKhoan") String TenTaiKhoan,
                                     @Field("MatKhau") String MatKhau);

    @FormUrlEncoded
    @POST("api/v1/taiKhoan/suaTaiKhoan")
    Call<Object> suaTaiKhoan(@Field("MaTk") String MaTk,
                             @Field("TenTaiKhoan") String TenTaiKhoan,
                             @Field("MatKhau") String MatKhau);

    @FormUrlEncoded
    @POST("api/v1/taiKhoan/getTKTheoMaTK")
    Call<TaiKhoan> getInfoTaiKhoan(@Field("MaTk") String MaTk );

    @FormUrlEncoded
    @POST("api/v1/taiKhoan/xoaTaiKhoan")
    Call<Object> xoaTaiKhoan(@Field("MaTk") String MaTk);
}
