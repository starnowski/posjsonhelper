package com.github.starnowski.posjsonhelper.poc;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlFunctionScriptsExecutor {

    //TODO boolean showSql

    public void execute(DataSource dataSource, URI script) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(readAllBytes(script));
        }
    }

    private String readAllBytes(URI fileUri) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(fileUri)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
