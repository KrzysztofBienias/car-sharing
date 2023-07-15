package carsharing.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementBinder<T> {
    void bindValues(PreparedStatement statement, T entity) throws SQLException;
}


