package com.vph.qltb.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;


public class LoginActivity extends AppCompatActivity {

    EditText editMSSV, editMK;
    Button btnScan, btnLogin, btnQuestion, btnDangKy;
    CheckBox chkHienThi, chkSave;
    public static CheckBox chkSaveLogin;
    String mssv = "mssv";
    public static TextView scanError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        chkHienThi = findViewById(R.id.chkHienThi);
        editMK = findViewById(R.id.editMatKhau);
        editMSSV = findViewById(R.id.editMSSV);
        scanError= findViewById(R.id.ScanError);
        btnScan= findViewById(R.id.btnScan);
        btnQuestion = findViewById(R.id.btnQuestion);
        btnLogin = findViewById(R.id.btnLogin);
        chkSaveLogin = findViewById(R.id.chkSaveLogin);
        chkSave = findViewById(R.id.chkSave);
        btnDangKy = findViewById(R.id.btnDangKy);
        ChucNang();
        DangKytoLogin();
    }

    private void DangKytoLogin() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("DangKy");
        if(bundle==null){

        }else {
            editMSSV.setText(bundle.getString("MSSV"));
            editMK.setText(bundle.getString("MK"));
        }
    }


    private void ChucNang() {
        //????ng k??
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, DangKyAccount.class);
                startActivity(intent);
            }
        });

        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("H?????ng d???n").setIcon(R.drawable.question);
                builder.setMessage("C??ch 1: B???m n??t 'Qu??t m?? v???ch' v?? s??? d???ng th??? sinh vi??n c???a b???n " +
                        "\n\nC??ch 2: b???n c?? th??? s??? d???ng MSSV v?? m???t kh???u ????? ????ng nh???p");
                builder.setPositiveButton("OK",null);
                AlertDialog dialog = builder.create() ;
                dialog.show();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    login();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("C???nh b??o!").setIcon(R.drawable.ic_wifiout);
                    builder.setMessage("Vui l??ng ki???m tra l???i k???t n???i Internet c???a b???n!");
                    builder.setNegativeButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
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
        //L??u MSSV
        SharedPreferences sharedPreferences = getSharedPreferences(mssv, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MSSV",editMSSV.getText().toString());
        editor.putBoolean("Save",chkSave.isChecked());
        editor.apply();
        //Duy tr?? ????ng nh???p
            SharedPreferences sharedPreferences2 = getSharedPreferences(mssv, MODE_PRIVATE);
            SharedPreferences.Editor editorLogin = sharedPreferences2.edit();
            editorLogin.putBoolean("SaveLogin", chkSaveLogin.isChecked());

            editorLogin.apply();
        //Ki???m tra Internet trc khi cho login
        if (MSSV.isEmpty()) {
            Toast.makeText(LoginActivity.this, "MSSV tr???ng", Toast.LENGTH_SHORT).show();
        } else if (MK.isEmpty()) {
            Toast.makeText(LoginActivity.this, "M???t kh???u tr???ng", Toast.LENGTH_SHORT).show();
        } else {
            FireBaseHelper.reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Ki???m tra n???u MSSV/MK t???n t???i
                    if (snapshot.hasChild(MSSV)) {
                        final String getMK = snapshot.child(MSSV).child("matkhau").getValue(String.class);
                         if (getMK.equals(MK)) {
                            Toast.makeText(LoginActivity.this, "????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, Menu.class);
                            intent.putExtra("Data", bundle);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "M???t kh???u sai!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "MSSV sai!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    public void scanner(View view) {
        if (isConnected()) {
            Intent intent = new Intent(LoginActivity.this, ScanHelper.class);
            startActivity(intent);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("C???nh b??o!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui l??ng ki???m tra l???i k???t n???i Internet c???a b???n!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(mssv,MODE_PRIVATE);
        String LuuMSSV = sharedPreferences.getString("MSSV","");
        boolean save = sharedPreferences.getBoolean("Save",false);

        if(save){
            chkSave.setChecked(true);
            editMSSV.setText(LuuMSSV);
        }
        SharedPreferences sharedPreferences2 = getSharedPreferences(mssv,MODE_PRIVATE);
        SharedPreferences.Editor editorLogin = sharedPreferences2.edit();
        boolean savelogin = sharedPreferences2.getBoolean("SaveLogin",false);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("False");
        if(bundle==null){
            if(savelogin){
            String MSSV = editMSSV.getText().toString();
            Bundle bundle2 = new Bundle();
            bundle2.putString("ketqua", MSSV);
            Intent intent2 = new Intent(LoginActivity.this, Menu.class);
            intent2.putExtra("Data", bundle2);
            startActivity(intent2);
            finish();
            }
        }
        else{
            editorLogin.remove("SaveLogin");
            editorLogin.apply();
        }


    }
    //Ki???m tra k???t n???i internet
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