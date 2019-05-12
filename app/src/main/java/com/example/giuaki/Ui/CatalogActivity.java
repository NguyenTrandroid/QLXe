package com.example.giuaki;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giuaki.Adapters.ForecastAdapter;
import com.example.giuaki.Database.Database;
import com.example.giuaki.Models.Catalog;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CatalogActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener<ForecastAdapter.ViewHolder>,
        DiscreteScrollView.ScrollStateChangeListener<ForecastAdapter.ViewHolder> {
    Database database;
    Dialog dialogDel;
    Dialog dialogEdit;
    ForecastAdapter forecastAdapter;
    Dialog dialogAddCatalog;
    boolean checkIDCatalog = false;

    List<Catalog> catalogs;
    @BindView(R.id.iv_Catalog)
    ImageView ivCatalog;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.forecast_view)
    LinearLayout forecastView;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.forecast_city_picker)
    DiscreteScrollView forecastCityPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        ButterKnife.bind(this);
        init();
        getListCatalog();
    }

    private void init() {
        database = new Database(this, "QLXE.sqlite", null, 1);
        catalogs = new ArrayList<>();
        forecastAdapter = new ForecastAdapter(catalogs, CatalogActivity.this);
        forecastCityPicker.setSlideOnFling(true);
        forecastCityPicker.setAdapter(forecastAdapter);

        forecastCityPicker.addOnItemChangedListener(this);
        forecastCityPicker.addScrollStateChangeListener(this);

        forecastCityPicker.scrollToPosition(2);
        forecastCityPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        forecastCityPicker.setItemTransitionTimeMillis(150);

    }

    public void getListCatalog() {

        Cursor cursor = database.GetData("SELECT * FROM LOAIXE");
        catalogs.clear();
        while (cursor.moveToNext()) {
            String maloai = cursor.getString(0);
            String tenloai = cursor.getString(1);
            String xuatxu = cursor.getString(2);
            if (maloai.equals("XM")) {
                catalogs.add(new Catalog(maloai, tenloai, xuatxu, R.drawable.ic_scooter, R.drawable.xemay_on));
            } else if (maloai.equals("XOT")) {
                catalogs.add(new Catalog(maloai, tenloai, xuatxu, R.drawable.ic_car_black, R.drawable.car_on));
            } else if (maloai.equals("XMT")) {
                catalogs.add(new Catalog(maloai, tenloai, xuatxu, R.drawable.ic_motorbike, R.drawable.moto_on));
            } else if (maloai.equals("XD")) {
                catalogs.add(new Catalog(maloai, tenloai, xuatxu, R.drawable.ic_bycicle, R.drawable.bike_on));
            } else {
                catalogs.add(new Catalog(maloai, tenloai, xuatxu, R.drawable.ic_other, R.drawable.ic_other_whitl));

            }
        }
        forecastAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCurrentItemChanged(@Nullable ForecastAdapter.ViewHolder viewHolder, int adapterPosition) {
        Log.d("AAA", adapterPosition + "");
        if(catalogs.size()!=0){
            ivCatalog.setImageResource(catalogs.get(adapterPosition).getImgOn());
            tvName.setText("Loại xe: " + catalogs.get(adapterPosition).getName());
            tvCountry.setText("Xuất xứ: " + catalogs.get(adapterPosition).getCountry());
        }

    }

    @Override
    public void onScrollStart(@NonNull ForecastAdapter.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScrollEnd(@NonNull ForecastAdapter.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable ForecastAdapter.ViewHolder currentHolder, @Nullable ForecastAdapter.ViewHolder newCurrent) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.tv_add)
    public void onViewClicked() {
        dialogAddCatalog = new Dialog(Objects.requireNonNull(CatalogActivity.this));
        dialogAddCatalog.setContentView(R.layout.dialog_add_catalog);
        Objects.requireNonNull(dialogAddCatalog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button button2 = dialogAddCatalog.findViewById(R.id.bt_add);
        Button button3 = dialogAddCatalog.findViewById(R.id.bt_cc);
        final EditText edName = dialogAddCatalog.findViewById(R.id.edt_name);
        final EditText edDt = dialogAddCatalog.findViewById(R.id.edt_Dt);
        final EditText edPrice = dialogAddCatalog.findViewById(R.id.edt_pirce);
        dialogAddCatalog.show();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < catalogs.size(); i++) {
                    if (edName.getText().toString().trim().equals(catalogs.get(i).getId())) {
                        checkIDCatalog = true;
                        break;
                    }
                }
                if (checkIDCatalog) {
                    Toast.makeText(CatalogActivity.this, "Mã loại bị trùng!", Toast.LENGTH_SHORT).show();
                    dialogAddCatalog.dismiss();
                } else {
                    database.QueryData("INSERT INTO LOAIXE VALUES('" + edName.getText().toString() + "','" + edDt.getText().toString() + "','" + edPrice.getText().toString() + "')");
                    getListCatalog();
                    dialogAddCatalog.dismiss();

                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case 3:
                dialogEdit = new Dialog(Objects.requireNonNull(CatalogActivity.this));
                dialogEdit.setContentView(R.layout.dialog_edit_catalog);
                Objects.requireNonNull(dialogEdit.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button button2 = dialogEdit.findViewById(R.id.bt_add);
                Button button3 = dialogEdit.findViewById(R.id.bt_cc);
                final EditText edName = dialogEdit.findViewById(R.id.edt_name);
                final EditText edDt = dialogEdit.findViewById(R.id.edt_Dt);
                edName.setText(catalogs.get(item.getGroupId()).getName());
                edDt.setText(catalogs.get(item.getGroupId()).getCountry());
                dialogEdit.show();
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.QueryData("UPDATE LOAIXE SET TenLoai = '" + edName.getText().toString() + "',XuatXu='" + edDt.getText().toString() + "' WHERE MaLoai = '" + catalogs.get(item.getGroupId()).getId() + "'");
                        getListCatalog();
                        dialogEdit.dismiss();


                    }
                });
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogEdit.dismiss();
                    }
                });

                break;
            case 4:
                dialogDel = new Dialog(Objects.requireNonNull(CatalogActivity.this));
                dialogDel.setContentView(R.layout.dialog_del);
                Objects.requireNonNull(dialogDel.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button button = dialogDel.findViewById(R.id.bt_add);
                Button button1 = dialogDel.findViewById(R.id.bt_cc);
                dialogDel.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.QueryData("DELETE FROM LOAIXE WHERE MaLoai='" + catalogs.get(item.getGroupId()).getId() + "'");
                        getListCatalog();
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
