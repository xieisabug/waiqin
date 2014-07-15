package DbUtil;
import java.io.InputStream;
import java.util.Properties;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;  
import com.alibaba.druid.pool.DruidPooledConnection;
  
public class DruidDataSourceUtil {
    public  DruidDataSource getDataSource()  
        throws Exception {
    	Properties configureProperties = new Properties();
    	InputStream is = DruidDataSourceUtil.class.getClassLoader().getResourceAsStream("ConfigureProperties.properties");   
    	configureProperties.load(is);
        return (DruidDataSource)DruidDataSourceFactory.createDataSource(configureProperties);
    }
    public  DruidPooledConnection getDruidConnection(DruidDataSource druidDataSource) throws Exception {
        return druidDataSource.getConnection();
    }
}  
