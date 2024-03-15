package com.android.menulisaksarajawa.ui.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.menulisaksarajawa.databinding.ItemRowWordBinding;
import com.android.menulisaksarajawa.ui.model.Characters;
import com.android.menulisaksarajawa.ui.utils.PrefManager;

import java.util.ArrayList;

public class ListKataAdapter extends RecyclerView.Adapter<ListKataAdapter.ListViewHolder> {
    private ArrayList<Characters> listKata;
    private OnItemClickCallback onItemClickCallback;
    private String type, role;
    private PrefManager prefManager;

    public ListKataAdapter(ArrayList<Characters> list) {
        this.listKata = list;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRowWordBinding binding = ItemRowWordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        prefManager = new PrefManager(parent.getContext());
        role = prefManager.getSPRole();

        return new ListViewHolder(binding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        Characters character = listKata.get(position);

        holder.binding.tvCharRomaji.setText(character.getRomaji());
        holder.binding.ivStatus.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listKata.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return listKata.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ItemRowWordBinding binding;

        ListViewHolder(ItemRowWordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Characters data);
    }
}