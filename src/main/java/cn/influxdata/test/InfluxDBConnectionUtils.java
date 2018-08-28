package cn.influxdata.test;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * InfluxDBConnectionUtils
 *
 * @create 2018-08-28 14:28
 * @copyright huxiaolei1997@gmail.com
 */
public class InfluxDBConnectionUtils {
    private static String USERNAME;
    private static String PASSWORD;
    private static String URL;
    private static String DATABASE;
    private static InfluxDB influxDB;

    static {
        InputStream inputStream = InfluxDBConnectionUtils.class.getResourceAsStream("/influxdb.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            USERNAME = properties.getProperty("influxdb.username");
            PASSWORD = properties.getProperty("influxdb.password");
            URL = properties.getProperty("influxdb.url");
            DATABASE = properties.getProperty("influxdb.database");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InfluxDB getInfluxDbConnection() {
        influxDB = InfluxDBFactory.connect(URL, USERNAME, PASSWORD);
        //influxDB.setDatabase(DATABASE);
        return influxDB;
    }

    public static void main(String[] args) {
        System.out.println(USERNAME + ", " + PASSWORD + ", " + URL + ", " + DATABASE);
        //InfluxDBConnectionUtils influxDBConnectionUtils = new InfluxDBConnectionUtils();
        InfluxDB influxDB = InfluxDBConnectionUtils.getInfluxDbConnection();
        Query query = new Query("select * from cpu", DATABASE);
        QueryResult queryResult = influxDB.query(query);
        List<QueryResult.Result> qr = queryResult.getResults();
        qr.forEach(x -> {
            x.getSeries().forEach(m -> {
                System.out.println(m.getColumns().toString());
                m.getValues().forEach(n -> {
                    n.forEach(z -> System.out.println(z));
                });
            });
        });
        //System.out.println(queryResult.getResults().get(0));
    }

}
