package com.example.carsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateDeleteActivity extends BaseActivity {

    protected String ID;
    protected EditText editCarBrand, editCarPrice, editCarYear;
    protected Button btnUpdate, btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        editCarBrand=findViewById(R.id.editCarBrand);
        editCarPrice=findViewById(R.id.editCarPrice);
        editCarYear=findViewById(R.id.editCarYear);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            ID = b.getString("ID");
            editCarBrand.setText(b.getString("Brand"));
            editCarPrice.setText(b.getString("Price"));
            editCarYear.setText(b.getString("Year"));
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ExecSQL("DELETE FROM CARS WHERE ID = ?",
                            new Object[]{
                                    ID
                            },
                            new OnQuerySuccess() {
                                @Override
                                public void OnSuccess() {
                                    Toast.makeText(getApplicationContext(),
                                            "Offer successfully deleted!",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                    );

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                finishActivity(200);
                startActivity(new Intent(UpdateDeleteActivity.this, MainActivity.class));
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ExecSQL("UPDATE CARS SET Brand=?, Price=?, Year=? " +
                                    "WHERE ID=?",
                            new Object[]{
                                    editCarBrand.getText().toString(),
                                    editCarPrice.getText().toString(),
                                    editCarYear.getText().toString(),
                                    ID
                            },
                            new OnQuerySuccess() {
                                @Override
                                public void OnSuccess() {
                                    Toast.makeText(getApplicationContext(),
                                            "Offer updated!",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                    );

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                finishActivity(200);
                startActivity(new Intent(UpdateDeleteActivity.this, MainActivity.class));
            }
        });

    }
}