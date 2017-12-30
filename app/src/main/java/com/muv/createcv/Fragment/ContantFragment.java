package com.muv.createcv.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.muv.createcv.CommonValidation.Constant.MapAlreadyView;


public class ContantFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    DatabaseReference CantactDatabasereference,SocialDatabasereference;
    ValueEventListener CantactVauesistister,SocialValueslistener;
    @BindView(R.id.imagbtFb)
    ImageButton imagbtFb;
    @BindView(R.id.imgbtLinkedin)
    ImageButton imgbtLinkedin;
    @BindView(R.id.imgbttwitter)
    ImageButton imgbttwitter;
    @BindView(R.id.imgbtWhatsapp)
    ImageButton imgbtWhatsapp;
    @BindView(R.id.Propicture)
    SimpleDraweeView Propicture;
    @BindView(R.id.txtUserName)
    TextView txtUserName;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    Unbinder unbinder;
    String strFblink,strTwitterlink,strWhatsappNum,strLinkedin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

     GoogleMap mMap;
    View v;
    Marker Makerapply;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null){
                parent.removeView(v);
            }

        }
        // Inflate the layout for this fragment
        try {
            v = inflater.inflate(R.layout.fragment_contant, container, false);
            unbinder = ButterKnife.bind(this, v);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        if(mMap==null){
            ((MapFragment) getActivity().getFragmentManager()
                    .findFragmentById(R.id.map)).getMapAsync(this);
        }


        SocialLink();
        return v;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationDatabse();
    }

    @Override
    public void onMapLoaded() {

    }

    public void LocationDatabse() {
        CantactDatabasereference = FirebaseDatabase.getInstance().getReference().child("users").child("muthupandi").child("profile");
        CantactVauesistister = CantactDatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtAddress.setText(CommonValidation.ObjectValidation(dataSnapshot.child("location").getValue()));
                Constant.username = CommonValidation.ObjectValidation(dataSnapshot.child("username").getValue());
                txtUserName.setText(CommonValidation.StrCaption(Constant.username));
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setRoundAsCircle(true);
                Propicture.getHierarchy().setRoundingParams(roundingParams);
                Propicture.setImageURI(Uri.parse(CommonValidation.ObjectValidation(dataSnapshot.child("profilepic").getValue())));
              LatLng Latlng =  new LatLng(CommonValidation.LatLngValidation(dataSnapshot.child("lat").getValue()),CommonValidation.LatLngValidation(dataSnapshot.child("lang").getValue()));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(Latlng)
                        .zoom(17)
                        .tilt(0)
                        .build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                if(Makerapply!=null){
                    Makerapply.remove();
                }
                Makerapply=mMap.addMarker(new MarkerOptions().position(Latlng ).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ub__ic_pin_pickup)));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder!=null){
            unbinder.unbind();

        }
        MapAlreadyView =false;
        android.app.Fragment fragment = getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        if (null != fragment) {
            android.app.FragmentTransaction ft = getActivity()
                    .getFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }
    }

    @OnClick({R.id.imagbtFb, R.id.imgbtLinkedin, R.id.imgbttwitter, R.id.imgbtWhatsapp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imagbtFb:
                CallSocialLink(strFblink);
                break;
            case R.id.imgbtLinkedin:
                CallSocialLink(strLinkedin);
                break;
            case R.id.imgbttwitter:
                CallSocialLink(strTwitterlink);
                break;
            case R.id.imgbtWhatsapp:
                Toast.makeText(getContext(), "Still InProgress", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public void CallSocialLink(String url){
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
    public void SocialLink(){
        SocialDatabasereference = FirebaseDatabase.getInstance().getReference().child("users").child("muthupandi").child("social");
        SocialValueslistener = SocialDatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    strFblink = CommonValidation.ObjectValidation(dataSnapshot.child("facebook").getValue()) ;
                    strLinkedin = CommonValidation.ObjectValidation(dataSnapshot.child("linkedin").getValue()) ;
                    strTwitterlink = CommonValidation.ObjectValidation(dataSnapshot.child("twitter").getValue()) ;
                    strWhatsappNum = CommonValidation.ObjectValidation(dataSnapshot.child("whatsapp").getValue()) ;


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
