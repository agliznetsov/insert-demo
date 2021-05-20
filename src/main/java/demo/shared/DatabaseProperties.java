package demo.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseProperties {

    protected Map<String, String> prop = new HashMap<>();

    public DatabaseProperties(String propertiesName) throws IOException {
        InputStream is = locateInputStream(propertiesName);
        Properties p = new Properties();
        p.load(is);
        p.forEach((k, v) -> prop.put(k.toString(), v.toString()));
    }

    private InputStream locateInputStream(String propertiesName) throws FileNotFoundException {
        File f = new File(propertiesName);
        System.out.println(f.getAbsolutePath());
        if (f.exists()) {
            return new FileInputStream(f);
        }
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(propertiesName);
        if (is == null) {
            throw new FileNotFoundException(propertiesName);
        }
        return is;
    }

    public void with(String key, String value) {
        prop.put(key, value);
    }

    public String getProperty(String key) {
        return prop.get(key);
    }

}
