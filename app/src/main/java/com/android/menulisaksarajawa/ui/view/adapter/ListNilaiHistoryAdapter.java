package com.android.menulisaksarajawa.ui.view.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.menulisaksarajawa.databinding.ItemNilaiHistoryBinding;
import com.android.menulisaksarajawa.databinding.ItemRowCharacterBinding;
import com.android.menulisaksarajawa.ui.model.NilaiHistory;
import com.android.menulisaksarajawa.ui.utils.PrefManager;

import java.util.ArrayList;

public class ListNilaiHistoryAdapter extends RecyclerView.Adapter<ListNilaiHistoryAdapter.ListViewHolder> {
    private ArrayList<NilaiHistory> listNilaiHistory;
    private String type;
    private PrefManager prefManager;

    public ListNilaiHistoryAdapter(ArrayList<NilaiHistory> list) {
        this.listNilaiHistory = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNilaiHistoryBinding binding = ItemNilaiHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        prefManager = new PrefManager(parent.getContext());

        return new ListViewHolder(binding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        NilaiHistory nilai = listNilaiHistory.get(position);

        holder.binding.tvTitleNilai.setText("Nilai Aksara "+type);
        holder.binding.tvNilai.setText(nilai.getNilai());
        holder.binding.startTime.setText("Mulai : "+nilai.getStart());
        holder.binding.endTime.setText("Selesai : "+nilai.getEnd());
    }

    @Override
    public int getItemCount() {
        return listNilaiHistory.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ItemNilaiHistoryBinding binding;

        ListViewHolder(ItemNilaiHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setJenis(String type){
        this.type = type;
    }
}