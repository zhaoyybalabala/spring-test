package com.demo.spring;

import java.sql.*;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/13 22:32
 *
 * Connection.TRANSACTION_READ_COMMITTED
 * 允许读取已提交事物，会出现不可重复读，幻读的问题
 */
public class ReadCommittedTest {
    private static String jdbcUrl = "jdbc:mysql://192.168.5.104:3306/spring";
    private static String userName = "root";
    private static String password = "root";
    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = run(new Runnable() {
            public void run() {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    insert("001", "test", 100);
                }
            }
        });

        Thread t2 = run(new Runnable() {
            public void run() {
                try {
                    Connection connection = openConnection();
                    connection.setAutoCommit(false);
                    // 将参数升级成 Connection.TRANSACTION_REPEATABLE_READ 即可解决不可重复读问题
                    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                    // 第一次读取不到
                    select("test", connection);
                    // 释放锁
                    synchronized (lock) {
                        lock.notify();
                    }
                    // 第二次读取到(数据不一至)
                    Thread.sleep(500);
                    select("test", connection);
                    connection.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.join();
        t2.join();

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
