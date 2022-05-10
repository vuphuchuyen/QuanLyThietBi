package com.vph.qltb.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.assist.AssistStructure;
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
import android.widget.ImageButton;
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
    ImageButton btnQuestion;
    Button btnScan, btnLogin;
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
        scanError= (TextView) findViewById(R.id.ScanError);
        btnScan=(Button) findViewById(R.id.btnScan);
        btnQuestion = (ImageButton) findViewById(R.id.btnQuestion);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        chkSaveLogin = findViewById(R.id.chkSaveLogin);
        chkSave = findViewById(R.id.chkSave);
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
                if (isConnected()) {
                    login();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
                    builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
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
        //Lưu MSSV
        SharedPreferences sharedPreferences = getSharedPreferences(mssv, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MSSV",editMSSV.getText().toString());
        editor.putBoolean("Save",chkSave.isChecked());
        editor.apply();
        //Duy trì đăng nhập
            SharedPreferences sharedPreferences2 = getSharedPreferences(mssv, MODE_PRIVATE);
            SharedPreferences.Editor editorLogin = sharedPreferences2.edit();
            editorLogin.putBoolean("SaveLogin", chkSaveLogin.isChecked());

            editorLogin.apply();
        //Kiểm tra Internet trc khi cho login
        if (MSSV.isEmpty()) {
            Toast.makeText(LoginActivity.this, "MSSV trống", Toast.LENGTH_SHORT).show();
        } else if (MK.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Mật khẩu trống", Toast.LENGTH_SHORT).show();
        } else {
            FireBaseHelper.reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Kiểm tra nếu MSSV/MK tồn tại
                    if (snapshot.hasChild(MSSV)) {
                        final String getMK = snapshot.child(MSSV).child("matkhau").getValue(String.class);
                         if (getMK.equals(MK)) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, Menu.class);
                            intent.putExtra("Data", bundle);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Mật khẩu sai!", Toast.LENGTH_SHORT).show();
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
            private void showtext() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hướng dẫn").setIcon(R.drawable.question);
        builder.setMessage("Cách 1: Bấm nút 'Quét mã vạch' và sử dụng thẻ sinh viên của bạn " +
                "\n\nCách 2: bạn có thể sử dụng MSSV và mật khẩu để đăng nhập");
        builder.setPositiveButton("OK",null);
        AlertDialog dialog = builder.create() ;
        dialog.show();
    }

    public void scanner(View view) {
        if (isConnected()) {
            Intent intent = new Intent(LoginActivity.this, ScanHelper.class);
            startActivity(intent);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
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