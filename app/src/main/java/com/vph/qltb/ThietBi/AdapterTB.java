package com.vph.qltb.ThietBi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.squareup.picasso.Picasso;
import com.vph.qltb.R;

import java.util.List;

public class AdapterTB extends ArrayAdapter {


    public AdapterTB(@NonNull Context context, int resource, @NonNull List<ModuleTB> objects) {
        super(context, resource, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ModuleTB moduleTB = (ModuleTB) getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_thietbi, parent, false);
        }

        TextView STT = convertView.findViewById(R.id.stttb);
        TextView Ten =  convertView.findViewById(R.id.tentb);
        TextView SL =  convertView.findViewById(R.id.soluongtb);
        TextView TT =  convertView.findViewById(R.id.thongtintb);
        ImageView img = convertView.findViewById(R.id.HinhAnhtb);

        //Tự tăng STT
        int stt = position+1;
        STT.setText(String.valueOf(stt));
        //Hiển thị danh sách
        Ten.setText(moduleTB.getTen());
        SL.setText(moduleTB.getSoluong());
        TT.setText(moduleTB.getThongtin());
        Picasso.get().load(moduleTB.getHinhanh())
                .placeholder(R.drawable.ic_holder)
                .error(R.drawable.ic_error)
                .into(img);
        //Chức năng Zoom
        Button Zoom_in = convertView.findViewById(R.id.Big);
        Button Zoom_out = convertView.findViewById(R.id.Small);

        Zoom_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setScaleY(2);
                img.setScaleX(2);
            }
        });
        Zoom_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setScaleY(1);
                img.setScaleX(1);
            }
        });

        return convertView;
    }
}
