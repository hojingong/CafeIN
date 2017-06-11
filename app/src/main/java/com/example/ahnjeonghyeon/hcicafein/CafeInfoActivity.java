package com.example.ahnjeonghyeon.hcicafein;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hojingong on 2017. 6. 9..
 */

public class CafeInfoActivity extends Activity{
    Button frag1;
    Button frag2;
    Bundle bundle;
    TextView tname;
    TextView tad;
    TextView tcall;
    ImageView logoimg;
    ImageButton like;
    LikeDB dbhelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cafe_info);

        //intent 받아오기
        Intent intent = getIntent();
        final String cname = intent.getStringExtra("cafem");
        final String cad = intent.getStringExtra("cafead");
        String ccall = intent.getStringExtra("cafecall");
        String cimg = intent.getStringExtra("cafeimg");

        logoimg = (ImageView)findViewById(R.id.cafeimg);
        tname = (TextView)findViewById(R.id.cafename);
        tad = (TextView)findViewById(R.id.cafead);
        tcall = (TextView)findViewById(R.id.cafecall);
        tname.setText(cname);
        tad.setText(cad);
        tcall.setText(ccall);

        //카페 이미지 설정 근데 이거 이미지도 다른 그림으로 해야될듯!
        int lid = this.getResources().getIdentifier(cimg,"drawable",this.getPackageName());
        logoimg.setImageResource(lid);


        //
        like = (ImageButton) findViewById(R.id.likebtn);

        //관심카페
        final LikeHandler dbhandler = new LikeHandler(getApplicationContext());
        long c = dbhandler.check(cname);
        if(c==1){
            like.setImageResource(R.drawable.full);//이미 관심등록
        }else{
           like.setImageResource(R.drawable.empty);
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbhelper = new LikeDB(getApplicationContext());

                long check = dbhandler.check(cname);
                if(check==1){
                    Toast.makeText(getApplicationContext(),"관심카페 해제",Toast.LENGTH_SHORT).show();
                    dbhelper.delete(cname,cad);
                    like.setImageResource(R.drawable.empty);
                }else{
                    long check2 = dbhandler.insert(cname,cad);
                    if(check2==0){
                        Toast.makeText(getApplicationContext(),"오류",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"관심카페로 등록",Toast.LENGTH_SHORT).show();
                        like.setImageResource(R.drawable.full);
                    }
                }
            }
        });
        //이미지만 프래그먼트로 넘겨준다
        bundle =new Bundle();
        bundle.putString("cafeimg",cimg);
        //
        frag1 = (Button)findViewById(R.id.frag1btn);
        frag2 = (Button)findViewById(R.id.frag2btn);

        frag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag1.setBackgroundColor(Color.parseColor("#6799FF"));
                frag2.setBackgroundColor(Color.GRAY);
                Fragment myfragment;
                myfragment = new Fragment_drink();

                //메뉴알고싶은 카페 정보전달
                myfragment.setArguments(bundle);

                android.app.FragmentManager fm = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,myfragment);
                fragmentTransaction.commit();
            }
        });

        frag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag1.setBackgroundColor(Color.GRAY);
                frag2.setBackgroundColor(Color.parseColor("#6799FF"));
                Fragment myfragment;
                myfragment = new Fragment_bakery();

                //메뉴알고싶은 카페 정보전달
                myfragment.setArguments(bundle);

                android.app.FragmentManager fm = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,myfragment);
                fragmentTransaction.commit();
            }
        });
    }

}
