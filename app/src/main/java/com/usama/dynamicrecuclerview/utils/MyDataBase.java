package com.usama.dynamicrecuclerview.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MyDataBase extends SQLiteOpenHelper {

    private static final int VERSION_DB = 1;
    public static final String PRODUCTS_TABLE = "PRODUCTS";
    public static final String NAME_DB = "DB";

    private static MyDataBase instance;

    private MyDataBase(Context context) {
        super(context, NAME_DB, null, VERSION_DB);
    }

    public static MyDataBase getInstance(Context context) {
        if (instance == null) instance = new MyDataBase(context);
        return instance;
    }

    public void createTable() {
        SQLiteDatabase db = getWritableDatabase();
        String SQLCreateTabele = "CREATE TABLE IF NOT EXISTS " + PRODUCTS_TABLE + "(PRODUCT TEXT,NAME TEXT)";
        db.execSQL(SQLCreateTabele);
    }

    public ArrayList<Product> getProducts() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Product> lArray = new ArrayList<>();
        String query = "SELECT * FROM " + MyDataBase.PRODUCTS_TABLE;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                Product mProduct = new Gson().fromJson(c.getString(0), Product.class);
                lArray.add(mProduct);
            } while (c.moveToNext());
        }
        c.close();
        return lArray;
    }

    public void addProduct(Product mProduct) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PRODUCT", new Gson().toJson(mProduct));
        cv.put("NAME", mProduct.getName());

        long id = db.insert(PRODUCTS_TABLE, null, cv);
        if (id == -1)
            throw new SQLException("Error inserting" + mProduct.getName() + "in the database");
    }

    public void updateProduct(Product mProduct) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PRODUCT", new Gson().toJson(mProduct));
        cv.put("NAME", mProduct.getName());

        long id = db.update(PRODUCTS_TABLE, cv, "NAME = '" + mProduct.getName() + "'", null);
        if (id <= 0)
            throw new SQLException("Error updating " + mProduct.getName() + " in the database");
    }

    public void removeProduct(Product mProduct) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PRODUCTS_TABLE, "Name = '" + mProduct.getName() + "'", null);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
