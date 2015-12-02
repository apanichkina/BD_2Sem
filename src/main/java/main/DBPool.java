package main;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;


/**
 * Created by anna on 14.11.15.
 */
public class DBPool {
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String URL_DB = "jdbc:mysql://localhost:3306/forumdb?autoreconnect=true&useUnicode=yes&characterEncoding=UTF-8";
    public static final String USER_DB = "root";
    public static final String PASSWORD_DB = "12345";

    private GenericObjectPool connectionPool = null;

    public DataSource createSource() throws Exception
    {
        Class.forName(DBPool.DRIVER).newInstance();

        //
        // Creates an instance of GenericObjectPool that holds our
        // pool of connections object.
        //
        connectionPool = new GenericObjectPool();
        connectionPool.setMaxActive(100);

        //
        // Creates a connection factory object which will be use by
        // the pool to create the connection object. We passes the
        // JDBC url info, username and password.
        //
        ConnectionFactory cf = new DriverManagerConnectionFactory(
                DBPool.URL_DB,
                DBPool.USER_DB,
                DBPool.PASSWORD_DB);

        //
        // Creates a PoolableConnectionFactory that will wraps the
        // connection object created by the ConnectionFactory to add
        // object pooling functionality.
        //
        PoolableConnectionFactory pcf =
                new PoolableConnectionFactory(cf, connectionPool,
                        null, null, false, true);
        return new PoolingDataSource(connectionPool);
    }

    public GenericObjectPool getConnectionPool() {
        return connectionPool;
    }
}