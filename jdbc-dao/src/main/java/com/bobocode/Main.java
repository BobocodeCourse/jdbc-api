package com.bobocode;

import com.bobocode.dao.AccountDao;
import com.bobocode.dao.AccountDaoImpl;
import com.bobocode.dao.CompanyDao;
import com.bobocode.dao.CompanyDaoImpl;
import com.bobocode.model.Account;
import com.bobocode.model.Company;
import com.bobocode.util.DbUtil;
import com.bobocode.util.TestDataProvider;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.company.CompanyProperties;

public class Main {
    public static void main(String[] args) {
        AccountDao accountDao = new AccountDaoImpl(DbUtil.getDataSource());
        CompanyDao companyDao = new CompanyDaoImpl(DbUtil.getDataSource());

        //Add new fake accounts
        for(int i = 0; i < 4; i ++) {
            Long newAccountId = accountDao.save(TestDataProvider.generateFakeAccount());
            System.out.println("New " + accountDao.find(newAccountId).get());
            System.out.println("--------------------------------");
        }

        //Update existing account
        Long userIdForUpdate = 1L;
        Account accountForUpdate = accountDao.find(userIdForUpdate).get();
        accountForUpdate.setFirstName("NewName");
        accountDao.update(accountForUpdate);
        System.out.println("Updated " + accountDao.find(userIdForUpdate).get());
        System.out.println("--------------------------------");

        //Find specific account by user id
        System.out.println("Found by Id " + accountDao.find(50L).isPresent());
        System.out.println("--------------------------------");

        //Find all accounts
        accountDao.findAll().stream()
                .forEach(System.out::println);
        System.out.println("--------------------------------");

        //Add new fake companies and find them by name
        for (int i = 0; i < 3; i++) {
            Company newCompany = generateFakeCompany();
            companyDao.save(newCompany);
            System.out.println("New " + companyDao.findByName(newCompany.getName()));
            System.out.println("--------------------------------");
        }

        //Find all companies
        companyDao.findAll().stream()
                .forEach(System.out::println);
        System.out.println("--------------------------------");
    }

    public static Company generateFakeCompany() {
        Fairy fairy = Fairy.create();
        io.codearte.jfairy.producer.company.Company company = fairy.company(new CompanyProperties.CompanyProperty[0]);
        Company fakeCompany = new Company();
        fakeCompany.setName(company.getName());
        fakeCompany.setPhone(company.getVatIdentificationNumber());
        return fakeCompany;
    }
}
