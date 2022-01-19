package com.vph.qltb.SinhVien.Muon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
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

public class SinhVienM extends AppCompatActivity {

    ListView lvNguoiMuon;
    ArrayList<ModuleSV> dsMuon;
    ImageButton btnRestartMN, btnBack;
    SearchView searchView;

    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsach_vn_muon);
        addControls();
        addEvents();


        hienthiDanhSach();
    }

    private void hienthiDanhSach() {
        reference = FirebaseDatabase
                .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("DanhSachMuon");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsMuon.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModuleSV moduleSV = ds.getValue(ModuleSV.class);
                    dsMuon.add(moduleSV);
                }
                AdapterSV adapter = new AdapterSV(SinhVienM.this,R.layout.design_thietbi,dsMuon);
                lvNguoiMuon.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvents() {
        btnRestartMN.setOnClickListener(new View.OnClickListener(){
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
        //Tìm kiếm sinh viên
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<ModuleSV> filterMN = new ArrayList<>();
                for(ModuleSV moduleSV : dsMuon){
                    if(moduleSV.getSinhvien().toLowerCase().contains(s.toLowerCase())){
                        filterMN.add(moduleSV);

                    }else if(moduleSV.getMssv().toLowerCase().contains(s.toLowerCase())){
                        filterMN.add(moduleSV);
                    }else if(moduleSV.getTenthietbi().toLowerCase().contains(s.toLowerCase())){
                        filterMN.add(moduleSV);
                    }else if(moduleSV.getSdt().toLowerCase().contains(s.toLowerCase())){
                        filterMN.add(moduleSV);
                    }else if(moduleSV.getLop().toLowerCase().contains(s.toLowerCase())){
                        filterMN.add(moduleSV);
                    }

                }
                AdapterSV adapterSV = new AdapterSV(getApplicationContext(),0,filterMN);
                lvNguoiMuon.setAdapter(adapterSV);
                return false;
            }
        });
    }



    private void addControls() {
        searchView = findViewById(R.id.search);
        btnBack = findViewById(R.id.btnQuaylaiMenu);
        btnRestartMN = findViewById(R.id.btnRestartNM);
        lvNguoiMuon = findViewById(R.id.lvNguoiMuon);
        dsMuon = new ArrayList<>();
    }



    public void Restart(){
        Intent intent = new Intent(this, SinhVienM.class);
        startActivity(intent);
        finish();
        Toast.makeText(this,"Trang đã được tải lại",Toast.LENGTH_SHORT).show();
    }
}