package com.vph.qltb.SinhVien.ChucNang;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.SinhVien.ModuleSV;
import com.vph.qltb.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PhieuDangKy extends AppCompatActivity {
    TextView Rule;
    EditText tenthietbi, soluong, lydo;
    CheckBox chkCamKet;
    Button btnBack, btnXoa, btnXacNhan, btnUp, btnDown;



    Calendar calendar = Calendar.getInstance(); // Lấy thời gian hiện tại
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy"); //Format hiển thị ngày/tháng/năm
    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_dang_ky);
        addControls();
        addEvents();
        ChonTB();

    }


    private void ChonTB() {
        //TB mượn
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Muon");
        if(bundle == null){

        }else{
            tenthietbi.setText(bundle.getString("KQMuon"));
        }

    }
    private void addEvents() {
        //Quy tắc
        Rule.setPaintFlags(Rule.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        Rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent rule = getIntent(this, Rule.class);
//                startActivity(rule);
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
//        ngaymuon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog.OnDateSetListener callback2 = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int y, int M, int d) {
//                        calendar.set(Calendar.YEAR, y);
//                        calendar.set(Calendar.MONTH, M);
//                        calendar.set(Calendar.DATE, d);
//                        ngaymuon.setText(sdfD.format(calendar.getTime()));
//                    }
//                };
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        PhieuDangKy.this,
//                        callback2,
//                        calendar.get(Calendar.YEAR),
//                        calendar.get(Calendar.MONTH),
//                        calendar.get(Calendar.DATE)
//                );
//                datePickerDialog.show();
//            }
//        });
        //Pick ngày trả
//        hantra.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog.OnDateSetListener callback2 = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int y, int M, int d) {
//                        calendar.set(Calendar.YEAR, y);
//                        calendar.set(Calendar.MONTH, M);
//                        calendar.set(Calendar.DATE, d);
//                    }
//                };
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        PhieuDangKy.this,
//                        callback2,
//                        calendar.get(Calendar.YEAR),
//                        calendar.get(Calendar.MONTH),
//                        calendar.get(Calendar.DATE)
//                );
//                datePickerDialog.show();
//            }
//        });
        //Xóa danh sách vừa nhập
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lydo.setText(null);
                soluong.setText("1");
                chkCamKet.setChecked(false);
            }
        });
        //Xác nhận mượn thiết bị
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Muon();
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
                    int up = Integer.parseInt(num);
                    up += 1;
                    soluong.setText(String.valueOf(up));
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

    private void Muon() {
        if (isConnected()) {
            if (chkCamKet.isChecked()) {
                //values
                String dateDK = currentDate.format(calendar.getTime());
                String timeDK = currentTime.format(calendar.getTime());
                String key = FireBaseHelper.reference.push().getKey();
                String ThietBi = tenthietbi.getText().toString();
                String SoLuong = soluong.getText().toString();
                String Lydo = lydo.getText().toString();
                String tinhtrang = "1";
                FireBaseHelper.reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                        final String Mssv = Menu.login;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Mssv)) {
                                final String getSV = snapshot.child(Mssv).child("sinhvien").getValue(String.class);
                                ModuleSV moduleThietBi = new ModuleSV(SoLuong, ThietBi, dateDK, timeDK, tinhtrang, Lydo, key);
                                if (ThietBi.isEmpty() || SoLuong.isEmpty() || Lydo.isEmpty()) {
                                    Toast.makeText(PhieuDangKy.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                                } else {
                                                int sl = Integer.parseInt(SoLuong);
                                                if (sl <= 0) {
                                                    AlertDialog.Builder bsl = new AlertDialog.Builder(PhieuDangKy.this);
                                                    bsl.setTitle("Thông báo!").setIcon(R.drawable.question);
                                                    bsl.setMessage("Số lượng mượn phải lớn hơn 0!");
                                                    bsl.setNegativeButton("OK", null);
                                                    AlertDialog dialogsl = bsl.create();
                                                    dialogsl.show();
                                                } else {
                                                    AlertDialog.Builder bdk = new AlertDialog.Builder(PhieuDangKy.this);
                                                    bdk.setTitle("Thông báo!").setIcon(R.drawable.question);
                                                    bdk.setMessage("Xác nhận mượn thiết bị " + ThietBi
                                                            + "\nSố lượng: " + SoLuong
                                                            + "\nLý do: " + Lydo);
                                                    bdk.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            FireBaseHelper.reference.child("DanhSachDangKy").child(Mssv).child("sinhvien").setValue(getSV);
                                                            FireBaseHelper.reference.child("DanhSachDangKy").child(Mssv).child("mssv").setValue(Mssv);
                                                            FireBaseHelper.reference.child("DanhSachDangKy").child(Mssv).child("Thiết bị").child(key).setValue(moduleThietBi);

                                                                lydo.setText(null);
                                                                soluong.setText("1");
                                                                chkCamKet.setChecked(false);
                                                                Toast.makeText(PhieuDangKy.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                                            }
                                                    });
                                                    bdk.setPositiveButton("NO", null);
                                                    AlertDialog dialog = bdk.create();
                                                    dialog.show();
                                                }

                                            }
                                }

                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            } else {
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
        chkCamKet = findViewById(R.id.chkCamKet);
        chkCamKet.setVisibility(View.VISIBLE);
        soluong = findViewById(R.id.editDevice_Number);
        tenthietbi = findViewById(R.id.editDevice_Name);
        lydo = findViewById(R.id.editReason);
        btnBack = findViewById(R.id.btnBack);
        btnXacNhan = findViewById(R.id.btnXacnhan);
        btnXoa = findViewById(R.id.btnClear);
        btnUp = findViewById(R.id.upDK);
        btnDown = findViewById(R.id.downDK);
        Rule = findViewById(R.id.Rule);
    }

    //Kiểm tra kết nối internet
    boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(PhieuDangKy.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return networkInfo.isConnected();
        } else
            return false;
    }
}