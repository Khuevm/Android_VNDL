package com.example.vndl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vndl.R;
import com.example.vndl.support.BienBaoHeaderOnItemSelected;
import com.example.vndl.model.SignGroup;

import java.util.List;

public class BienBaoHeaderItemAdapter extends RecyclerView.Adapter<BienBaoHeaderItemAdapter.ViewHolder>{
    Context context;
    List<SignGroup> signGroupList;
    BienBaoHeaderOnItemSelected bienBaoHeaderItemListener;
    int selected = 0;

    public BienBaoHeaderItemAdapter(Context context, List<SignGroup> signGroupList) {
        this.context = context;
        this.signGroupList = signGroupList;
    }

    public void setBienBaoItemListener(BienBaoHeaderOnItemSelected bienBaoHeaderItemListener) {
        this.bienBaoHeaderItemListener = bienBaoHeaderItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.bien_bao_header_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SignGroup signGroup = signGroupList.get(position);

        holder.headerButton.setText(signGroup.getGroupName());
        if (signGroup.isSelected()) {
            holder.headerButton.setBackgroundColor(context.getResources().getColor(R.color.primary));
            holder.headerButton.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.headerButton.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.headerButton.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.headerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignGroup oldSelectedGroup = signGroupList.get(selected);
                oldSelectedGroup.setSelected(false);

                signGroup.setSelected(true);
                selected = holder.getAdapterPosition();

                bienBaoHeaderItemListener.bienBaoHeaderOnItemClick(v, selected);
                notifyDataSetChanged();
            }
        });
    }



    @Override
    public int getItemCount() {
        return signGroupList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button headerButton;
        public ViewHolder(@NonNull View v) {
            super(v);

            headerButton = v.findViewById(R.id.headerButton);
        }
    }

}
