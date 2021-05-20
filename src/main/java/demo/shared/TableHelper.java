package demo.shared;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import javax.sql.DataSource;

import org.postgresql.jdbc.PgConnection;

public class TableHelper {

    public static final String INSERT = "insert into attributes_copy (id, type, creator, modifiedby, owner, creationdate, lastmodified, date_time, numeric_value, "
            + "opt_lock, is_system, boolean_value, attr_type, expression_long) "
            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public final static String COPY = "COPY attributes_copy (id, type, creator, modifiedby, owner, creationdate, lastmodified, date_time, numeric_value, "
            + "opt_lock, is_system, boolean_value, attr_type, expression_long) FROM STDIN WITH (FORMAT TEXT, ENCODING 'UTF-8', DELIMITER '\t', HEADER false)";

    public static final String CREATE_TABLE = "create table attributes_copy\n"
            + "(\n"
            + "    id uuid not null primary key,\n"
            + "    type uuid not null,\n"
            + "    creator uuid,\n"
            + "    modifiedby uuid,\n"
            + "    owner uuid,\n"
            + "    creationdate bigint,\n"
            + "    lastmodified bigint,\n"
            + "    date_time bigint,\n"
            + "    numeric_value double precision,\n"
            + "    opt_lock integer,\n"
            + "    is_system boolean,\n"
            + "    boolean_value boolean,\n"
            + "    attr_type varchar(3) not null,\n"
            + "    expression_long text\n"
            + ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS attributes_copy";

    public static void createTable(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(DROP_TABLE);
            stmt.execute(CREATE_TABLE);
        }
    };

    static UUID type = UUID.fromString("00000000-0000-0000-0000-000000003114");
    static UUID user = UUID.fromString("00000000-0000-0000-0000-000000900001");

    //String.format("('%s'::uuid)", domain)
    public static void setParameters(PreparedStatement pstmt, int index) throws SQLException {
        setObject(pstmt, 1, UUID.randomUUID());
        setObject(pstmt, 2, type);
        setObject(pstmt, 3, user);
        setObject(pstmt, 4, user);
        setObject(pstmt, 5, user);
        setObject(pstmt, 6, 0);
        setObject(pstmt, 7, 0);
        setObject(pstmt, 8, null);
        setObject(pstmt, 9, null);
        setObject(pstmt, 10, 0);
        setObject(pstmt, 11, false);
        setObject(pstmt, 12, null);
        setObject(pstmt, 13, "SA");
        setObject(pstmt, 14, "attribute value " + index);
    }

    public static void addRow(StringBuilder sb, int index) {
        sb.append(UUID.randomUUID()).append("\t");
        sb.append(type).append("\t");
        sb.append(user).append("\t");
        sb.append(user).append("\t");
        sb.append(user).append("\t");
        sb.append(0).append("\t");
        sb.append(0).append("\t");
        sb.append("\\N").append("\t");
        sb.append("\\N").append("\t");
        sb.append(0).append("\t");
        sb.append(false).append("\t");
        sb.append("\\N").append("\t");
        sb.append("SA").append("\t");
        sb.append("attribute value " + index);
        sb.append("\n");
    }

    /*
     * Own setObject implementation to avoid problems between different setObject
     * implementations in each driver. Extracted from JFleet implementation.
     */
    public static void setObject(PreparedStatement pstmt, int parameterIndex, Object parameterObj) throws SQLException {
        if (parameterObj == null) {
            pstmt.setNull(parameterIndex, java.sql.Types.OTHER);
        } else {
            if (parameterObj instanceof Integer) {
                pstmt.setInt(parameterIndex, ((Integer) parameterObj).intValue());
            } else if (parameterObj instanceof String) {
                pstmt.setString(parameterIndex, (String) parameterObj);
            } else if (parameterObj instanceof Long) {
                pstmt.setLong(parameterIndex, ((Long) parameterObj).longValue());
            } else if (parameterObj instanceof Boolean) {
                pstmt.setBoolean(parameterIndex, ((Boolean) parameterObj).booleanValue());
            } else if (parameterObj instanceof java.util.Date) {
                pstmt.setTimestamp(parameterIndex, new Timestamp(((java.util.Date) parameterObj).getTime()));
            } else if (parameterObj instanceof BigDecimal) {
                pstmt.setBigDecimal(parameterIndex, (BigDecimal) parameterObj);
            } else if (parameterObj instanceof Byte) {
                pstmt.setInt(parameterIndex, ((Byte) parameterObj).intValue());
            } else if (parameterObj instanceof Character) {
                pstmt.setString(parameterIndex, ((Character) parameterObj).toString());
            } else if (parameterObj instanceof Short) {
                pstmt.setShort(parameterIndex, ((Short) parameterObj).shortValue());
            } else if (parameterObj instanceof Float) {
                pstmt.setFloat(parameterIndex, ((Float) parameterObj).floatValue());
            } else if (parameterObj instanceof Double) {
                pstmt.setDouble(parameterIndex, ((Double) parameterObj).doubleValue());
            } else if (parameterObj instanceof java.sql.Date) {
                pstmt.setDate(parameterIndex, (java.sql.Date) parameterObj);
            } else if (parameterObj instanceof Time) {
                pstmt.setTime(parameterIndex, (Time) parameterObj);
            } else if (parameterObj instanceof Timestamp) {
                pstmt.setTimestamp(parameterIndex, (Timestamp) parameterObj);
            } else if (parameterObj instanceof BigInteger) {
                pstmt.setObject(parameterIndex, parameterObj);
            } else if (parameterObj instanceof LocalDate) {
                pstmt.setObject(parameterIndex, parameterObj);
            } else if (parameterObj instanceof LocalTime) {
                pstmt.setObject(parameterIndex, parameterObj);
            } else if (parameterObj instanceof LocalDateTime) {
                pstmt.setObject(parameterIndex, parameterObj);
            } else if (parameterObj instanceof UUID) {
                pstmt.setObject(parameterIndex, parameterObj, java.sql.Types.OTHER);
            } else {
                throw new RuntimeException("No type mapper for " + parameterObj.getClass());
            }
        }
    }

}
