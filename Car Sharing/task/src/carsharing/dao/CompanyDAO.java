package carsharing.dao;

import carsharing.db.Database;
import carsharing.abstracts.BaseDAO;
import carsharing.models.Company;
import carsharing.utils.StatementBinder;
import carsharing.utils.StatementMapper;

import java.util.List;

public class CompanyDAO extends BaseDAO<Company> {

    public CompanyDAO(Database companyDAO) {
        super(companyDAO);
    }

    public boolean createCompany(String name) {
        String insertQuery = "INSERT INTO COMPANY (NAME) VALUES (?)";

        Company company = new Company();
        company.setName(name);

        StatementBinder<Company> binder = (statement, companyObj) -> {
            statement.setString(1, companyObj.getName());
        };

        return executeUpdate(insertQuery, binder, company);
    }

    public List<Company> getAllCompanies() {
        String query = "SELECT ID, NAME FROM COMPANY ORDER BY ID";

        StatementMapper<Company> mapper = resultSet -> {
            Company company = new Company();
            company.setId(resultSet.getInt("ID"));
            company.setName(resultSet.getString("NAME"));
            return company;
        };

        return executeQuery(query, mapper);
    }

    public Company getCompanyById(int companyId) {
        String query = "SELECT * FROM COMPANY WHERE ID = ?";

        StatementMapper<Company> mapper = resultSet -> {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");

            Company company = new Company();
            company.setId(id);
            company.setName(name);

            return company;
        };

        List<Company> companies = executeQuery(query, mapper);
        return companies.isEmpty() ? null : companies.get(0);
    }
}
