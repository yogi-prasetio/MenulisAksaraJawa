package com.android.menulisaksarajawa.ui.view.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.menulisaksarajawa.databinding.ItemRowCharacterBinding;
import com.android.menulisaksarajawa.ui.model.Characters;

import java.util.ArrayList;

public class ListAksaraAdapter extends RecyclerView.Adapter<ListAksaraAdapter.ListViewHolder> {
    private ArrayList<Characters> listAksara;
    private OnItemClickCallback onItemClickCallback;
    private int nilai;
    private String type;

    public ListAksaraAdapter(ArrayList<Characters> list) {
        this.listAksara = list;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRowCharacterBinding binding = ItemRowCharacterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ListViewHolder(binding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        Characters character = listAksara.get(position);
        holder.binding.ivAksara.setImageResource(character.getAksara());
        holder.binding.tvCharRomaji.setText(character.getRomaji());
        Log.e("DATA NILAI", String.valueOf(nilai) + " = "+ position);
        if(position>nilai){
            holder.binding.ivStatus.setColorFilter(Color.argb(100, 232, 66, 53));
        } else {
            holder.binding.ivStatus.setColorFilter(Color.argb(100, 8, 200, 16));
        }
        if(type.equals("test")){
            holder.binding.ivStatus.setVisibility(View.VISIBLE);
        } else {
            holder.binding.ivStatus.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listAksara.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return listAksara.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ItemRowCharacterBinding binding;

        ListViewHolder(ItemRowCharacterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Characters data);
    }

    public void setNilai(int nilai, String type){
        this.nilai = nilai-1;
        this.type = type;
    }
}