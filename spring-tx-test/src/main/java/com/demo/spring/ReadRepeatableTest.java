package com.demo.spring;

import java.sql.*;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/13 23:15
 *
 * Connection.TRANSACTION_REPEATABLE_READ
 * 可重复读 ,在一个事物中同一SQL语句 无论执行多少次都会得到相同的结果
 * 会出现幻读的问题
 */
public class ReadRepeatableTest {

    private static String jdbcUrl = "jdbc:mysql://192.168.5.104:3306/spring";
    private static String userName = "root";
    private static String password = "root";
    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException, SQLException, ClassNotFoundException {
        Thread t1 = run(new Runnable() {
            public void run() {
                try {
                    synchronized (lock) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                update("test");
            }
        });

        Thread t2 = run(new Runnable() {
            public void run() {
                try {
                    Connection conn = openConnection();
                    conn.setAutoCommit(false);
                    conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                    // 第一次读取 读取到的数据为未修改前的数据
                    select("test", conn);
                    // 释放锁
                    synchronized (lock) {
                        lock.notify();
                    }
                    // 第二次读取 TRANSACTION_REPEATABLE_READ级别，读取到的数据也为未修改前的数据 两次读取数据一致
                    // 设置id为主键 如果此时t1做插入（id=1），t2按主键查询（id=1）
                    // 因为此时为TRANSACTION_REPEATABLE_READ级别 ，所以查询为空，然后进行插入（id=1）
                    // 此时会出现主键冲突的异常，这种情况为幻读，有兴趣的可以尝试一下
                    Thread.sleep(500);
                    select("test", conn);
                    conn.close();
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

    public static void deleteAccount(Connection conn) {
        try {
            PreparedStatement prepare = conn.prepareStatement("delete from account");
            prepare.executeUpdate();
            System.out.println("执行删除成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(String user) {
        try {
            Connection conn = openConnection();
            PreparedStatement prepare = conn.
                    prepareStatement("update account set money = money + 1 where user = ?");
            prepare.setString(1, user);
            prepare.executeUpdate();
            conn.close();
            System.out.println("执行修改成功");
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


}
