package com.example.ahnjeonghyeon.hcicafein;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hojingong on 2017. 6. 9..
 */

public class Fragment_drink extends Fragment{
    ImageView imageView;
    String cimage;
    String mimage;
    CafeMenuDB dbhelper;
    SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_layout,container,false);
        imageView =(ImageView)root.findViewById(R.id.menuimg);

        cimage = getArguments().getString("cafeimg");
        Toast.makeText(root.getContext(), cimage, Toast.LENGTH_SHORT).show();
        //디비에서 이 이름인 카페 찾아서 해당하는 메뉴 스트링가져옴  //이건 베이커리니까 2

        //
        final CafeMenuHandler dbhandler = new CafeMenuHandler(root.getContext());
        dbhelper = new CafeMenuDB(root.getContext());

        String check = dbhandler.check(cimage,"1");
        if(check!=null){
            Toast.makeText(root.getContext(),check,Toast.LENGTH_SHORT).show();
            mimage = check;
        }

        int lid = this.getResources().getIdentifier(mimage,"drawable",root.getContext().getPackageName());

        Toast.makeText(root.getContext(),getString(lid),Toast.LENGTH_SHORT).show();

        imageView.setImageResource(lid);


      //  imageView.setImageResource(R.drawable.menusample);
        return root;
    }
}
