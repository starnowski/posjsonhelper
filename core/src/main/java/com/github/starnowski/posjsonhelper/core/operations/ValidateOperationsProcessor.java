package com.github.starnowski.posjsonhelper.core.operations;

import com.github.starnowski.posjsonhelper.core.operations.exceptions.ValidationDatabaseOperationsException;
import com.github.starnowski.posjsonhelper.core.operations.util.SQLUtil;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;
import com.github.starnowski.posjsonhelper.core.util.Pair;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidateOperationsProcessor implements IDatabaseOperationsProcessor {

    private final SQLUtil sqlUtil;

    public ValidateOperationsProcessor() {
        this(new SQLUtil());
    }

    ValidateOperationsProcessor(SQLUtil sqlUtil) {
        this.sqlUtil = sqlUtil;
    }

    @Override
    public void run(DataSource dataSource, List<ISQLDefinition> sqlDefinitions) throws SQLException, ValidationDatabaseOperationsException {
        Map<String, Set<String>> failedChecks = null;
        try (Connection connection = dataSource.getConnection()) {
            failedChecks = sqlDefinitions.stream().flatMap(definition -> definition.getCheckingStatements().stream().map(cs -> new Pair<String, String>(definition.getCreateScript(), cs)))
                    .filter(csKey -> {
                                try {
                                    long result = sqlUtil.returnLongResultForQuery(connection, csKey.getValue());
                                    return result <= 0;
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    )
                    .collect(Collectors.toMap(cs1 -> cs1.getKey(), cs2 -> (Set)new HashSet<String>(Arrays.asList(cs2.getValue())), (o1, o2) -> (Set)new HashSet<String>(Stream.concat(o1.stream(), o2.stream()).collect(Collectors.toSet())), ()-> new LinkedHashMap<String, Set<String>>()));
        }
        if (!failedChecks.isEmpty()) {
            throw new ValidationDatabaseOperationsException(failedChecks);
        }
    }

    SQLUtil getSqlUtil() {
        return sqlUtil;
    }
}
