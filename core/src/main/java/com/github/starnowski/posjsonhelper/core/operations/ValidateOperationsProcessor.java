package com.github.starnowski.posjsonhelper.core.operations;

import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;
import com.github.starnowski.posjsonhelper.core.util.Pair;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            sqlDefinitions.stream().flatMap(definition -> definition.getCheckingStatements().stream().map(cs -> new Pair<>(definition.getCreateScript(), cs)))
                    .filter(csKey -> {
                        try {
                            PreparedStatement statement = connection.prepareStatement(csKey.getValue());
                            ResultSet rs = statement.executeQuery();
                            rs.next();
                            long result = rs.getLong(1);
                            return result <= 0;
                        } catch (SQLException e) {
                            //TODO
                            throw new RuntimeException(e);
                        }
                    }
            );
//                    .collect(Collectors.toMap(sr -> sr.statement, sr -> sr.result));
        }
    }
}
