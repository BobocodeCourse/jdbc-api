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

    //A lot of duplicated code. I'm going to refactor this class in the near feature)

    @Override
    public Long save(Company company) {
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement insertStatement = prepareInsertStatement(connection, company);

            executeInsert(insertStatement);

            Long id = fetchGeneratedId(insertStatement);
            company.setId(id);
            return id;
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    @Override
    public List<Company> findAll() {
        try(Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM company");
            return collectToList(rs);

        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    @Override
    public List<Company> findByName(String name) {
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM company WHERE name = ?");
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            return collectToList(rs);

        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    private PreparedStatement prepareInsertStatement(Connection connection, Company company) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO" +
                " company(name,phone) VALUES(?,?);"
            , PreparedStatement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, company.getName());
        preparedStatement.setString(2, company.getPhone());

        return preparedStatement;
    }

    private void executeInsert(PreparedStatement preparedStatement) throws SQLException {
        int rowsAffected = preparedStatement.executeUpdate();

        if(rowsAffected == 0) {
            throw new SQLException("Company wasn't created");
        }
    }

    private Long fetchGeneratedId(PreparedStatement preparedStatement) throws SQLException {
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

        if(generatedKeys.next()) {
            return generatedKeys.getLong(1);
        } else {
            throw new SQLException("Can not obtain company ID");
        }
    }

    private Company parseRow(ResultSet rs) throws SQLException {
        Company company = new Company();

        company.setId(rs.getLong(1));
        company.setName(rs.getString(2));
        company.setPhone(rs.getString(3));

        return company;
    }

    private List<Company> collectToList(ResultSet rs) throws SQLException {
        List<Company> companyList = new ArrayList<>();
        while (rs.next()) {
            Company company = parseRow(rs);
            companyList.add(company);
        }
        return companyList;
    }
}
