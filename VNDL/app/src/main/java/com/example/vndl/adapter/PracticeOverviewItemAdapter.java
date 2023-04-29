package com.example.vndl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vndl.R;
import com.example.vndl.model.QuestionType;
import com.example.vndl.support.OverviewOnItemSelected;

import java.util.ArrayList;
import java.util.List;

public class PracticeOverviewItemAdapter extends ArrayAdapter {
    private Context context;
    private List<QuestionType> questionTypeList;
    OverviewOnItemSelected practiceOverviewItemListener;
    public PracticeOverviewItemAdapter(@NonNull Context context, @NonNull List<QuestionType> questionTypeList) {
        super(context, R.layout.practice_overview_item, questionTypeList);

        this.context = context;
        this.questionTypeList = questionTypeList;
    }

    public void setPracticeOverviewItemListener(OverviewOnItemSelected practiceOverviewItemListener) {
        this.practiceOverviewItemListener = practiceOverviewItemListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.practice_overview_item, null, true);
        ImageView imageView = v.findViewById(R.id.imageView2);
        TextView title = v.findViewById(R.id.titleText);
        TextView desc = v.findViewById(R.id.descText);

        QuestionType questionType = questionTypeList.get(position);

        String imageName = "ic_" + questionType.getImageName();
        int resID = context.getResources().getIdentifier(imageName , "drawable" , context.getPackageName());

        imageView.setImageResource(resID);
        title.setText(questionType.getTitle());
        desc.setText(questionType.getDesc());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                practiceOverviewItemListener.overviewOnItemClick(v, position);
            }
        });

        return v;
    }
}
