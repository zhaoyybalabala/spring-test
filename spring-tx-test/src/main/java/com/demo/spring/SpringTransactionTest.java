package com.demo.spring;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/13 20:51
 */
public class SpringTransactionTest {

    private static String jdbcUrl = "jdbc:mysql://192.168.5.104:3306/spring";
    private static String userName = "root";
    private static String password = "root";

    public static Connection openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
        return conn;
    }

    public static void main(String[] args) {
        final DataSource dataSource = new DriverManagerDataSource(jdbcUrl, userName, password);
        TransactionTemplate template = new TransactionTemplate();
        template.setTransactionManager(new DataSourceTransactionManager(dataSource));

        template.execute(new TransactionCallback<Object>() {
            public Object doInTransaction(TransactionStatus transactionStatus) {
                Connection connection = DataSourceUtils.getConnection(dataSource);
                Object savePoint = null;

                try {
                    {
                        PreparedStatement preparedStatement = connection.prepareStatement(
                                "insert into account(accountName,user,money) VALUES (?,?,?)");
                        preparedStatement.setString(1,"111");
                        preparedStatement.setString(2,"a");
                        preparedStatement.setInt(3,100);
                        preparedStatement.executeUpdate();
                    }
                    savePoint = transactionStatus.createSavepoint();

                    {
                        PreparedStatement preparedStatement = connection.prepareStatement(
                                "insert into account(accountName,user,money) VALUES (?,?,?)");
                        preparedStatement.setString(1,"222");
                        preparedStatement.setString(2,"b");
                        preparedStatement.setInt(3,100);
                        preparedStatement.executeUpdate();
                    }
                    {
                        PreparedStatement preparedStatement = connection.prepareStatement(
                            "update account set money= money+1 where user=?");
                        preparedStatement.setString(1,"333");

                        int i = 1/0;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("update failed");
                    if (savePoint != null) {
                        transactionStatus.rollbackToSavepoint(savePoint);
                    } else {
                        transactionStatus.setRollbackOnly();
                    }
                }
                return null;
            }
        });
    }
}
