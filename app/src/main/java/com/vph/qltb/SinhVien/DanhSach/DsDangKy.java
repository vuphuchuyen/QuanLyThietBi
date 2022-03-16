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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vph.qltb.Menu.MenuLoginMSSV;
import com.vph.qltb.Menu.MenuLoginScan;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ModuleSV;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DsDangKy extends AppCompatActivity {
    TextView DSDK;
    ImageButton btnRestartDK, btnBack;
    ListView lvDanKy;
    ArrayList<ModuleSV> dsDangKy;


    public static DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsach_sv_dangky);
        addControls();
        addEvents();
        hienthiDanhSach();
        Hide();
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
            TextView STT = (TextView) convertView.findViewById(R.id.STTNM);

            TextView SVM = (TextView) convertView.findViewById(R.id.SinhVienM);
            TextView SDT = (TextView) convertView.findViewById(R.id.SDTM);
            TextView mssvM = (TextView) convertView.findViewById(R.id.MSSVM);
            TextView slM = (TextView) convertView.findViewById(R.id.SLM);
            TextView tenthietbiM = (TextView) convertView.findViewById(R.id.TenThietBiM);
            TextView ngayMuon = (TextView) convertView.findViewById(R.id.NgayMuon);
            TextView hanTra = (TextView) convertView.findViewById(R.id.HanTra);
            TextView lop = (TextView) convertView.findViewById(R.id.Lop);
            ImageButton Accept = convertView.findViewById(R.id.btnAccept);
            //CheckBox Check = (CheckBox) convertView.findViewById(R.id.CheckNM);

            //Tự tăng STT
            int stt = position + 1;
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
            String key = moduleSV.getId();
            //
            ImageButton btnAccept = convertView.findViewById(R.id.btnAccept);

            DatabaseReference reference = FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("DanhSachDangKy");
            DatabaseReference refMuon = FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("DanhSachMuon");

            reference.child(String.valueOf(moduleSV.getId()))
                    .orderByChild(String.valueOf(stt))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            btnAccept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DsDangKy.this);
                                    builder.setTitle("Thông báo!").setIcon(R.drawable.question);
                                    builder.setMessage("Đồng ý cho sinh viên "
                                            + moduleSV.getSinhvien() + " mượn thiết bị "
                                            + moduleSV.getTenthietbi() + " ?"
                                            + "\nNgày mượn: " + moduleSV.getNgaymuon()
                                            + "\nHạn trả: " + moduleSV.getHantra());
                                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            final String getSV = moduleSV.getSinhvien();
                                            final String getSDT = moduleSV.getSdt();
                                            final String getLop = moduleSV.getLop();
                                            final String getMssvM = moduleSV.getMssv();
                                            final String getSL = moduleSV.getSoluong();
                                            final String getTenTB = moduleSV.getTenthietbi();
                                            final String getNgayMuon = moduleSV.getNgaymuon();
                                            final String getHanTra = moduleSV.getHantra();
                                            final String getTinhTrang = "Đang mượn";
                                            ModuleSV moduleSV = new ModuleSV(getSV, getLop, getSDT, getMssvM, getSL, getTenTB, getNgayMuon, getHanTra, getTinhTrang, key);
                                            refMuon.child(key).setValue(moduleSV);
                                            for (DataSnapshot dsdk : snapshot.getChildren()) {
                                                dsdk.getRef().removeValue();
                                            }
                                            Toast.makeText(DsDangKy.this, "Đã đồng ý cho sinh viên này mượn thiết bị", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            for (DataSnapshot dsdk : snapshot.getChildren()) {
                                                dsdk.getRef().removeValue();
                                                Toast.makeText(DsDangKy.this, "Đã từ chối sinh viên này mượn thiết bị!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            //Kiểm tra role
            DatabaseReference refSV = FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Account");
            if (MenuLoginScan.scan == null) {
                String check = MenuLoginMSSV.login.getText().toString();
                refSV.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Role = snapshot.child(check).child("role").getValue(String.class);
                        if (Role.equals("admin")) {
                            btnAccept.setVisibility(View.VISIBLE);
                        } else {
                            btnAccept.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else if (MenuLoginMSSV.login == null){
                String check = MenuLoginScan.scan.getText().toString();
                refSV.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Role = snapshot.child(check).child("role").getValue(String.class);
                        if (Role.equals("admin")) {
                            btnAccept.setVisibility(View.VISIBLE);
                        } else {
                            btnAccept.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            return convertView;
        }

    }
    private void Hide() {
        //Kiểm tra role
        DatabaseReference refSV = FirebaseDatabase
                .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Account");
        if (MenuLoginScan.scan == null) {
            String check = MenuLoginMSSV.login.getText().toString();
            refSV.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String Role = snapshot.child(check).child("role").getValue(String.class);
                    if (Role.equals("admin")) {
                        DSDK.setVisibility(View.VISIBLE);
                    } else {
                        DSDK.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else if (MenuLoginMSSV.login == null) {
            String check = MenuLoginScan.scan.getText().toString();
            refSV.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String Role = snapshot.child(check).child("role").getValue(String.class);
                    if (Role.equals("admin")) {
                        DSDK.setVisibility(View.VISIBLE);
                    } else {
                        DSDK.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
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
        reference = FirebaseDatabase
                .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("DanhSachDangKy");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsDangKy.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModuleSV moduleSV = ds.getValue(ModuleSV.class);
                    dsDangKy.add(moduleSV);
                }
                AdapterDK adapter = new AdapterDK(DsDangKy.this,R.layout.design_ds_dangky,dsDangKy);
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
        DSDK = findViewById(R.id.DSDKAccept);
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