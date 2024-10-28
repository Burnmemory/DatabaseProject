package com.example.lab6demo;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_SKU = "SKU";
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
                + " TEXT," + COLUMN_SKU + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();  // 获取可写数据库
        ContentValues values = new ContentValues();  // 创建一个键值对集合

        // 向ContentValues中插入产品名称和SKU
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_SKU, product.getSku());

        // 执行插入操作，将数据插入到表中
        db.insert(TABLE_PRODUCTS, null, values);

        // 关闭数据库连接
        db.close();
    }

    public Product findProduct(String productname) {
        SQLiteDatabase db = this.getReadableDatabase();  // 获取可读数据库
        String query = "SELECT * FROM " + TABLE_PRODUCTS +
                " WHERE " + COLUMN_PRODUCTNAME + " = \"" + productname + "\"";
        Cursor cursor = db.rawQuery(query, null);
        Product product = new Product();

        if (cursor.moveToFirst()) {  // 检查是否有结果
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setSku(Integer.parseInt(cursor.getString(2)));
            cursor.close();  // 关闭游标
        } else {
            product = null;  // 如果没有找到产品，返回 null
        }
        db.close();  // 关闭数据库连接
        return product;  // 返回产品对象
    }
    public boolean deleteProduct(String productname) {
        boolean result = false;  // 初始化结果为 false

        SQLiteDatabase db = this.getWritableDatabase();  // 获取可写数据库
        String query = "SELECT * FROM " + TABLE_PRODUCTS +
                " WHERE " + COLUMN_PRODUCTNAME + " = \"" + productname + "\"";

        Cursor cursor = db.rawQuery(query, null);  // 执行查询

        if (cursor.moveToFirst()) {  // 检查是否有结果
            String idStr = cursor.getString(0);  // 获取产品 ID
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?", new String[]{idStr});  // 删除产品
            cursor.close();  // 关闭游标
            result = true;  // 如果删除成功，设置结果为 true
        }

        db.close();  // 关闭数据库
        return result;  // 返回删除结果
    }
}
