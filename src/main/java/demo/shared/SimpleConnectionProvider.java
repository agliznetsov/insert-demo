package demo.shared;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleConnectionProvider extends DatabaseProperties implements Supplier<Connection> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleConnectionProvider.class);
            
    public SimpleConnectionProvider(String propertiesName) throws IOException {
        super(propertiesName);
    }

    @Override
    public Connection get() {
        LOGGER.info("Connecting with {}", prop);
        String driver = prop.get("driver");
        try {
            Class.forName(driver).newInstance();
            return DriverManager.getConnection(prop.get("urlConnection"), prop.get("user"), prop.get("password"));
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException("Can not instantiate driver " + driver, ex);
        } catch (SQLException ex) {
            throw new RuntimeException("Can not connect to database", ex);
        }
    }

}
