package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

//import android.database.sqlite.SQLiteDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;


/**
 * Created by Rashindrie on 11/20/2016.
 */

public class PersistentExpenseManager extends ExpenseManager{
    private Context context;
    PersistentExpenseManager(Context context){
        this.context=context;
        
    }

    @Override
    public void setup() throws ExpenseManagerException {

        //create a DB using index number
        SQLiteDatabase mydatabase = context.openOrCreateDatabase("140449V",Context.MODE_PRIVATE ,null);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(Account_no VARCHAR PRIMARY KEY,Bank_name VARCHAR,account_holder_name VARCHAR,Balance REAL);");
        //mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Transaction()");


    }
}
