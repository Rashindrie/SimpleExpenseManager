package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

//import android.database.sqlite.SQLiteDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;


/**
 * Created by Rashindrie on 11/20/2016.
 */

public class PersistentExpenseManager extends ExpenseManager{
    private Context context;

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.context=context;
        try {
            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setup() throws ExpenseManagerException {

        //create a DB using index number
        SQLiteDatabase mydatabase = context.openOrCreateDatabase("140449V",Context.MODE_PRIVATE ,null);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(Account_no VARCHAR PRIMARY KEY,Bank_name VARCHAR,Account_holder_name VARCHAR,Balance REAL);");

        //could not name this table as 'Transaction" since 'Transaction' is a key word
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS TransactionLedger(Transaction_id INTEGER PRIMARY KEY,Account_no VARCHAR,Expense_type INT,Amount REAL,Date DATE,FOREIGN KEY (Account_no) REFERENCES Account(Account_no));");

        PersistentAccountDAO accountDAO = new PersistentAccountDAO(mydatabase);
        setAccountsDAO(accountDAO);

        PersistentTransactionDAO transactionDAO = new PersistentTransactionDAO(mydatabase);
        setTransactionsDAO(transactionDAO);



    }
}
