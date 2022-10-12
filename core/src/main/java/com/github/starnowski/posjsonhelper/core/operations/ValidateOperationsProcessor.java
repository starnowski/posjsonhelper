package com.github.starnowski.posjsonhelper.core.operations;

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
    public void run(DataSource dataSource, List<ISQLDefinition> sqlDefinitions) throws SQLException {
        Map<String, List<String>> failedChecks = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            //TODO
            LinkedHashMap<String, HashSet> map = sqlDefinitions.stream().flatMap(definition -> definition.getCheckingStatements().stream().map(cs -> new Pair<>(definition.getCreateScript(), cs)))
                    .filter(csKey -> {
                                try {
                                    long result = sqlUtil.returnLongResultForQuery(connection, csKey.getValue());
                                    return result <= 0;
                                } catch (SQLException e) {
                                    //TODO
                                    throw new RuntimeException(e);
                                }
                            }
                    ).collect(Collectors.toMap(Pair::getKey, cs -> new HashSet(Arrays.asList(cs.getValue())), (o1, o2) -> Stream.concat(o1.stream(), o2.stream()).collect(Collectors.toSet()), ()-> new LinkedHashMap<String, HashSet>()));
//            if (!map.isEmpty()) {
//                throw new
//            }
//                    .collect(Collectors.toMap(sr -> sr.statement, sr -> sr.result));
        }
    }

    SQLUtil getSqlUtil() {
        return sqlUtil;
    }
}
