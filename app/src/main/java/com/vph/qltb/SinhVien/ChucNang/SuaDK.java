package com.vph.qltb.SinhVien.ChucNang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.SinhVien.ModuleSV;
import com.vph.qltb.R;

public class SuaDK extends AppCompatActivity {
    TextView Rule, DangKy;
    EditText tenthietbi, soluong, lydo;
    Button btnBack, btnXoa, btnXacNhan, btnUp, btnDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_dang_ky);
        Controls();
        addEvents();
    }

    private void Controls() {
        //txtDangKy.findViewById(R.id.txtDangKy);
        soluong = findViewById(R.id.editDevice_Number);
        tenthietbi = findViewById(R.id.editDevice_Name);
        lydo = findViewById(R.id.editReason);
        btnBack = findViewById(R.id.btnBack);
        btnXacNhan = findViewById(R.id.btnXacnhan);
        DangKy = findViewById(R.id.txtDangKy);
        btnXoa = findViewById(R.id.btnClear);

        btnUp = findViewById(R.id.upDK);
        btnDown = findViewById(R.id.downDK);
        Rule = findViewById(R.id.Rule);
    }

    private void addEvents() {
        DangKy.setText("Sửa đăng ký");
        Rule.setVisibility(View.GONE);
        btnXacNhan.setText("Xác nhận");
        btnXoa.setVisibility(View.GONE);
        //Hiển thị
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("SuaTT");
        String key = bundle.getString("key");
        String mssv = Menu.login;
        //Lấy danh sách
        FireBaseHelper.reference.child("DanhSachDangKy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(mssv)){
                    final String getTen = snapshot.child(mssv).child("Thiết bị").child(key).child("tenthietbi").getValue(String.class);
                    final String getSL = snapshot.child(mssv).child("Thiết bị").child(key).child("soluong").getValue(String.class);
                    final String getLydo = snapshot.child(mssv).child("Thiết bị").child(key).child("lydo").getValue(String.class);
                    tenthietbi.setText(getTen);
                    soluong.setText(getSL);
                    lydo.setText(getLydo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Sửa đăng ký
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SuaDK.this);
                builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_warning);
                builder.setMessage("Xác nhận chỉnh sửa thông tin mượn?");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FireBaseHelper.reference.child("DanhSachDangKy").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(mssv)){
                                    final String getTen = snapshot.child(mssv).child("Thiết bị").child(key).child("tenthietbi").getValue(String.class);
                                    final String getDateDK = snapshot.child(mssv).child("Thiết bị").child(key).child("dateDK").getValue(String.class);
                                    final String getTimeDK = snapshot.child(mssv).child("Thiết bị").child(key).child("timeDK").getValue(String.class);
                                    final String getStatus = "1";
                                    String getSL = soluong.getText().toString();
                                    String getLydo = lydo.getText().toString();
                                    ModuleSV moduleSV = new ModuleSV(getSL, getTen, getDateDK, getTimeDK, getStatus, getLydo, key);
                                    FireBaseHelper.reference.child("DanhSachDangKy").child(mssv).child("Thiết bị").child(key).setValue(moduleSV);
                                    Toast.makeText(SuaDK.this, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
                builder.setPositiveButton("NO", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //quay về
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

}