package com.example.giuaki;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.example.giuaki.Adapters.BillAdapter;
import com.example.giuaki.Database.Database;
import com.example.giuaki.Interfaces.CallBackRemoveBill;
import com.example.giuaki.Models.Bill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillActivity extends AppCompatActivity implements CallBackRemoveBill {
    List<Bill> bills;
    Database database;
    Dialog dialogDel;
    BillAdapter billAdapter;
    Dialog dialogCalander;
    @BindView(R.id.rv_car)
    RecyclerView rvCar;
    @BindView(R.id.cv_Add)
    CardView cvAdd;
    @BindView(R.id.iv_trash)
    ImageView ivTrash;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        database = new Database(this, "QLXE.sqlite", null, 1);
        bills = new ArrayList<>();
        rvCar.setLayoutManager(new GridLayoutManager(this, 3));
        getListBill();
        billAdapter = new BillAdapter(this, bills);
        rvCar.setAdapter(billAdapter);


    }

    public void getListBill() {
        Cursor cursor = database.GetData("SELECT * FROM DONDATHANG ORDER BY NgayLap DESC ");
        bills.clear();
        while (cursor.moveToNext()) {
            bills.add(new Bill(cursor.getString(0), cursor.getString(1)));
        }
        Log.d("AAA", bills.size() + "");

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.cv_Add)
    public void onViewClicked() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                database.QueryData("INSERT INTO DONDATHANG VALUES(null,'" + simpleDateFormat.format(calendar.getTime()) + "')");
                getListBill();
                billAdapter.notifyDataSetChanged();


            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case 5:
                break;
            case 6:
                dialogDel = new Dialog(Objects.requireNonNull(BillActivity.this));
                dialogDel.setContentView(R.layout.dialog_del);
                Objects.requireNonNull(dialogDel.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button button = dialogDel.findViewById(R.id.bt_add);
                Button button1 = dialogDel.findViewById(R.id.bt_cc);
                dialogDel.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.QueryData("DELETE FROM CTDONDATHANG WHERE MADDH='" + bills.get(item.getGroupId()).getMADDH() + "'");
                        database.QueryData("DELETE FROM DONDATHANG WHERE MADDH='" + bills.get(item.getGroupId()).getMADDH() + "'");
                        getListBill();
                        billAdapter.notifyDataSetChanged();
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

    @Override
    public void RemoveBills(int count) {
        Log.d("AAA", count + "");
        if (count != 0) {
            ivTrash.setVisibility(View.VISIBLE);
            ivCancel.setVisibility(View.VISIBLE);
        } else {
            ivTrash.setVisibility(View.INVISIBLE);
            ivCancel.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.iv_trash, R.id.iv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_trash:
                for (int i = 0; i < bills.size(); i++) {
                    if (bills.get(i).isCheckRemove()) {
                        database.QueryData("DELETE FROM CTDONDATHANG WHERE MADDH='" + bills.get(i).getMADDH() + "'");
                        database.QueryData("DELETE FROM DONDATHANG WHERE MADDH='" + bills.get(i).getMADDH() + "'");
                    }
                }
                getListBill();
                billAdapter.notifyDataSetChanged();
                billAdapter.count = 0;
                ivCancel.setVisibility(View.INVISIBLE);
                ivTrash.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_cancel:
                for (int i = 0; i < bills.size(); i++) {
                    bills.get(i).setCheckOn(false);
                    bills.get(i).setCheckRemove(false);
                }
                billAdapter.count = 0;
                billAdapter.notifyDataSetChanged();
                ivCancel.setVisibility(View.INVISIBLE);
                ivTrash.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
