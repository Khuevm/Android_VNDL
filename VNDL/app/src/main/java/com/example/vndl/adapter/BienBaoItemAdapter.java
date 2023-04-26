package com.example.vndl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vndl.R;
import com.example.vndl.support.BienBaoHeaderOnItemSelected;
import com.example.vndl.model.Sign;
import com.example.vndl.support.BienBaoOnItemSelected;

import java.util.ArrayList;
import java.util.List;

public class BienBaoItemAdapter extends RecyclerView.Adapter<BienBaoItemAdapter.ViewHolder> {
    Context context;
    List<Sign> signList = new ArrayList<>();
    BienBaoOnItemSelected bienBaoItemListener;

    public BienBaoItemAdapter(Context context, List<Sign> signList) {
        this.context = context;
        this.signList = signList;
    }

    public void setBienBaoItemListener(BienBaoOnItemSelected bienBaoItemListener) {
        this.bienBaoItemListener = bienBaoItemListener;
    }

    @NonNull
    @Override
    public BienBaoItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.bien_bao_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BienBaoItemAdapter.ViewHolder holder, int position) {
        Sign sign = signList.get(position);

        String[] imageName = sign.getImageName().split("[.]");
        String imageNameFormat = imageName[0].toLowerCase();

        int resID = context.getResources().getIdentifier(imageNameFormat , "drawable" , context.getPackageName());

        holder.imageView.setImageResource(resID);
        holder.title.setText(sign.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bienBaoItemListener.bienBaoOnItemClick(v,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return signList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        View itemView;

        public ViewHolder(@NonNull View v) {
            super(v);

            imageView = v.findViewById(R.id.bienBaoImageView);
            title = v.findViewById(R.id.bienBaoTitle);
            itemView = v.findViewById(R.id.bienBaoItemView);
        }
    }
}
