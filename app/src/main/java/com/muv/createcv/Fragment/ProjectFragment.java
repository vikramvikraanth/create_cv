package com.muv.createcv.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muv.createcv.Adapter.ProjectAdapter;
import com.muv.createcv.Adapter.SkillSetAdapter;
import com.muv.createcv.CommonValidation.CommonValidation;
import com.muv.createcv.Modelclass.ProjectModel;
import com.muv.createcv.R;
import com.muv.createcv.RecycleViewDivider.ItemOffsetDecoration;
import com.muv.createcv.RecycleviewOnclick.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.muv.createcv.CommonValidation.Constant.CheckNavi;


public class ProjectFragment extends Fragment implements RecyclerItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.txtEmpty)
    TextView txtEmpty;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private ArrayList<ProjectModel> movieList = new ArrayList<>();
    ProjectAdapter mAdapter;
    SkillSetAdapter skillSetAdapter;

    DatabaseReference ProjectDataReference;
    ValueEventListener ProjectValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    View v;
    String strFireBasePath = "projects";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_project, container, false);
        unbinder = ButterKnife.bind(this, v);
        if (CheckNavi) {
            strFireBasePath = "projects";
            mAdapter = new ProjectAdapter(movieList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new ItemOffsetDecoration(recyclerView.getContext(), R.dimen.item_decoration));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            if (mAdapter instanceof ProjectAdapter)
                ((ProjectAdapter) mAdapter).setRecyclerItemClickListener(this);
        } else {
            strFireBasePath = "skills";
            skillSetAdapter = new SkillSetAdapter(movieList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new ItemOffsetDecoration(recyclerView.getContext(), R.dimen.item_decoration));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(skillSetAdapter);
        }
        progressBar.setVisibility(View.VISIBLE);
        getFirebaseProjectList();
        return v;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClickListener(int position) {
        final ProjectModel picture = movieList.get(position);
        String url = picture.getUrl();

        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")){
                url = "http://" + url;
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getFirebaseProjectList() {
        movieList.clear();
        ProjectDataReference = FirebaseDatabase.getInstance().getReference().child("users").child("muthupandi").child(strFireBasePath);
        ProjectValues = ProjectDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        ProjectModel ProjectModel = new ProjectModel();
                        if (CheckNavi) {
                            ProjectModel.setTitle(CommonValidation.ObjectValidation(childDataSnapshot.child("title").getValue()));
                            ProjectModel.setUrl(CommonValidation.ObjectValidation(childDataSnapshot.child("url").getValue()));
                            ProjectModel.setCoverImage(CommonValidation.ObjectValidation(childDataSnapshot.child("coverimg").getValue()));
                            movieList.add(ProjectModel);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            ProjectModel.setTitle(CommonValidation.ObjectValidation(childDataSnapshot.getValue()));
                            movieList.add(ProjectModel);
                            skillSetAdapter.notifyDataSetChanged();
                        }
                        Showlist();
                    }
                } else {
                    Emptylist();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Emptylist();
            }
        });
    }

    public void Emptylist() {
        recyclerView.setVisibility(View.GONE);
        txtEmpty.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        if (CheckNavi) {
            txtEmpty.setText("Empty Project List");
        } else {
            txtEmpty.setText("Empty SkillSet List");
        }
    }

    public void Showlist() {
        recyclerView.setVisibility(View.VISIBLE);
        txtEmpty.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }
}
