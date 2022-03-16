package com.vph.qltb.SinhVien.DanhSach;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.Menu.MenuLoginMSSV;
import com.vph.qltb.Menu.MenuLoginScan;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ChucNang.ChinhSuaHS;
import com.vph.qltb.SinhVien.ChucNang.PhieuDangKy;
import com.vph.qltb.SinhVien.ModuleSV;
import com.vph.qltb.ThietBi.ModuleTB;
import com.vph.qltb.ThietBi.ThietBi;

import java.util.ArrayList;
import java.util.List;

public class HoSoSV extends AppCompatActivity {

    TextView mssv, ten, lop, sdt, tbdm;
    ImageButton btnMail, btnNoti, btnBack, btnReload;
    Button btnUpdate, btnMuon;
    ListView lvHS;
    ArrayList<ModuleSV> dsMuonHS;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so_sv);
        Controls();
        HienThiHoSo();
        ThietBiDangMuon();
        xuLyButton();
        DemSoThietBiDaMuon();
    }
    public class AdapterHS extends ArrayAdapter<ModuleSV> {

        Context context;

        public AdapterHS(@NonNull Context context, int resource, @NonNull List<ModuleSV> objects) {
            super(context, resource, objects);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ModuleSV moduleSV = getItem(position);

            if(convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_danhsach, parent, false);
            }
            TextView STT = (TextView) convertView.findViewById(R.id.STTHS);

            TextView slM = (TextView) convertView.findViewById(R.id.SLHS);
            TextView tenthietbiM = (TextView) convertView.findViewById(R.id.TenThietBiHS);
            TextView ngayMuon = (TextView) convertView.findViewById(R.id.NgayMuonHS);
            TextView hanTra = (TextView) convertView.findViewById(R.id.HanTraHS);

            //Tự tăng STT
            int stt = position+1;
            STT.setText(String.valueOf(stt));
            //Hiển thị danh sách
            slM.setText(moduleSV.getSoluong());
            tenthietbiM.setText(moduleSV.getTenthietbi());
            ngayMuon.setText(moduleSV.getNgaymuon());
            hanTra.setText(moduleSV.getHantra());



            return convertView;
        }

    }


    private void DemSoThietBiDaMuon() {

    }

    private void xuLyButton() {
        //Muon
        btnMuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HoSoSV.this);
                builder.setTitle("Thông báo!").setIcon(R.drawable.question);
                builder.setMessage("Bạn muốn mượn thiết bị?");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent muon = new Intent(HoSoSV.this, PhieuDangKy.class);
                        startActivity(muon);
                    }
                });
                builder.setPositiveButton("NO", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //Quay lại Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HoSoSV.this, ChinhSuaHS.class);
                startActivity(intent);

            }
        });
    }

    private void HienThiHoSo() {
            reference = FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference();
            if (MenuLoginScan.scan == null) {
                mssv.setText(MenuLoginMSSV.login.getText().toString());
            } else if (MenuLoginMSSV.login == null) {
                mssv.setText(MenuLoginScan.scan.getText().toString());
            }
            String HsMSSV = mssv.getText().toString();
            reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(HsMSSV)) {
                        final String getSV = snapshot.child(HsMSSV).child("sinhvien").getValue(String.class);
                        final String getSDT = snapshot.child(HsMSSV).child("sdt").getValue(String.class);
                        final String getLop = snapshot.child(HsMSSV).child("lop").getValue(String.class);
                        ten.setText(getSV);
                        sdt.setText(getSDT);
                        lop.setText(getLop);
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void ThietBiDangMuon() {
        reference = FirebaseDatabase
                .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("DanhSachMuon");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsMuonHS.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModuleSV moduleSV = ds.getValue(ModuleSV.class);
                    if(moduleSV.getMssv().toLowerCase().contains(mssv.getText().toString().toLowerCase())) {
                        dsMuonHS.add(moduleSV);
                    }

                }
                AdapterHS adapter = new AdapterHS(HoSoSV.this,R.layout.design_danhsach,dsMuonHS);
                lvHS.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void Controls() {
        mssv = findViewById(R.id.txtHsMSSV);
        ten = findViewById(R.id.txtHsTenSV);
        lop = findViewById(R.id.txtHsLop);
        sdt = findViewById(R.id.txtHsSDT);
        tbdm = findViewById(R.id.txtHsTBMuon);

        btnMail = findViewById(R.id.btnHsMail);
        btnNoti = findViewById(R.id.btnHsNoti);
        btnBack = findViewById(R.id.btnHsBack);
        btnUpdate = findViewById(R.id.btnHsUpdate);
        btnMuon = findViewById(R.id.btnHsMuon);
        btnReload = findViewById(R.id.btnReloadHS);

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restart();
            }
        });

        lvHS = findViewById(R.id.lvHS);
        dsMuonHS = new ArrayList<>();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(HoSoSV.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(HoSoSV.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
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