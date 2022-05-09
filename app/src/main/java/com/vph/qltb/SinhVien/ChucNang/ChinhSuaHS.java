package com.vph.qltb.SinhVien.ChucNang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ModuleHSSV;

public class ChinhSuaHS extends AppCompatActivity {
    EditText mssv, ten, lop, sdt;
    ImageButton btnBack, btnUpdate;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_hs);
        Controls();
        xulyButton();
        autofill();
    }

    private void autofill() {
        reference = FirebaseDatabase
                .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Account");
        mssv.setText(Menu.login.getText().toString());
        String HsMSSV = mssv.getText().toString();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void xulyButton() {
        //Quay lại
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
                update();
            }
        });
    }
    private void update(){
        if(isConnected()){
            reference = FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Account");
            //Get all the values
            String Ten = ten.getText().toString();
            String Lop = lop.getText().toString();
            String Sdt = sdt.getText().toString();
            String Mssv = mssv.getText().toString();
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(Mssv)) {
                        final String getRole = snapshot.child(Mssv).child("role").getValue(String.class);
                        final String getMK = snapshot.child(Mssv).child("matkhau").getValue(String.class);
                    ModuleHSSV moduleHSSV = new ModuleHSSV(Ten, Lop, Sdt, Mssv, getRole, getMK);
                    if (Ten.isEmpty() || Lop.isEmpty() || Sdt.isEmpty() || Mssv.isEmpty()) {
                        Toast.makeText(ChinhSuaHS.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChinhSuaHS.this)
                                .setTitle("Thông báo!")
                                .setIcon(R.drawable.question)
                                .setMessage("Đồng ý chỉnh sửa "
                                        + "\nMSSV: " + Mssv
                                        + "\nTên: " + Ten
                                        + "\nLớp: " + Lop
                                        + "\nSđt: " + Sdt)

                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Toast.makeText(ChinhSuaHS.this, "Chỉnh sửa thành công!", Toast.LENGTH_SHORT).show();
                                        reference.child(Mssv).setValue(moduleHSSV);
                                        onBackPressed();
                                    }
                                }).setPositiveButton("NO", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ChinhSuaHS.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private void Controls() {
        mssv = findViewById(R.id.editHsMssv);
        ten = findViewById(R.id.editHsTenSV);
        lop = findViewById(R.id.editHSLop);
        sdt = findViewById(R.id.editHSsdt);
        btnUpdate = findViewById(R.id.btnHSXacnhan);
        btnBack = findViewById(R.id.btnHSQuaylai);
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