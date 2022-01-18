package com.vph.qltb.Scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.Menu.MenuLoginMSSV;
import com.vph.qltb.Menu.MenuLoginScan;
import com.vph.qltb.R;


public class LoginActivity extends AppCompatActivity {

    DatabaseReference reference = FirebaseDatabase
            .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference();

    EditText editMSSV, editMK;
    ImageView btnQuestion;
    Button btnScan, btnLogin;
    CheckBox chkHienThi;
    public static TextView scanError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        chkHienThi = findViewById(R.id.chkHienThi);
        editMK = findViewById(R.id.editMatKhau);
        editMSSV = findViewById(R.id.editMSSV);
        scanError= (TextView) findViewById(R.id.ScanError);
        btnScan=(Button) findViewById(R.id.btnScan);
        btnQuestion = (ImageView) findViewById(R.id.btnQuestion);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        ChucNang();
    }



    private void ChucNang() {
        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showtext();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        chkHienThi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    editMK.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    editMK.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }
    private void login(){
        String MSSV = editMSSV.getText().toString();
        String MK = editMK.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("ketqua", MSSV);
        if (MSSV.isEmpty()) {
            Toast.makeText(LoginActivity.this, "MSSV trống", Toast.LENGTH_SHORT).show();
        } else if(MK.isEmpty()){
            Toast.makeText(LoginActivity.this, "Mật khẩu trống", Toast.LENGTH_SHORT).show();
        }else{
            reference.child("DanhSachSinhVien").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Kiểm tra nếu MSSV/MK tồn tại
                    if(snapshot.hasChild(MSSV)){
                        final String getMK = snapshot.child(MSSV).child("matkhau").getValue(String.class);
                        if(getMK.equals(MK)){
                            Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MenuLoginMSSV.class);
                            intent.putExtra("Data", bundle);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this,"Mật khẩu sai!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"MSSV sai!",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
    private void showtext() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hướng dẫn");
        builder.setMessage("Cách 1: Bấm nút 'Quét mã vạch' và sử dụng thẻ sinh viên của bạn " +
                "\n\nCách 2: bạn có thể sử dụng MSSV và mật khẩu để đăng nhập");
        builder.setPositiveButton("OK",null);
        AlertDialog dialog = builder.create() ;
        dialog.show();
    }

    public void scanner(View view) {
        Intent intent = new Intent(LoginActivity.this, Scan.class);
        startActivity(intent);
    }
}