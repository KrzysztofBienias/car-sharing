package carsharing.abstracts;

import carsharing.db.Database;
import carsharing.utils.StatementBinder;
import carsharing.utils.StatementMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    protected final Database database;

    protected BaseDAO(Database database) {
        this.database = database;
    }

    protected Connection getConnection() throws SQLException {
        return database.getConnection();
    }

    protected List<T> executeQuery(String query, StatementMapper<T> mapper) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            List<T> entities = new ArrayList<>();
            while (resultSet.next()) {
                T entity = mapper.mapResultSet(resultSet);
                entities.add(entity);
            }
            return entities;
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    protected <U> boolean executeUpdate(String query, StatementBinder<U> binder, U entity) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            binder.bindValues(statement, entity);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            return false;
        }
    }
}

