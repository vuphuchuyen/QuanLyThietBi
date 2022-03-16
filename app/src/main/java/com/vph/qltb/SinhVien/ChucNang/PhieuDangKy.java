package com.vph.qltb.SinhVien.ChucNang;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    EditText tenthietbi, soluong, ngaymuon, hantra;
    Button btnChon;
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
    }

    private void addEvents() {
        //Pick thiết bị
        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

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


    private void Them() {
        if(isConnected()) {
            if (chkCamKet.isChecked()) {
                reference = FirebaseDatabase
                        .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference();
                //values
                String key = reference.push().getKey();
                String ThietBi = tenthietbi.getText().toString();
                String SoLuong = soluong.getText().toString();
                String ngayMuon = ngaymuon.getText().toString();
                String ngayTra = hantra.getText().toString();
                if (MenuLoginScan.scan == null) {
                    reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                        String Mssv = MenuLoginMSSV.login.getText().toString();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Mssv)) {
                                final String getSV = snapshot.child(Mssv).child("sinhvien").getValue(String.class);
                                final String getSDT = snapshot.child(Mssv).child("sdt").getValue(String.class);
                                final String getLop = snapshot.child(Mssv).child("lop").getValue(String.class);
                                ModuleSV moduleSV = new ModuleSV(getSV, getLop, getSDT, Mssv, SoLuong, ThietBi, ngayMuon, ngayTra,key);
                                if (ThietBi.isEmpty() || SoLuong.isEmpty() | ngayTra.isEmpty() || ngayMuon.isEmpty()) {
                                    Toast.makeText(PhieuDangKy.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PhieuDangKy.this);
                                    builder.setTitle("Thông báo!").setIcon(R.drawable.question);
                                    builder.setMessage("Xác nhận mượn thiết bị " + ThietBi
                                            + "\nSố lượng: " + SoLuong
                                            + "\nNgày mượn: " + ngayMuon
                                            + "\nHạn trả " + ngayTra);
                                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            reference.child("DanhSachDangKy").child(key).setValue(moduleSV);
                                            tenthietbi.setText("");
                                            soluong.setText("");
                                            String date = sdfD.format(calendar.getTime());
                                            ngaymuon.setText(date);
                                            hantra.setText(date);
                                            Toast.makeText(PhieuDangKy.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    builder.setPositiveButton("NO", null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    String Mssv = MenuLoginScan.scan.getText().toString();
                    reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Mssv)) {
                                final String getSV = snapshot.child(Mssv).child("sinhvien").getValue(String.class);
                                final String getSDT = snapshot.child(Mssv).child("sdt").getValue(String.class);
                                final String getLop = snapshot.child(Mssv).child("lop").getValue(String.class);
                                ModuleSV moduleSV = new ModuleSV(getSV, getLop, getSDT, Mssv, SoLuong, ThietBi, ngayMuon, ngayTra, key);
                                if (ThietBi.isEmpty() || SoLuong.isEmpty() | ngayTra.isEmpty() || ngayMuon.isEmpty()) {
                                    Toast.makeText(PhieuDangKy.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PhieuDangKy.this);
                                    builder.setTitle("Thông báo!").setIcon(R.drawable.question);
                                    builder.setMessage("Xác nhận mượn thiết bị " + ThietBi
                                            + "\nSố lượng: " + SoLuong
                                            + "\nNgày mượn: " + ngayMuon
                                            + "\nHạn trả " + ngayTra);
                                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            reference.child("DanhSachDangKy").child(key).setValue(moduleSV);
                                            tenthietbi.setText("");
                                            soluong.setText("");
                                            String date = sdfD.format(calendar.getTime());
                                            ngaymuon.setText(date);
                                            hantra.setText(date);
                                            Toast.makeText(PhieuDangKy.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    builder.setPositiveButton("NO", null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }else
                {
                    Toast.makeText(PhieuDangKy.this, "Bạn chưa ấn vào cam kết", Toast.LENGTH_SHORT).show();
                }


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(PhieuDangKy.this);
                builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
                builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
                builder.setNegativeButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }


    private void addControls() {
        btnChon = findViewById(R.id.btnChonTbMuon);
        chkCamKet = findViewById(R.id.chkCamKet);
        soluong = findViewById(R.id.edtSoluong);
        tenthietbi = findViewById(R.id.edtThietBi);
        ngaymuon = findViewById(R.id.edtNgayMuon);
        hantra = findViewById(R.id.edtNgayTra);
        btnBack = findViewById(R.id.btnQuaylaiMenu);
        btnXacNhan = findViewById(R.id.btnXacnhan);
        btnXoa = findViewById(R.id.btnClear);
        btnUp = findViewById(R.id.upDK);
        btnDown = findViewById(R.id.downDK);

        String date = sdfD.format(calendar.getTime());
        ngaymuon.setText(date);
        hantra.setText(date);
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