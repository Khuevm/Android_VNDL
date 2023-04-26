package com.example.vndl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vndl.adapter.BienBaoHeaderItemAdapter;
import com.example.vndl.adapter.BienBaoItemAdapter;
import com.example.vndl.database.DBHandler;
import com.example.vndl.support.BienBaoHeaderOnItemSelected;
import com.example.vndl.model.Sign;
import com.example.vndl.model.SignGroup;
import com.example.vndl.support.BienBaoOnItemSelected;
import com.example.vndl.support.Constant;

import java.util.ArrayList;
import java.util.List;

public class BienbaoActivity extends AppCompatActivity implements BienBaoHeaderOnItemSelected, BienBaoOnItemSelected {
    ImageButton backButton;
    RecyclerView headerView, bienBaoRecycleView;
    List<SignGroup> signGroupList = new ArrayList<>();
    List<Sign> signList = new ArrayList<>();
    BienBaoHeaderItemAdapter headerAdapter;
    LinearLayoutManager headerManager;
    BienBaoItemAdapter itemAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bien_bao);

        headerView = findViewById(R.id.questionHeaderView);
        bienBaoRecycleView = findViewById(R.id.bienBaoRecycleView);
        backButton = findViewById(R.id.backButton);

        //Database
        DBHandler db = new DBHandler(this);
        signGroupList = db.getListSignGroup();
        signGroupList.get(0).setSelected(true);

        //Header SignGroup
        headerAdapter = new BienBaoHeaderItemAdapter(this, signGroupList);
        headerManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        headerAdapter.setBienBaoItemListener(this);
        headerView.setAdapter(headerAdapter);
        headerView.setLayoutManager(headerManager);

        //Sign RecycleView
        itemAdapter = new BienBaoItemAdapter(this,signList);
        GridLayoutManager itemManager = new GridLayoutManager(this, 2);

        itemAdapter.setBienBaoItemListener(this);
        bienBaoRecycleView.setAdapter(itemAdapter);
        bienBaoRecycleView.setLayoutManager(itemManager);
        getSignList(0);

        //BackButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void bienBaoHeaderOnItemClick(View v, int position) {
        headerManager.scrollToPositionWithOffset(position, (Constant.SCREEN_WIDTH - v.getWidth())/2);
        getSignList(position);
    }

    @Override
    public void bienBaoOnItemClick(View v, int position) {
        Sign sign = signList.get(position);

        Intent intent = new Intent(BienbaoActivity.this, BienbaoDetailActivity.class);
        intent.putExtra("Sign", sign);
        startActivity(intent);
    }

    private void getSignList(int position) {
        signList.clear();
        signList.addAll(signGroupList.get(position).getSigns());

        itemAdapter.notifyDataSetChanged();
    }
}
