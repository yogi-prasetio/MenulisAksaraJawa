package com.android.menulisaksarajawa.ui.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.menulisaksarajawa.databinding.ItemRowNilaiBinding;
import com.android.menulisaksarajawa.ui.model.Nilai;

import java.util.ArrayList;

public class ListNilaiAdapter extends RecyclerView.Adapter<ListNilaiAdapter.ListViewHolder> {
    private ArrayList<Nilai> listNilai;
    private ListNilaiAdapter.OnItemClickCallback onItemClickCallback;
    
    public ListNilaiAdapter(ArrayList<Nilai> list) {
        this.listNilai = list;
    }

    public void setOnItemClickCallback(ListNilaiAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListNilaiAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRowNilaiBinding binding = ItemRowNilaiBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ListNilaiAdapter.ListViewHolder(binding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ListNilaiAdapter.ListViewHolder holder, int position) {
        Nilai nilai = listNilai.get(position);
        holder.binding.tvName.setText(nilai.getName());
        holder.binding.tvNilaiAngka.setText(nilai.getAngka());
        holder.binding.tvNilaiCarakan.setText(nilai.getCarakan());
        holder.binding.tvNilaiPasangan.setText(nilai.getPasangan());
        holder.binding.tvNilaiSwara.setText(nilai.getSwara());
        holder.binding.tvTotalNilai.setText(nilai.getTotal());
        
        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listNilai.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return listNilai.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ItemRowNilaiBinding binding;

        ListViewHolder(ItemRowNilaiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Nilai data);
    }
}