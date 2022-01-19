package com.vph.qltb.SinhVien.DangKy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.vph.qltb.R;
import com.vph.qltb.SinhVien.AdapterSV;
import com.vph.qltb.SinhVien.ModuleSV;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SinhVienDK extends AppCompatActivity {

    EditText mssv, sdt, sinhvien, tenthietbi, soluong, ngaymuon, hantra, stt;
    ImageButton btnRestartDK, btnBack;
    ListView lvDanKy;
    ArrayList<ModuleSV> dsDangKy;
    Button  btnThem, btnXoa;
    CheckBox checkBox;


    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsach_sv_dangky);
        addControls();
        addEvents();
        hienthiDanhSach();
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
                AdapterSV adapter = new AdapterSV(SinhVienDK.this,R.layout.design_thietbi,dsDangKy);
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
        Intent intent = new Intent(this, SinhVienDK.class);
        startActivity(intent);
        finish();
        Toast.makeText(this,"Trang đã được tải lại",Toast.LENGTH_SHORT).show();
    }
}