import core.Company;
import dao.CompanyDao;
import dao.CompanyDaoImpl;
import com.bobocode.util.DbUtil;

import java.util.List;

/**
 * Created by hamster on 21.08.2017.
 */
public class Main {
    public static void main(String[] args) {
        CompanyDao companyDao = new CompanyDaoImpl(DbUtil.getDataSource());

        companyDao.createCompanyTable();

        saveTestCompanyToDb(companyDao);


        List<Company> allCompany = companyDao.findAll();
        printListCompany(allCompany);

        System.out.println(companyDao.findByName("Lambda ink"));

    }

    public static void saveTestCompanyToDb (CompanyDao companyDao){
        companyDao.save(new Company(2,"NeoCOmp", "0683547811"));
        companyDao.save(new Company(5,"Leika", "0503478988"));
        companyDao.save(new Company(6,"Garbage and Ko", "0639874514"));
        companyDao.save(new Company(7,"Lambda ink", "0664561425"));
        companyDao.save(new Company(22,"NoutbookCharger", "0500032145"));
        companyDao.save(new Company(41,"Running man", "0503697845"));
        companyDao.save(new Company(3,"Sun oil", "0502143654"));
    }

    public static void printListCompany(List<Company> list){
        for (Company company:list) {
            System.out.println(company);
        }
    }
}
