package com.muv.createcv.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muv.createcv.CommonValidation.CommonValidation;
import com.muv.createcv.CommonValidation.Constant;
import com.muv.createcv.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AboutMeFragment extends Fragment {


    @BindView(R.id.Propicture)
    SimpleDraweeView Propicture;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.txtDob)
    TextView txtDob;
    @BindView(R.id.txtQualification)
    TextView txtQualification;
    @BindView(R.id.txtDestination)
    TextView txtDestination;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.card_view)
    CardView cardView;
    Unbinder unbinder;
    DatabaseReference UserDatabaseReference;
    ValueEventListener UserValues;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_about_me, container, false);
        unbinder = ButterKnife.bind(this, v);
        progressBar.setVisibility(View.VISIBLE);
        SetUserDetails();

        return v;
    }

    public void SetUserDetails() {
        UserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("muthupandi").child("profile");
        UserValues = UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Constant.username = CommonValidation.ObjectValidation(dataSnapshot.child("username").getValue());
                System.out.println("enter the url name" + Constant.username);
                System.out.println("enter the url of name" + CommonValidation.StrCaption(Constant.username));
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setRoundAsCircle(true);
                Propicture.getHierarchy().setRoundingParams(roundingParams);
                Propicture.setImageURI(Uri.parse(CommonValidation.ObjectValidation(dataSnapshot.child("profilepic").getValue())));
                txtDestination.setText(CommonValidation.ObjectValidation(dataSnapshot.child("Professional").getValue()));
                txtName.setText(CommonValidation.StrCaption(Constant.username));
                txtAddress.setText(CommonValidation.ObjectValidation(dataSnapshot.child("location").getValue()));
                txtDob.setText(CommonValidation.ObjectValidation(dataSnapshot.child("dob").getValue()));
                txtEmail.setText(CommonValidation.ObjectValidation(dataSnapshot.child("email").getValue()));
                txtQualification.setText(CommonValidation.ObjectValidation(dataSnapshot.child("qualification").getValue()));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
