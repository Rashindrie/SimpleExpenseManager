package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Rashindrie on 11/20/2016.
 */

public class PersistentTransactionDAO implements TransactionDAO {
    private SQLiteDatabase db;

    public PersistentTransactionDAO(SQLiteDatabase sqLiteDB){
        this.db=sqLiteDB;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        String sqlquery = "INSERT INTO TransactionLedger (Account_no,Expense_type,Amount,t_date) VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sqlquery);

        statement.bindString(1,accountNo);
        statement.bindLong(2,(expenseType == ExpenseType.INCOME) ? 1 : 0);
        statement.bindDouble(3,amount);
        statement.bindLong(4,date.getTime());

        statement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor resultSet = db.rawQuery("SELECT * FROM TransactionLedger",null);
        List<Transaction> tr_list = new ArrayList<>();

        if(resultSet.moveToFirst()) {
            do{
                Transaction transaction = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("t_date"))),
                resultSet.getString(resultSet.getColumnIndex("Account_no")),
                (resultSet.getInt(resultSet.getColumnIndex("Expense_type")) == 1) ? ExpenseType.INCOME : ExpenseType.EXPENSE,
                 resultSet.getDouble(resultSet.getColumnIndex("Amount")));
                tr_list.add(transaction);
            }while (resultSet.moveToNext());
        }
        return tr_list;

    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor resultSet = db.rawQuery("SELECT * FROM Transactionledger LIMIT " + limit, null);
        List<Transaction> tr_list = new ArrayList<>();

        if(resultSet.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("t_date"))),
                resultSet.getString(resultSet.getColumnIndex("Account_no")),
                (resultSet.getInt(resultSet.getColumnIndex("Expense_type")) == 1) ? ExpenseType.INCOME : ExpenseType.EXPENSE,
                resultSet.getDouble(resultSet.getColumnIndex("Amount")));
                tr_list.add(transaction);
            } while (resultSet.moveToNext());
        }

        return tr_list;

    }
}
