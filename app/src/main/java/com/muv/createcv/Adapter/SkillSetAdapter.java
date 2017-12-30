package com.muv.createcv.Adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muv.createcv.Modelclass.ProjectModel;
import com.muv.createcv.R;
import com.muv.createcv.RecycleviewOnclick.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vikram on 25-Dec-17.
 */

public class SkillSetAdapter extends RecyclerView.Adapter<SkillSetAdapter.ProjectHolder> {

     ArrayList<ProjectModel> pictureArrayList;


    @Override
    public SkillSetAdapter.ProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.skillsetadapter, parent, false);
        return new SkillSetAdapter.ProjectHolder(itemView);
    }
    public SkillSetAdapter(ArrayList<ProjectModel> pictureArrayList) {
        this.pictureArrayList = pictureArrayList;
    }
    @Override
    public void onBindViewHolder(SkillSetAdapter.ProjectHolder holder, int position) {
        final ProjectModel picture = pictureArrayList.get(position);
        holder.txtTitle.setText(picture.getTitle());

    }

    @Override
    public int getItemCount() {
        return pictureArrayList.size();
    }

    public class ProjectHolder extends RecyclerView.ViewHolder  {

        @BindView(R.id.txt_title)
        TextView txtTitle;

        public ProjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
