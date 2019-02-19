package com.demo.spring;

import java.sql.*;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/13 22:55
 *
 * Connection.TRANSACTION_READ_UNCOMMITTED
 * 允许读取未提交事物，会出现脏读，不可重复读，幻读的问题
 */
public class ReadUncommittedTest {
    private static String jdbcUrl = "jdbc:mysql://192.168.5.104:3306/spring";
    private static String userName = "root";
    private static String password = "root";
    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException, SQLException, ClassNotFoundException {
        Thread t1 = run(new Runnable() {
            public void run() {
                insert("001", "test", 100);
            }
        });

        Thread t2 = run(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500);
                    Connection conn = openConnection();
                    // 将参数升级成 Connection.TRANSACTION_READ_COMMITTED 即可解决脏读的问题
                    conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                    select("test", conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t1.join();

    }

    public static Thread run(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public static Connection openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
        return conn;
    }

    static {
        try {
            Connection connection = openConnection();
            //deleteAccount(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insert(String accountName, String name, int money) {
        try {
            Connection conn = openConnection();
            PreparedStatement prepare = conn.
                    prepareStatement("insert into account (accountname,user,money) values (?,?,?)");
            prepare.setString(1, accountName);
            prepare.setString(2, name);
            prepare.setInt(3, money);
            prepare.executeUpdate();
            System.out.println("执行插入成功");
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void select(String name, Connection conn) {
        try {
            PreparedStatement prepare = conn.
                    prepareStatement("select * from account where user = ?");
            prepare.setString(1, name);
            ResultSet resultSet = prepare.executeQuery();
            System.out.println("执行查询");
            while (resultSet.next()) {
                for (int i = 1; i <= 4; i++) {
                    System.out.print(resultSet.getString(i) + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAccount(Connection conn) {
        try {
            PreparedStatement prepare = conn.prepareStatement("delete from account");
            prepare.executeUpdate();
            System.out.println("执行删除");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
