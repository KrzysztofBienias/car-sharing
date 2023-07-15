package carsharing.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface StatementMapper<T> {
    T mapResultSet(ResultSet resultSet) throws SQLException;
}
