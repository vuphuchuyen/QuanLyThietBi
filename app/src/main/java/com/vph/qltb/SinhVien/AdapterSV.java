package com.vph.qltb.SinhVien;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.rpc.context.AttributeContext;
import com.vph.qltb.R;

import java.util.List;


public class AdapterSV extends ArrayAdapter<ModuleSV> {

    public AdapterSV(@NonNull Context context, int resource, @NonNull List<ModuleSV> objects) {
        super(context, resource, objects);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ModuleSV moduleSV = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_danhsach_sv, parent, false);
        }
        TextView STT = (TextView) convertView.findViewById(R.id.STTNM);

        TextView SVM = (TextView) convertView.findViewById(R.id.SinhVienM);
        TextView SDT = (TextView) convertView.findViewById(R.id.SDTM);
        TextView mssvM = (TextView) convertView.findViewById(R.id.MSSVM);
        TextView slM = (TextView) convertView.findViewById(R.id.SLM);
        TextView tenthietbiM = (TextView) convertView.findViewById(R.id.TenThietBiM);
        TextView ngayMuon = (TextView) convertView.findViewById(R.id.NgayMuon);
        TextView hanTra = (TextView) convertView.findViewById(R.id.HanTra);
        TextView lop = (TextView) convertView.findViewById(R.id.Lop) ;
        ImageButton Accept = convertView.findViewById(R.id.btnAccept);
        //CheckBox Check = (CheckBox) convertView.findViewById(R.id.CheckNM);

        //Tự tăng STT
        int stt = position+1;
        STT.setText(String.valueOf(stt));
        //Hiển thị danh sách
        SVM.setText(moduleSV.getSinhvien());
        SDT.setText(moduleSV.getSdt());
        lop.setText(moduleSV.getLop());
        mssvM.setText(moduleSV.getMssv());
        slM.setText(moduleSV.getSoluong());
        tenthietbiM.setText(moduleSV.getTenthietbi());
        ngayMuon.setText(moduleSV.getNgaymuon());
        hanTra.setText(moduleSV.getHantra());

        return convertView;
    }

}
