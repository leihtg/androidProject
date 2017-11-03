package dianshi.matchtrader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.dianshi.matchtrader.server.GlobalSingleton;

import java.util.ArrayList;

import dianshi.matchtrader.constant.APPFinal;


/**
 * sqlite工具类
 */
public class SQLiteHelper extends SQLiteOpenHelper {


    GlobalSingleton globalSingleton = GlobalSingleton.CreateInstance();
    int a;

    public SQLiteHelper(Context context, String name, CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建数据库
     *
     * @param context
     */
    public SQLiteHelper(Context context) {


        this(context, APPFinal.DB_NAME, null, APPFinal.DB_VERSION);


    }

    /**
     * 创建表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        //根据不同的用户ID建不同的表
        String tableName = APPFinal.TABLE_NAME + globalSingleton.CustomerId;

        String sql = " create table " + tableName + "("
                + APPFinal.C_ID + " nvarchar(50))";


        db.execSQL(sql);


    }


    /**
     * 在表中添加数据
     *
     * @param db
     */
    public void insert(SQLiteDatabase db, int c_id) {
        ContentValues values = new ContentValues();

        values.put(APPFinal.C_ID, c_id);
        String tableName = APPFinal.TABLE_NAME + globalSingleton.CustomerId;
        db.insert(tableName, null, values);


    }

    /**
     * 从表中删除数据
     *
     * @param db
     */
    public void delete(SQLiteDatabase db, int c_id) {
        String tableName = APPFinal.TABLE_NAME + globalSingleton.CustomerId;
        db.delete(tableName, "c_id = ?", new String[]{c_id + ""});


    }

    /**
     * 在表中查询所有数据
     *
     * @param db
     */

    public ArrayList<Integer> query(SQLiteDatabase db) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        /*
         query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)方法各参数的含义：
		 table：表名。相当于select语句from关键字后面的部分。如果是多表联合查询，可以用逗号将两个表名分开。
		 columns：要查询出来的列名。相当于select语句select关键字后面的部分。
		 selection：查询条件子句，相当于select语句where关键字后面的部分，在条件子句允许使用占位符“?”
		 selectionArgs：对应于selection语句中占位符的值，值在数组中的位置与占位符在语句中的位置必须一致，否则就会有异常。
		 groupBy：相当于select语句group by关键字后面的部分
		 having：相当于select语句having关键字后面的部分
		 orderBy：相当于select语句order by关键字后面的部分，如：personid desc, age asc;
		 limit：指定偏移量和获取的记录数，相当于select语句limit关键字后面的部分。
		 */


        String tableName = APPFinal.TABLE_NAME + globalSingleton.CustomerId;


        Cursor cursor = db.query(tableName, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            array.add(cursor.getInt(cursor.getColumnIndex(APPFinal.C_ID)));
        }
        return array;

    }


    /**
     * 查询某条数据是否存在
     *
     * @param db
     * @param product_id
     * @return
     */
    public boolean queryById(SQLiteDatabase db, int product_id) {
        String tableName = APPFinal.TABLE_NAME + globalSingleton.CustomerId;


        Cursor cursor = db.query(tableName, null, "c_id=?", new String[]{product_id + ""}, null, null, null);

        return cursor.getCount() > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }


    /**
     * 返回数据库的名字
     *
     * @return
     */
    public String getTableName() {
        String tableName = APPFinal.TABLE_NAME + globalSingleton.CustomerId;
        return tableName;
    }

    /**
     * 判断表是否存在
     *
     * @return
     */
    public boolean isTableExist(SQLiteDatabase db) {
        boolean result = false;
//		if(tableName == null){
//			return false;
//		}
        String tableName = APPFinal.TABLE_NAME + globalSingleton.CustomerId;
        Cursor cursor = null;


        try {
            db = this.getReadableDatabase();
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' ";


            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        return result;
    }


}
