package com.bobocode.dao;

import com.bobocode.exception.DaoOperationException;
import com.bobocode.model.Company;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {
    private DataSource dataSource;

    public CompanyDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Company company) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement insertStatement = createStatement(connection, company);
            executeInsert(insertStatement);
            Long id = fetchGeneratedId(insertStatement);
            company.setId(id);
            System.out.println("Company was created! Id: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Long fetchGeneratedId(PreparedStatement insertStatement) throws SQLException {
        ResultSet generatedKeys = insertStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        } else {
            throw new SQLException("Can not obtain an account ID");
        }
    }

    private void executeInsert(PreparedStatement insertStatement) throws SQLException {
        int i = insertStatement.executeUpdate();
        if (i == 0) {
            throw new DaoOperationException("Company was not created");
        }
    }

    private PreparedStatement createStatement(Connection connection, Company company) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO company(name, phone) VALUES(?,?);",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        preparedStatement.setString(1, company.getName());
        preparedStatement.setString(2, company.getPhone());
        return preparedStatement;
    }

    @Override
    public List<Company> findAll() {
        try (Connection connection = dataSource.getConnection()) {
            return collectToList(connection
                    .createStatement()
                    .executeQuery("SELECT * FROM company;"));
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    // TODO: 21.08.17 create this method for all model
    private List<Company> collectToList(ResultSet resultSet) throws SQLException {
        List<Company> companies = new ArrayList<>();
        while (resultSet.next()) {
            Company company = parseRow(resultSet);
            companies.add(company);
        }
        return companies;
    }

    private Company parseRow(ResultSet resultSet) throws SQLException {
        return new Company() {{
            setId(resultSet.getLong(1));
            setName(resultSet.getString(2));
            setPhone(resultSet.getString(3));
        }};
    }

    @Override
    public List<Company> findByName(String name) {
        try (Connection connection = dataSource.getConnection()) {
            return collectToList(
                    prepareFindByNameStatement(connection, name)
                            .executeQuery());
        } catch (SQLException e) {
            throw new DaoOperationException("Didn't found company.", e);
        }
    }

    private PreparedStatement prepareFindByNameStatement(Connection connection, String name) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM company WHERE name=?;");
        ps.setString(1, name);
        return ps;
    }
}
