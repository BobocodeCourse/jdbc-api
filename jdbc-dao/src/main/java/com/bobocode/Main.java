package com.bobocode;

import com.bobocode.dao.AccountDao;
import com.bobocode.dao.AccountDaoImpl;
import com.bobocode.model.Account;
import com.bobocode.util.DbUtil;
import com.bobocode.util.TestDataProvider;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AccountDao accountDao = new AccountDaoImpl(DbUtil.getDataSource());

        TestDataProvider.generateAccountList(10)
                .forEach(accountDao::save);

        List<Account> accounts = accountDao.findAll();
        accounts.forEach(System.out::println);

        //Show last account
        Account tempAccount = accountDao.find((long) accounts.size());
        System.out.println("\nFind last entity: " + tempAccount);

        //Update and show last account
        tempAccount.setFirstName("Kyiv");
        accountDao.update(tempAccount);
        tempAccount = accountDao.find(tempAccount.getId());
        System.out.println("\nFind update entity: " + tempAccount);
    }
}
