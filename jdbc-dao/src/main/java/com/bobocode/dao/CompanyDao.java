package com.bobocode.dao;

import com.bobocode.model.Company;

import java.util.List;

public interface CompanyDao {
    void save(Company company);

    List<Company> findAll();

    List<Company> findByName(String name);

}
