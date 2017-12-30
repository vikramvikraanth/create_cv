package com.muv.createcv.Adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectHolder> {


    private RecyclerItemClickListener recyclerItemClickListener;
    private ArrayList<ProjectModel> pictureArrayList;
    public void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }
    public ProjectAdapter(ArrayList<ProjectModel> pictureArrayList) {
        this.pictureArrayList = pictureArrayList;
    }
    @Override
    public ProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projectadapter, parent, false);
        return new ProjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProjectHolder holder, int position) {
        final ProjectModel picture = pictureArrayList.get(position);
        holder.txtTitle.setText(picture.getTitle());
        holder.imageView.setImageURI(Uri.parse(picture.getCoverImage()));
    }

    @Override
    public int getItemCount() {
        return pictureArrayList.size();
    }

    public class ProjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        SimpleDraweeView imageView;
        @BindView(R.id.txt_title)
        TextView txtTitle;

        public ProjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (recyclerItemClickListener != null)
                recyclerItemClickListener.onItemClickListener(getAdapterPosition());
        }
    }
}
