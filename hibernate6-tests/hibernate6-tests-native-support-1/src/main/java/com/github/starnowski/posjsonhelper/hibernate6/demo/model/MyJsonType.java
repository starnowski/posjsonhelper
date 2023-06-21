package com.github.starnowski.posjsonhelper.hibernate6.demo.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class MyJsonType implements UserType<JsonbContent> {
    @Override
    public int getSqlType() {
        return Types.VARCHAR;
    }

    @Override
    public Class<JsonbContent> returnedClass() {
        return JsonbContent.class;
    }

    @Override
    public boolean equals(JsonbContent jsonbContent, JsonbContent j1) {
        return false;
    }

    @Override
    public int hashCode(JsonbContent jsonbContent) {
        return 0;
    }

    @Override
    public JsonbContent nullSafeGet(ResultSet resultSet, int i, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws SQLException {
        final String cellContent = resultSet.getString(i);
        if (cellContent == null) {
            return null;
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(cellContent.getBytes("UTF-8"), returnedClass());
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to MyJson: " + ex.getMessage(), ex);
        }
    }

    @Override
    public JsonbContent deepCopy(JsonbContent jsonbContent) {
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement ps, JsonbContent value, int idx, SharedSessionContractImplementor sharedSessionContractImplementor) throws SQLException {
        if (value == null) {
            ps.setNull(idx, Types.OTHER);
            return;
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final StringWriter w = new StringWriter();
            mapper.writeValue(w, value);
            w.flush();
            ps.setObject(idx, w.toString(), Types.OTHER);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert MyJson to String: " + ex.getMessage(), ex);
        }
    }


    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(JsonbContent jsonbContent) {
        return null;
    }

    @Override
    public JsonbContent assemble(Serializable serializable, Object o) {
        return null;
    }
}
