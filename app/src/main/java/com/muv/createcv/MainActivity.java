package com.muv.createcv;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.muv.createcv.Fragment.AboutMeFragment;
import com.muv.createcv.Fragment.ContantFragment;
import com.muv.createcv.Fragment.ProjectFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

import static com.muv.createcv.CommonValidation.Constant.CheckNavi;
import static com.muv.createcv.CommonValidation.Constant.Fragmentopen;
import static com.muv.createcv.CommonValidation.Constant.MapAlreadyView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.banner_slider1)
    BannerSlider bannerSlider1;
    List<Banner> banners = new ArrayList<>();
    DatabaseReference BannerImagedatbasereference, UserDatabaseReference;
    ValueEventListener BannerImageValues, UserValues;
    DrawerLayout drawer;
    @BindView(R.id.txtHeader)
    TextView txtHeader;
    @BindView(R.id.txtprofession)
    TextView txtprofession;
    @BindView(R.id.txtdescription)
    TextView txtdescription;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.txtQualification)
    TextView txtQualification;
    TextView txtUserName, txtDesignnation;
    SimpleDraweeView Propicture;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SetUserDetails();
        progressBar.setVisibility(View.VISIBLE);
        SetBannerImage();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        Propicture = (SimpleDraweeView) header.findViewById(R.id.Propicture);
        txtUserName = (TextView) header.findViewById(R.id.txtUserName);
        txtDesignnation = (TextView) header.findViewById(R.id.txtDesignnation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void SetBannerImage() {
        BannerImagedatbasereference = FirebaseDatabase.getInstance().getReference().child("users").child("muthupandi").child("banners");
        BannerImageValues = BannerImagedatbasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String strBannerImage = childDataSnapshot.getValue().toString();
                        System.out.println("enter the image of vallue event image" + strBannerImage);
                        banners.add(new RemoteBanner(strBannerImage));
                        progressBar.setVisibility(View.GONE);
                    }
                    if (banners != null) {
                        for (int i = 0; i < banners.size(); i++) {
                            banners.get(i).setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        bannerSlider1.setBanners(banners);
                    }
                    if (BannerImagedatbasereference != null) {
                        BannerImagedatbasereference.removeEventListener(BannerImageValues);
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("enter the firebase dabase refer exception" + databaseError.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
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
                txtDesignnation.setText(CommonValidation.ObjectValidation(dataSnapshot.child("Professional").getValue()));
                txtHeader.setText(CommonValidation.StrCaption(Constant.username));
                txtUserName.setText(CommonValidation.StrCaption(Constant.username));
                txtprofession.setText(CommonValidation.ObjectValidation(dataSnapshot.child("Professional").getValue()));
                txtdescription.setText(CommonValidation.ObjectValidation(dataSnapshot.child("description").getValue()));
                txtEmail.setText(CommonValidation.ObjectValidation(dataSnapshot.child("email").getValue()));
                txtQualification.setText(CommonValidation.ObjectValidation(dataSnapshot.child("qualification").getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        if (bannerSlider1 != null) {
            bannerSlider1.removeAllBanners();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_Skills:
                Fragmentopen =false;
                CheckNavi = false;
                MapAlreadyView =false;
                ProjectFragment skillset = new ProjectFragment();
                if(skillset!=null && !skillset.isAdded()){
                    moveToFragment(skillset);
                }

                break;
            case R.id.nav_Project:
                CheckNavi = true;
                Fragmentopen =false;
                MapAlreadyView =false;
                ProjectFragment ProjectFragment = new ProjectFragment();
                if(ProjectFragment!=null && !ProjectFragment.isAdded()){
                    moveToFragment(ProjectFragment);
                }

                break;
            case R.id.nav_about:
                MapAlreadyView =false;
                Fragmentopen =false;
                AboutMeFragment AboutMeFragment = new AboutMeFragment();
                if(AboutMeFragment!=null && !AboutMeFragment.isAdded()){
                    moveToFragment(AboutMeFragment);
                }

                break;
            case R.id.nav_Contact:
                Fragmentopen =false;
                if(!MapAlreadyView){
                    MapAlreadyView =true;
                    ContantFragment ContantFragment = new ContantFragment();
                    if(ContantFragment!=null && !ContantFragment.isAdded()){
                        moveToFragment(ContantFragment);
                    }
                }

                break;
            default:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void moveToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Mainlayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(Fragmentopen){
                super.onBackPressed();
            }else {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Fragmentopen =true;
                MapAlreadyView =false;
            }

        }
    }

    @OnClick(R.id.imgbtNav)
    public void onViewClicked() {
        System.out.println("enter the on click");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }

    }
}
