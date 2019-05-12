package com.example.giuaki;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.giuaki.Adapters.BillDetailAdapter;
import com.example.giuaki.Adapters.CarAdapter;
import com.example.giuaki.Database.Database;
import com.example.giuaki.Models.BillDetail;
import com.example.giuaki.Models.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillDetailActivity extends AppCompatActivity {
    Dialog dialogAddDetail;
    List<String> listMaxe;
    List<BillDetail> billDetails;
    Database database;
    String MADDH;
    BillDetailAdapter billDetailAdapter;
    Dialog dialogDel;
    Dialog dialogEditDetail;

    @BindView(R.id.iv_trash)
    ImageView ivTrash;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.rv_car)
    RecyclerView rvCar;
    @BindView(R.id.cv_Add)
    CardView cvAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        ButterKnife.bind(this);
        init();


    }

    private void init() {

        MADDH = getIntent().getStringExtra("IDBill");
        database = new Database(this, "QLXE.sqlite", null, 1);
        listMaxe = new ArrayList<>();
        billDetails = new ArrayList<>();
        getListBillDetail(MADDH);
        rvCar.setLayoutManager(new GridLayoutManager(this, 1));
        billDetailAdapter = new BillDetailAdapter(billDetails, this);
        rvCar.setAdapter(billDetailAdapter);


    }

    private void getListMaxe(String maddh) {
        Cursor cursor = database.GetData("select MaXe FROM Xe where MaXe not in (select Maxe from CTDONDATHANG where MADDH = '" + maddh + "')");
        listMaxe.clear();
        while (cursor.moveToNext()) {
            listMaxe.add(cursor.getString(0));
        }
    }

    private void getListBillDetail(String maddh) {
        Cursor cursor = database.GetData("SELECT Xe.MaXe,Xe.TenXe,CTDONDATHANG.SoLuong FROM Xe,CTDONDATHANG WHERE CTDONDATHANG.MADDH='" + maddh + "' AND CTDONDATHANG.MaXe=Xe.MaXe");
        billDetails.clear();
        while (cursor.moveToNext()) {
            billDetails.add(new BillDetail(maddh, cursor.getString(0), cursor.getString(2), cursor.getString(1)));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.cv_Add)
    public void onViewClicked() {
        dialogAddDetail = new Dialog(Objects.requireNonNull(BillDetailActivity.this));
        dialogAddDetail.setContentView(R.layout.dialog_add_detail);
        Objects.requireNonNull(dialogAddDetail.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button button = dialogAddDetail.findViewById(R.id.bt_add);
        Button button1 = dialogAddDetail.findViewById(R.id.bt_cc);
        final EditText editText = dialogAddDetail.findViewById(R.id.edt_Dt);
        final Spinner spinner = dialogAddDetail.findViewById(R.id.spn_Maxe);
        getListMaxe(MADDH);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(BillDetailActivity.this, android.R.layout.simple_spinner_item, listMaxe);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        dialogAddDetail.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.QueryData("INSERT INTO CTDONDATHANG VALUES('" + MADDH + "','" + spinner.getSelectedItem().toString() + "','" + editText.getText().toString() + "')");
                dialogAddDetail.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddDetail.dismiss();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case 7:
                dialogEditDetail = new Dialog(Objects.requireNonNull(BillDetailActivity.this));
                dialogEditDetail.setContentView(R.layout.dialog_edit_bill_detail);
                Objects.requireNonNull(dialogEditDetail.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button button2 = dialogEditDetail.findViewById(R.id.bt_add);
                Button button3 = dialogEditDetail.findViewById(R.id.bt_cc);
                final EditText edName = dialogEditDetail.findViewById(R.id.edt_name);
                edName.setText(billDetails.get(item.getGroupId()).getSoLuong());

                dialogEditDetail.show();
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.QueryData("UPDATE CTDONDATHANG SET SoLuong = '" + edName.getText().toString() + "' WHERE MADDH=' "+ MADDH +" ' AND MaXe='"+billDetails.get(item.getGroupId()).getMaXe()+"'");
                        getListBillDetail(MADDH);
                        billDetailAdapter.notifyDataSetChanged();
                        dialogEditDetail.dismiss();


                    }
                });
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogEditDetail.dismiss();
                    }
                });


                break;
            case 8:
                dialogDel = new Dialog(Objects.requireNonNull(BillDetailActivity.this));
                dialogDel.setContentView(R.layout.dialog_del);
                Objects.requireNonNull(dialogDel.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button button = dialogDel.findViewById(R.id.bt_add);
                Button button1 = dialogDel.findViewById(R.id.bt_cc);
                dialogDel.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.QueryData("DELETE FROM CTDONDATHANG WHERE MADDH='" + MADDH + "' AND MaXe='" + billDetails.get(item.getGroupId()).getMaXe() + "'");
                        getListBillDetail(MADDH);
                        billDetailAdapter.notifyDataSetChanged();
                        dialogDel.dismiss();


                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDel.dismiss();
                    }
                });


                break;

        }
        return super.onContextItemSelected(item);
    }
}
