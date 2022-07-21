package com.vph.qltb.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ModuleHSSV;
import com.vph.qltb.ThietBi.QuyTac;

public class DangKyAccount extends AppCompatActivity {
    Button btnQuestion, btnBack, btnXoa, btnXacNhan,  btnCheckMSSV, btnReCheckMSSV;
    EditText editMSSV, editTen, editLop, editPhone, editMatKhau, editXacNhanMatKhau;
    TextView Rule;
    CheckBox checkMK, checkXacNhanMK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        Controls();
        Events();
    }

    private void Controls() {
        btnXacNhan = findViewById(R.id.btnXacnhan);
        btnXoa = findViewById(R.id.btnXoa);
        btnBack = findViewById(R.id.btnBack);
        btnQuestion = findViewById(R.id.btnQuestion);
        editMSSV = findViewById(R.id.editStudent_ID);
        editTen = findViewById(R.id.editStudent_Name);
        editLop = findViewById(R.id.editStudent_Class);
        editPhone = findViewById(R.id.editStudent_Phone);
        editMatKhau = findViewById(R.id.editStudent_PassWord);
        editXacNhanMatKhau = findViewById(R.id.editStudent_PassWordXacNhan);
        Rule = findViewById(R.id.Rule);
        btnCheckMSSV = findViewById(R.id.btnCheckTen);
        btnReCheckMSSV = findViewById(R.id.btnReCheckTen);
        checkMK = findViewById(R.id.chkHienThi);
        checkXacNhanMK = findViewById(R.id.chkHienThiXacNhan);
    }

    private void Events() {
        Rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rule = new Intent(DangKyAccount.this, QuyTac.class);
                startActivity(rule);
            }
        });
        //Check trước mới được nhập
        btnCheckMSSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTen();
            }

        });

        btnReCheckMSSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnReCheckMSSV.setVisibility(View.GONE);
                btnCheckMSSV.setVisibility(View.VISIBLE);
                btnXacNhan.setVisibility(View.GONE);
                editMSSV.setEnabled(true);
            }
        });
        //Hiển thị mật khẩu
        checkMK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    editMatKhau.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    editMatKhau.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        checkXacNhanMK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    editXacNhanMatKhau.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    editXacNhanMatKhau.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        //Quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Lưu ý
        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DangKyAccount.this);
                        builder.setTitle("Lưu ý").setIcon(R.drawable.question);
                        builder.setMessage("MSSV dùng để đăng nhập" + "\nVui lòng bấm vào Icon bên cạnh MSSV để kiểm tra!");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });
        //Đăng ký


        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangky();
            }
        });
        //btnXoaDuLieu
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMSSV.setText("");
                editTen.setText("");
                editLop.setText("");
                editPhone.setText("");
                editMatKhau.setText("");
                editXacNhanMatKhau.setText("");
                btnXacNhan.setVisibility(View.GONE);
                btnCheckMSSV.setVisibility(View.VISIBLE);
                btnReCheckMSSV.setVisibility(View.GONE);
                editMSSV.setEnabled(true);
            }
        });
    }

    public void checkTen() {
        String MSSV = editMSSV.getText().toString();
        if(MSSV.equals("")){
            Toast.makeText(DangKyAccount.this, "MSSV trống!", Toast.LENGTH_SHORT).show();
        }else {
            if (MSSV.length()<9) {
                Toast.makeText(DangKyAccount.this, "MSSV có 9 kí tự", Toast.LENGTH_SHORT).show();
            } else {
                FireBaseHelper.reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(MSSV)) {
                            AlertDialog.Builder tontai = new AlertDialog.Builder(DangKyAccount.this)
                                    .setTitle("Thông báo!")
                                    .setIcon(R.drawable.ic_warning)
                                    .setMessage("MSSV đã tồn tại trong hệ thống!")
                                    .setNegativeButton("OK", null);
                            AlertDialog dialog = tontai.create();
                            dialog.show();
                        } else {
                            btnReCheckMSSV.setVisibility(View.VISIBLE);
                            btnCheckMSSV.setVisibility(View.GONE);
                            btnXacNhan.setVisibility(View.VISIBLE);
                            editMSSV.setEnabled(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    private void dangky() {
        String MSSV = editMSSV.getText().toString();
        String Ten = editTen.getText().toString();
        String Lop = editLop.getText().toString();
        String Phone = editPhone.getText().toString();
        String MatKhau = editMatKhau.getText().toString();
        String XacNhanMatKhau = editXacNhanMatKhau.getText().toString();
        String role = "sinhvien";
        ModuleHSSV dangky = new ModuleHSSV(MSSV, Ten, Lop, Phone, MatKhau, role);
        if (MSSV.equals("") || Ten.equals("") || Lop.equals("") || Phone.equals("") || MatKhau.equals("") || XacNhanMatKhau.equals("")) {
            Toast.makeText(DangKyAccount.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        } else {
            if (MatKhau.length() < 5) {
                Toast.makeText(DangKyAccount.this, "Vui lòng nhập mật khẩu có độ dài 6 trở lên!", Toast.LENGTH_SHORT).show();
            } else {
                if (MatKhau.equals(XacNhanMatKhau)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DangKyAccount.this);
                    builder.setTitle("Thông báo").setIcon(R.drawable.question);
                    builder.setMessage("Xác nhận đăng ký" +
                            "\nMSSV: "+ MSSV +
                            "\nTên: "+ Ten +
                            "\nLớp: "+ Lop +
                            "\nSĐT: "+ Phone);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FireBaseHelper.reference.child("Account").child(MSSV).setValue(dangky);
                            Toast.makeText(DangKyAccount.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            dangnhap();
                        }
                    });
                    builder.setPositiveButton("NO",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(DangKyAccount.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void dangnhap(){
        AlertDialog.Builder dangnhap = new AlertDialog.Builder(DangKyAccount.this);
        dangnhap.setTitle("Thông báo").setIcon(R.drawable.question);
        dangnhap.setMessage("Đăng nhập ngay?");
        dangnhap.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = new Bundle();
                bundle.putString("MSSV", editMSSV.getText().toString());
                bundle.putString("MK", editMatKhau.getText().toString());
                Intent intent = new Intent(DangKyAccount.this, LoginActivity.class);
                intent.putExtra("DangKy", bundle);
                startActivity(intent);
            }
        });
        dangnhap.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editMSSV.setText("");
                editTen.setText("");
                editLop.setText("");
                editPhone.setText("");
                editMatKhau.setText("");
                editXacNhanMatKhau.setText("");
                btnXacNhan.setVisibility(View.GONE);
                btnCheckMSSV.setVisibility(View.VISIBLE);
                btnReCheckMSSV.setVisibility(View.GONE);
                editMSSV.setEnabled(true);
            }
        });
        AlertDialog dangnhapdialog = dangnhap.create();
        dangnhapdialog.show();
    }
}