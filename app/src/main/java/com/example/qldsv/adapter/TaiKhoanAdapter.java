package com.example.qldsv.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qldsv.R;
import com.example.qldsv.model.TaiKhoan;

import java.util.List;

public class TaiKhoanAdapter extends  RecyclerView.Adapter<TaiKhoanAdapter.TKViewHolder>  {

    private final List<TaiKhoan> mListTK;

    private final ItemClickListener mItemClick;


    public TaiKhoanAdapter(List<TaiKhoan> mListTK, ItemClickListener mItemClick) {
        this.mListTK = mListTK;
        this.mItemClick = mItemClick;
    }

    @NonNull
    @Override
    public TKViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_taikhoan, parent,false);

        return new TKViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TKViewHolder holder, int position) {
        TaiKhoan tk = mListTK.get(position);
        if(tk == null){
            return;
        }
        holder.txtMaTK.setText(tk.getMaTk());
        holder.txtTenTK.setText(tk.getHoTen());

        holder.itemView.setOnClickListener(view -> {
            mItemClick.OnItemClick( view ,tk, position);

        });


    }

    @Override
    public int getItemCount() {
        return mListTK.size();
    }

    public interface ItemClickListener{
        void OnItemClick(View view, TaiKhoan taikhoan, int i);

    }

    public static class TKViewHolder extends RecyclerView.ViewHolder{

        private final TextView txtMaTK, txtTenTK;


        public TKViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaTK = itemView.findViewById(R.id.txtMaTK);
            txtTenTK = itemView.findViewById(R.id.txtTenTK);
        }

    }

}
