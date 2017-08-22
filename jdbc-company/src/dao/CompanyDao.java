package dao;

import core.Company;

import java.util.List;

/**
 * Created by hamster on 21.08.2017.
 */
public interface CompanyDao {
    void save (Company company);
    List<Company> findAll();

    Company findByName(String name);

    void createCompanyTable();
}
