package com.android.menulisaksarajawa.ui.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ItemRowCharacterBinding;
import com.android.menulisaksarajawa.ui.model.Characters;

import java.util.ArrayList;

public class ListAksaraAdapter extends RecyclerView.Adapter<ListAksaraAdapter.ListViewHolder> {
    private ArrayList<Characters> listAksara;
    private OnItemClickCallback onItemClickCallback;

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

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        Characters character = listAksara.get(position);
        holder.binding.ivCharJapan.setImageResource(character.getAksara());
        holder.binding.tvCharRomaji.setText(character.getRomaji());

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
}