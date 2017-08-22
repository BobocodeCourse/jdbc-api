package dao;
import com.bobocode.exception.DaoOperationException;
import core.Company;

import java.sql.*;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamster on 21.08.2017.
 */
public class CompanyDaoImpl implements CompanyDao {
    private DataSource dataSource;

    public CompanyDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Company company) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement createCompanyString = prepareInsertStatement(connection, company);
            statement.executeUpdate(createCompanyString.toString()); //вот тут непонятно что собственно делает в коде Тараса executeInsert
            statement.close();
            connection.close();
        }catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
        System.out.println("Records created successfully");
    }

    private PreparedStatement prepareInsertStatement(Connection connection, Company company) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO company(id, name, phone)" +
                "VALUES (?,?,?)");
        preparedStatement.setInt(1, company.getId());
        preparedStatement.setString(2, company.getName());
        preparedStatement.setString(3, company.getPhone());
        return preparedStatement;
    }

    @Override
    public List<Company> findAll() {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM company");
            return collectToList(resultSet);
        }catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    private List<Company> collectToList(ResultSet resultSet) throws SQLException {
        List<Company> companyList = new ArrayList<>();
        while (resultSet.next()){
            Company company = parseRow(resultSet);
            companyList.add(company);
        }
        return companyList;
    }

    private Company parseRow(ResultSet resultSet) throws SQLException {
        Company company = new Company();
        company.setId(resultSet.getInt(1));
        company.setName(resultSet.getString(2));
        company.setPhone(resultSet.getString(3));
        return company;
    }

    @Override
    public Company findByName(String name) {
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM company WHERE name=?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return parseRow(resultSet);
        }catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    public  void createCompanyTable(){
        try {
            Connection connection = dataSource.getConnection();
            if (connection !=null) System.out.println("Connect to db successfully!");
            Statement statement = connection.createStatement();
            String createTable = "CREATE TABLE company(id SERIAL NOT NULL PRIMARY KEY, name varchar(225), phone varchar(10))";
            statement.executeUpdate(createTable);
            statement.close();
            connection.close();
        }catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
        System.out.println("Table created successfully");
    }
}
