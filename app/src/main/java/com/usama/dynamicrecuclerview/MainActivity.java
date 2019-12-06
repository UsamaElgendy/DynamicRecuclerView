package com.usama.dynamicrecuclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.usama.dynamicrecuclerview.dynamicrecyclerview.DynamicEventHandler;
import com.usama.dynamicrecuclerview.dynamicrecyclerview.MyAdapter;
import com.usama.dynamicrecuclerview.utils.MyDataBase;
import com.usama.dynamicrecuclerview.utils.Product;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createObjects();
        intialRecyclerView();
    }

    private void createObjects() {

        // created the table in the bank if there is no
        MyDataBase.getInstance(this).createTable();

        // Check if items have already been added, if not add them
        if (MyDataBase.getInstance(this).getProducts().size() > 0) return;


        //I create the objects
        for (int i = 1; i <= 20; i++) {
            Product mProduct = new Product();
            mProduct.setName("Product #" + i);
            //                  A random number ....
            mProduct.setValue(toReal(i * 3.48 + (i * 2) - i + (i * i)));
            mProduct.setPosition(i);
            MyDataBase.getInstance(this).addProduct(mProduct);
        }

    }

    private void intialRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DynamicEventHandler.DynamicEventCallBack callBack = new DynamicEventHandler.DynamicEventCallBack() {
            @Override
            public void onItemMove(int initialPosition, int finalPosition) {
                mAdapter.onItemMove(initialPosition, finalPosition);
            }

            @Override
            public void removeItem(int position) {
                mAdapter.removeItem(position);
            }
        };

        ItemTouchHelper androidItemTouchHelper = new ItemTouchHelper(new DynamicEventHandler(callBack));
        androidItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mAdapter = new MyAdapter(this, androidItemTouchHelper);
        mRecyclerView.setAdapter(mAdapter);

    }


    public static String toReal(double valor) {
        try {
            String returnValue;
            Currency usd = Currency.getInstance(Locale.getDefault());
            NumberFormat format = java.text.NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(usd);
            returnValue = format.format(valor);
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }


}
