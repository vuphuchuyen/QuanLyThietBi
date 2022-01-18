package com.vph.qltb.SinhVien.DangKy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.Menu.MenuLoginMSSV;
import com.vph.qltb.Menu.MenuLoginScan;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ModuleSV;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PhieuDangKy extends AppCompatActivity {
    public static Button btnBack, btnXoa, btnXacNhan;
    EditText sinhvien, sdt, mssv, tenthietbi, soluong, ngaymuon, hantra, lop;
    CheckBox chkCamKet;

    Calendar calendar = Calendar.getInstance(); // Lấy thời gian hiện tại
    SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy"); //Format hiển thị ngày/tháng/năm

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_dang_ky);
        addControls();
        addEvents();
        autoFill();
    }

    private void autoFill() {
        reference = FirebaseDatabase
                .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();
        if(MenuLoginScan.scan == null){
            mssv.setText(MenuLoginMSSV.login.getText().toString());
        }else{
            mssv.setText(MenuLoginScan.scan.getText().toString());
        }
        String MSSV = mssv.getText().toString();
        reference.child("DanhSachSinhVien").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(MSSV)) {
                    final String getSV = snapshot.child(MSSV).child("sinhvien").getValue(String.class);
                    final String getSDT = snapshot.child(MSSV).child("sdt").getValue(String.class);
                    final String getLop = snapshot.child(MSSV).child("lop").getValue(String.class);
                    sinhvien.setText(getSV);
                    sdt.setText(getSDT);
                    lop.setText(getLop);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvents() {
        //ko thay đổi

        //Quay về Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Pick ngày mượn
        ngaymuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener callback2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int M, int d) {
                        calendar.set(calendar.YEAR,y);
                        calendar.set(calendar.MONTH,M);
                        calendar.set(calendar.DATE,d);
                        ngaymuon.setText(sdfD.format(calendar.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PhieuDangKy.this,
                        callback2,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE)
                );
                datePickerDialog.show();
            }
        });
        //Pick ngày trả
        hantra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener callback2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int M, int d) {
                        calendar.set(calendar.YEAR,y);
                        calendar.set(calendar.MONTH,M);
                        calendar.set(calendar.DATE,d);
                        hantra.setText(sdfD.format(calendar.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PhieuDangKy.this,
                        callback2,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE)
                );
                datePickerDialog.show();
            }
        });
        //Xóa danh sách vừa nhập
        btnXoa.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                sdt.setText("");
                lop.setText("");
                sinhvien.setText("");
                tenthietbi.setText("");
                soluong.setText("");
                ngaymuon.setText("");
                hantra.setText("");
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Them();
            }
        });


    }

    private void show() {
        Toast.makeText(this,"Vị trí này không thể sửa",Toast.LENGTH_SHORT).show();
    }

    private void Them() {
        if(chkCamKet.isChecked()){
            reference = FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("DanhSachDangKy");


            //Get all the values
            String TenSV = sinhvien.getText().toString();
            String Lop = lop.getText().toString();
            String ThietBi = tenthietbi.getText().toString();
            String SoLuong = soluong.getText().toString();
            String SDT = sdt.getText().toString();
            String ngayMuon = ngaymuon.getText().toString();
            String ngayTra = hantra.getText().toString();
            String MSSV = mssv.getText().toString();
            ModuleSV moduleSV = new ModuleSV(TenSV, Lop, SDT, MSSV,SoLuong,ThietBi,ngayMuon,ngayTra);


            if( Lop.isEmpty() || TenSV.isEmpty() || ThietBi.isEmpty() || SoLuong.isEmpty() || SDT.isEmpty() ||ngayTra.isEmpty() || ngayMuon.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                reference.push().setValue(moduleSV);
                sdt.setText("");
                lop.setText("");
                sinhvien.setText("");
                tenthietbi.setText("");
                soluong.setText("");
                ngaymuon.setText("");
                hantra.setText("");
            }
        }else{
            Toast.makeText(this,"Bạn chưa ấn vào cam kết", Toast.LENGTH_SHORT).show();
        }
    }

    private void addControls() {
        lop = findViewById(R.id.edtLop);
        mssv = findViewById(R.id.edtMSSV);
        chkCamKet = findViewById(R.id.chkCamKet);
        soluong = findViewById(R.id.edtSoluong);
        sinhvien = findViewById(R.id.edtTenSV);
        tenthietbi = findViewById(R.id.edtThietBi);
        sdt = findViewById(R.id.edtSDT);
        ngaymuon = findViewById(R.id.edtNgayMuon);
        hantra = findViewById(R.id.edtNgayTra);
        btnBack = findViewById(R.id.btnQuaylaiMenu);
        btnXacNhan = findViewById(R.id.btnXacnhan);
        btnXoa = findViewById(R.id.btnClear);

    }
}