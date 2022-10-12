package com.github.starnowski.posjsonhelper.core.operations;

import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidateOperationsProcessor implements IDatabaseOperationsProcessor {
    @Override
    public void run(DataSource dataSource, List<ISQLDefinition> sqlDefinitions) throws SQLException {
        Map<String, List<String>> failedChecks = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            //TODO
//            sqlDefinitions.stream().flatMap(definition -> definition.getCheckingStatements().stream())
//                    .map()

//                    .map(definition -> {
//
//                connection.prepareStatement(selectStatement.getCheckingStatements())
//                        Long result = connection.execute((StatementCallback<Long>) statement -> {
//                            ResultSet rs = statement.executeQuery(selectStatement);
//                            rs.next();
//                            return rs.getLong(1);
//                        });
//                        return new StatementAndItLongResult(selectStatement, result);
//                    }
//            ).collect(Collectors.toMap(sr -> sr.statement, sr -> sr.result));
        }
    }
}
