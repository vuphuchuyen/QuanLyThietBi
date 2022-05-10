package com.vph.qltb.SinhVien.ChucNang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;

public class SuaSDT extends AppCompatActivity {
    EditText sdt;
    Button  btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_sdt);
        Controls();
        xulyButton();
        autofill();
    }

    private void autofill() {
        String MSSV = Menu.login;
        FireBaseHelper.reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(MSSV)) {
                    final String getSDT = snapshot.child(MSSV).child("sdt").getValue(String.class);
                    sdt.setText(getSDT);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void xulyButton() {
        //Update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }
    private void update(){
        if(isConnected()){
            //Get all the values
            String MSSV = Menu.login;
            String Sdt = sdt.getText().toString();
            FireBaseHelper.reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (Sdt.isEmpty()) {
                        Toast.makeText(SuaSDT.this, "SĐT không được phép trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        FireBaseHelper.reference.child(MSSV).child("sdt").setValue(Sdt);
                        Toast.makeText(SuaSDT.this, "Chỉnh sửa thành công!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(SuaSDT.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private void Controls() {
        sdt = findViewById(R.id.editStudent_Phone);
        btnUpdate = findViewById(R.id.btnXacnhan);
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