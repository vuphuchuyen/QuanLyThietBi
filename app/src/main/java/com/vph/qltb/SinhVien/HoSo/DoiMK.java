package com.vph.qltb.SinhVien.HoSo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;

public class DoiMK extends AppCompatActivity {
    EditText PW;
    Button btnXacNhan;
    CheckBox chkHienThi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mk);
        Controls();
        Events();


    }
    private void Controls(){
        PW = findViewById(R.id.editStudent_PassWord);
        btnXacNhan = findViewById(R.id.btnXacnhan);
        chkHienThi = findViewById(R.id.chkHienThi);
    }
    private void Events(){
        //Hiển thị mật khẩu
        chkHienThi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    PW.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    PW.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        //Nhập mật khẩu mới
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doimatkhau();
            }
        });
    }
    public void doimatkhau(){
        String key = Menu.login;
        String Pass = PW.getText().toString();
        FireBaseHelper.reference.child("Account").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Pass.equals("")){
                    Toast.makeText(DoiMK.this, "Mật khẩu không được phép trống", Toast.LENGTH_SHORT).show();
                }else {
                    if (Pass.length() < 5) {
                        Toast.makeText(DoiMK.this, "Vui lòng nhập mật khẩu có độ dài 6 trở lên!", Toast.LENGTH_SHORT).show();
                    } else {
                        FireBaseHelper.reference.child("Account").child(key).child("matkhau").setValue(Pass);
                        Toast.makeText(DoiMK.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}