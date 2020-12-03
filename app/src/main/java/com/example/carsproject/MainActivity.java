package com.example.carsproject;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    protected EditText editBrand, editPrice, editYear;
    protected Button  btnAdd;
    protected ListView carOffers;

    protected void FillListView()
    throws Exception
    {
        final ArrayList<String> listResults = new ArrayList<>();
        SelectSQL(
                "SELECT * FROM CARS ORDER BY Brand;",
                null,
                new OnSelectElement() {
                    @Override
                    public void OnElementIterate(String Brand, String Price, String Year, String ID) {
                    listResults.add(ID+"\t"+Brand+"\t"+Price+"\t"+Year+"\n");
                    }
                }
        );
        carOffers.clearChoices();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_list_view,
                R.id.textView,
                listResults
        );
        carOffers.setAdapter(arrayAdapter);
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        try {
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editBrand=findViewById(R.id.carBrand);
        editPrice=findViewById(R.id.carPrice);
        editYear=findViewById(R.id.carYear);
        btnAdd=findViewById(R.id.btnAdd);
        carOffers=findViewById(R.id.carOffers);

        carOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView clickedText = view.findViewById(R.id.textView);
                String selectedText = clickedText.getText().toString();
                String[] elements = selectedText.split("\t");
                String ID = elements[0];
                String Brand = elements[1];
                String Price = elements[2];
                String Year = elements[3];

                Intent intent = new Intent(MainActivity.this,UpdateDeleteActivity.class);
                Bundle b = new Bundle();
                b.putString("ID", ID);
                b.putString("Brand", Brand);
                b.putString("Price", Price);
                b.putString("Year", Year);
                intent.putExtras(b);

                startActivityForResult(intent,200,b);

            }
        });

        try{
        initDb();
        FillListView();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    ExecSQL("INSERT INTO CARS (BRAND, PRICE, YEAR)" +
                                    "VALUES (?, ?, ?)",
                            new Object[]{
                                    editBrand.getText().toString(),
                                    editPrice.getText().toString(),
                                    editYear.getText().toString()
                            },
                            new OnQuerySuccess() {
                                @Override
                                public void OnSuccess() {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Offer added!",
                                            Toast.LENGTH_LONG).show();
                                    try {
                                        FillListView();
                                    } catch (Exception e) {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            }
                    );
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}