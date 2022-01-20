package com.vph.qltb.SinhVien.DangKy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.Menu.MenuLoginMSSV;
import com.vph.qltb.Menu.MenuLoginScan;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ModuleSV;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PhieuDangKy extends AppCompatActivity {
    EditText sinhvien, sdt, mssv, tenthietbi, soluong, ngaymuon, hantra, lop;
    CheckBox chkCamKet;
    ImageButton btnBack, btnXoa, btnXacNhan, btnUp, btnDown;
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
        if (MenuLoginScan.scan == null) {
            mssv.setText(MenuLoginMSSV.login.getText().toString());
        } else {
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
                        calendar.set(Calendar.YEAR, y);
                        calendar.set(Calendar.MONTH, M);
                        calendar.set(Calendar.DATE, d);
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
                        calendar.set(Calendar.YEAR, y);
                        calendar.set(Calendar.MONTH, M);
                        calendar.set(Calendar.DATE, d);
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
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sdt.setText("");
                lop.setText("");
                sinhvien.setText("");
                tenthietbi.setText("");
                soluong.setText("");
                ngaymuon.setText("");
                hantra.setText("");
            }
        });
        //Đồng ý thêm
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Them();
            }
        });

        //Tăng số lượng
        btnUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String num = soluong.getText().toString();
                if (num.isEmpty()) {
                    soluong.setText("1");
                } else {
                    int plus = Integer.parseInt(num);
                    plus += 1;
                    soluong.setText(String.valueOf(plus));
                }
            }
        });
        //Giảm số lượng
        btnDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String num = soluong.getText().toString();
                if (num.isEmpty()) {
                    soluong.setText("1");
                } else {
                    int down = Integer.parseInt(num);
                    down -= 1;
                    soluong.setText(String.valueOf(down));
                }
            }
        });

    }

    private void show() {
        Toast.makeText(this, "Vị trí này không thể sửa", Toast.LENGTH_SHORT).show();
    }

    private void Them() {
        if (chkCamKet.isChecked()) {
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
            ModuleSV moduleSV = new ModuleSV(TenSV, Lop, SDT, MSSV, SoLuong, ThietBi, ngayMuon, ngayTra);


            if (Lop.isEmpty() || TenSV.isEmpty() || ThietBi.isEmpty() || SoLuong.isEmpty() || SDT.isEmpty() || ngayTra.isEmpty() || ngayMuon.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
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
        } else {
            Toast.makeText(this, "Bạn chưa ấn vào cam kết", Toast.LENGTH_SHORT).show();
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
        btnUp = findViewById(R.id.upDK);
        btnDown = findViewById(R.id.downDK);
    }
}