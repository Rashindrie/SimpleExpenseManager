package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Rashindrie on 11/20/2016.
 */

public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase db;

    public PersistentAccountDAO(SQLiteDatabase sqLiteDB){
        this.db=sqLiteDB;
    }

    @Override
    public List<String> getAccountNumbersList() {

        // A cursor to the result set acquired to o loop through results
        Cursor resultSet=db.rawQuery("SELECT Account_no FROM Account",null);

        // Get the cursor to the beginning of the resultset
        resultSet.moveToFirst();

        //A list will be used to store the data extracted
        List<String> acc_list=new ArrayList<String>();

        //Extract account information by looping through the results and adding the needed data to the list
        if(resultSet.moveToFirst()) {
            while (resultSet.moveToNext()) {
                acc_list.add(resultSet.getString(resultSet.getColumnIndex("Accout_no")));
            }
        }
        return acc_list;
    }

    @Override
    public List<Account> getAccountsList() {

        Cursor resultSet = db.rawQuery("SELECT * FROM Account",null);
        List<Account> acc_list = new ArrayList<>();


        if(resultSet.moveToFirst()) {
            do{
                Account account = new Account(resultSet.getString(resultSet.getColumnIndex("Account_no")),
                resultSet.getString(resultSet.getColumnIndex("Bank_name")),
                resultSet.getString(resultSet.getColumnIndex("Account_holder_name")),
                resultSet.getDouble(resultSet.getColumnIndex("Balance")));
                acc_list.add(account);
            }while (resultSet.moveToNext());
        }


        return acc_list;
    }


    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = db.rawQuery("SELECT * FROM Account WHERE Account_no = " + accountNo,null);

        Account account = null;

        // Check whether cursor has a result. Can get only one result since Account_no is a primary key in Account
        if(resultSet.moveToFirst()) {
            account = new Account(resultSet.getString(resultSet.getColumnIndex("Account_no")),
            resultSet.getString(resultSet.getColumnIndex("Bank_name")),
            resultSet.getString(resultSet.getColumnIndex("Account_holder_name")),
            resultSet.getDouble(resultSet.getColumnIndex("Balance")));

        }

        return account;

    }

    @Override
    public void addAccount(Account account) {
        //Used prepared statements to insert an account into Account

        String sqlquery = "INSERT INTO Account (Account_no,Bank_name,Account_holder_name,Balance) VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sqlquery);

        //Bind values
        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());

        // Execute statement
        statement.executeInsert();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sqlquery = "DELETE FROM Account WHERE Account_no = ?";
        SQLiteStatement statement = db.compileStatement(sqlquery);

        statement.bindString(1,accountNo);

        statement.executeUpdateDelete();

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        String sqlquery = "UPDATE Account SET Balance = Balance + ?";
        SQLiteStatement statement = db.compileStatement(sqlquery);

        if(expenseType == ExpenseType.INCOME){
            statement.bindDouble(1,amount);
        }else{
            statement.bindDouble(1,-amount);
        }

        statement.executeUpdateDelete();

    }
}
