package com.example.ahnjeonghyeon.hcicafein;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    GoogleMap map;
    final int REQ_PERMISSION=1000;
    AutoCompleteTextView search;
    ArrayList<String> autocomplete_List;
    ArrayAdapter adapter;

    DBHandler db;
    ArrayList<Cafe> cafeList;
    Geocoder gc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initmap();
        init();
    }

    public void init(){
        search=(AutoCompleteTextView)findViewById(R.id.search_Text);
        autocomplete_List=new ArrayList<>();
        gc=new Geocoder(this);
        cafeList=new ArrayList<>();
        db=new DBHandler(this,null,null,1);
        //변수들 초기화


        Cursor cursor=db.getCafeList();
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            autocomplete_List.add(cursor.getString(1));
            cursor.moveToNext();
        }
        adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,autocomplete_List);
        search.setAdapter(adapter);
        //autocompleteText 에 어뎁터 붙임

    }

    public void initmap(){
        mapFragment=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }//맵초기화

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            map.setMyLocationEnabled(true);
        }
        else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION
            );
        }

        //맵 ready
        callMarker();
        //지정된 카페들 마크찍음, 처음은 전부다찍기
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                map.setMyLocationEnabled(true);
            } else {
// Permission was denied. Display an error message.
            }
        }

    }//permission check
    public void callMarker(){//카페들 마크찍기 여기다가 필터링 추가하면 됨

        LatLng konkuk=new LatLng(37.5407625,127.07934279999995);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(konkuk,16));

        //TODO 지금은 카페 전부 검색하는데 DBHandler에 필터링하는 함수 추가해서 사용하면 됨
        //getCafeList에 필터링할 where절 추가해서 query문 보내면 필터링할 수 있음 필터링 코드는 DBHandler에서 하면 됨
        Cursor cursor=db.getCafeList();
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            Cafe cafe=new Cafe();
            String name=cursor.getString(1);
            String address=cursor.getString(2);
            String phone=cursor.getString(3);
            cafe.setName(name);
            cafe.setAddress(address);
            cafe.setPhone(phone);

            cafeList.add(cafe);
            cursor.moveToNext();
        }
        Toast.makeText(this,String.valueOf(cafeList.size())+cafeList.get(0).getAddress(),Toast.LENGTH_SHORT).show();

        //CafeList에 다 넣음



        //TODO 여기서 이미지 변경, cafe에 필터링 여부들 전부 추가해야한다 그리고 Cafe에서 읽어와서 사용하면 됨
        for(int i=0;i< cafeList.size();i++){
            try {
                List<Address> addr=gc.getFromLocationName(cafeList.get(i).getAddress(),5);

                LatLng latLng=new LatLng(addr.get(0).getLatitude(),addr.get(0).getLongitude());

                MarkerOptions options=new MarkerOptions();
                options.position(latLng);
                options.title(cafeList.get(i).getName());
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                //여기서 이미지로 변경
                Marker mk=map.addMarker(options);
                mk.showInfoWindow();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void searchCafe(View view) {//검색해서 지도 이동하기
        String moveToCafe=search.getText().toString();
        Cursor cursor=db.getCafeList();
        Cafe cafe=new Cafe();

        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            String name=cursor.getString(1);
            if(moveToCafe.equals(name)){

                cafe.setName(cursor.getString(1));
                cafe.setAddress(cursor.getString(2));
                cafe.setPhone(cursor.getString(3));
                break;
            }
            else {
                cursor.moveToNext();
            }
        }
        try {
            List<Address> addr=gc.getFromLocationName(cafe.getAddress(),5);
            LatLng latLng=new LatLng(addr.get(0).getLatitude(),addr.get(0).getLongitude());

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
