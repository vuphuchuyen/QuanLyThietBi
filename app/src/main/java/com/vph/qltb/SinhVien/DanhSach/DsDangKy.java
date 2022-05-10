package com.vph.qltb.SinhVien.DanhSach;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ModuleSV;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DsDangKy extends AppCompatActivity {
    Button btnRestartDK, btnBack;
    ListView lvDanKy;
    ArrayList<ModuleSV> dsDangKy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsach_sv_dangky);
        addControls();
        addEvents();
        hienthiDanhSach();
    }
    public class AdapterDK extends ArrayAdapter<ModuleSV> {

        Context context;

        public AdapterDK(@NonNull Context context, int resource, @NonNull List<ModuleSV> objects) {
            super(context, resource, objects);
            this.context = context;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ModuleSV moduleSV = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_ds_dangky, parent, false);
            }
            TextView Student_Name = convertView.findViewById(R.id.txtStudent_Name);
            TextView Student_ID = convertView.findViewById(R.id.txtStudent_ID);
            TextView Student_Class = convertView.findViewById(R.id.txtStudent_Class);
            TextView Total_Device = convertView.findViewById(R.id.txtTotal_Device);
            TextView textNoti = convertView.findViewById(R.id.textNoti);
            Button btnChiTiet = convertView.findViewById(R.id.btnChiTietDangKy);

            //Hiển thị danh sách
            Student_ID.setText(moduleSV.getMssv());
            //
             String ID = Student_ID.getText().toString();
            FireBaseHelper.reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(ID)) {
                        final String getSV = snapshot.child(ID).child("sinhvien").getValue(String.class);
                        final String getLop = snapshot.child(ID).child("lop").getValue(String.class);
                        Student_Name.setText(getSV);
                        Student_Class.setText(getLop);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FireBaseHelper.reference.child("DanhSachDangKy")
                    .child(ID)
                    .child("Thiết bị")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Đếm tổng số lượng
                    int sumSL = 0;
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object sl = map.get("soluong");
                        int pValue = Integer.parseInt(String.valueOf(sl));
                        sumSL += pValue;
                        Total_Device.setText(String.valueOf(sumSL));
                    }
                    //Tình trạng
                    int sumStatus = 0;
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object st = map.get("tinhtrang");
                        int pValue = Integer.parseInt(String.valueOf(st));
                        sumStatus += pValue;
                        if(sumStatus > 0){
                            textNoti.setVisibility(View.VISIBLE);
                        }else{
                            textNoti.setVisibility(View.INVISIBLE);
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            //Hiển thị chi tiết mượn
            btnChiTiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ChiTiet", moduleSV.getMssv());
                                    Intent intent = new Intent(DsDangKy.this, ChiTietDangKy.class);
                                    intent.putExtra("ThongTin", bundle);
                                    startActivity(intent);
                }
            });
            return convertView;
        }

    }


    private void addEvents() {

        btnRestartDK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Restart();
            }
        });
        //Quay lại Menu
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });

    }


    private void hienthiDanhSach() {
        FireBaseHelper.reference.child("DanhSachDangKy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsDangKy.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModuleSV moduleSV = ds.getValue(ModuleSV.class);
                    dsDangKy.add(moduleSV);
                }
                AdapterDK adapter = new AdapterDK(DsDangKy.this, R.layout.design_ds_dangky, dsDangKy);
                lvDanKy.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void addControls() {
        btnBack = findViewById(R.id.btnQuaylaiMenu);
        btnRestartDK = findViewById(R.id.btnRestartDK);

        lvDanKy = findViewById(R.id.lvNguoiMuon);
        dsDangKy = new ArrayList<>();
    }


    public void Restart(){
        recreate();
        Toast.makeText(this,"Trang đã được tải lại",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isConnected()) {

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(DsDangKy.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isConnected()) {

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(DsDangKy.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isConnected()) {

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(DsDangKy.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    //Kiểm tra kết nối internet
    boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
            if(networkInfo.isConnected())
                return true;
            else
                return false;
        }else
            return false;
    }
}