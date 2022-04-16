package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoodsServiceMySQL implements GoodsDAO {

    /**
     * data for connecting to MySQL Database
     **/

    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/goods";
    private static final String USER = "root";
    private static final String PASSWORD = "0992951902";

    /**
     * MySQL queries
     **/

    private static final String FIND_BY_PRODUCT = "SELECT * FROM goods WHERE product=?";
    private static final String FIND_BY_BRAND = "SELECT * FROM goods WHERE brand=?";
    private static final String SWITCH_AVAILABILITY_TO_FALSE = "UPDATE goods SET availability=false WHERE product=?";
    private static final String SWITCH_AVAILABILITY_TO_TRUE = "UPDATE goods SET availability=true WHERE product=?";
    private static final String INSERT_INTO_ORDERS = "INSERT INTO orders(product, brand, price, status) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_ORDERS = "UPDATE orders SET status='canceled' WHERE product=?";
    private static final String SELECT_ALL_ORDERS = "SELECT product, brand, price, status FROM orders";
    private static final String SELECT_ALL_GOODS = "SELECT product, brand, price, availability FROM goods";

    private Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not loaded!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Сonnection not established!");
            e.printStackTrace();
        }
        return connection;
    }

    private static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String findByProduct(String product) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(FIND_BY_PRODUCT);
            preparedStatement.setString(1, product);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Goods goods = new Goods();
                goods.setProduct(rs.getString("product"));
                goods.setBrand(rs.getString("brand"));
                goods.setPrice(rs.getInt("price"));
                goods.setAvailability(rs.getBoolean("availability"));
                return goods.toString();
            } else {
                return "There are no products with name '" + product + "'!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(connection);
            close(preparedStatement);
        }
    }

    @Override
    public List<Goods> findByBrand(String brand) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Goods> searchedList = new ArrayList<>();

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(FIND_BY_BRAND);
            preparedStatement.setString(1, brand);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Goods goods = new Goods();
                goods.setProduct(resultSet.getString("product"));
                goods.setBrand(resultSet.getString("brand"));
                goods.setPrice(resultSet.getInt("price"));
                goods.setAvailability(resultSet.getBoolean("availability"));

                searchedList.add(goods);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(connection);
            close(preparedStatement);
        }
        return searchedList;
    }

    @Override
    public List<Goods> addProductToCart(List<Goods> cart, String product) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(FIND_BY_PRODUCT);
            preparedStatement.setString(1, product);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Goods goods = new Goods();
                goods.setProduct(rs.getString("product"));
                goods.setBrand(rs.getString("brand"));
                goods.setPrice(rs.getInt("price"));
                goods.setAvailability(rs.getBoolean("availability"));

                if (!goods.getAvailability()) {
                    System.out.println("Error! This product is out of stock!");
                } else {
                    cart.add(goods);
                    System.out.println("Product '" + product + "' successfully added to your cart. You can view the contents of the cart with the command '/cart -> /list'.\n");
                    preparedStatement = connection.prepareStatement(SWITCH_AVAILABILITY_TO_FALSE);
                    preparedStatement.setString(1, product);

                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(connection);
            close(preparedStatement);
        }
        return cart;
    }

    @Override
    public void removeProductFromCart(String product) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SWITCH_AVAILABILITY_TO_TRUE);
            preparedStatement.setString(1, product);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(connection);
            close(preparedStatement);
        }
    }

    @Override
    public void makeOrder(Order order) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(INSERT_INTO_ORDERS, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, order.getProduct());
            preparedStatement.setString(2, order.getBrand());
            preparedStatement.setInt(3, order.getTotalPrice());
            preparedStatement.setString(4, order.getStatus());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(connection);
            close(preparedStatement);
        }
    }

    @Override
    public void cancelOrder(String product) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_ORDERS);
            preparedStatement.setString(1, product);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(SWITCH_AVAILABILITY_TO_TRUE);
            preparedStatement.setString(1, product);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(connection);
            close(preparedStatement);
        }
    }

    @Override
    public List<Order> ordersData() {
        Connection connection = null;
        Statement statement = null;
        List<Order> goodsList = new ArrayList<>();

        try {
            connection = getConnection();
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(SELECT_ALL_ORDERS);
            while (resultSet.next()) {
                Order order = new Order();
                order.setProduct(resultSet.getString("product"));
                order.setBrand(resultSet.getString("brand"));
                order.setTotalPrice(resultSet.getInt("price"));
                order.setStatus(resultSet.getString("status"));

                goodsList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
            close(statement);
        }
        return goodsList;
    }

    @Override
    public List<Goods> goodsData() {
        Connection connection = null;
        Statement statement = null;
        List<Goods> goodsList = new ArrayList<>();

        try {
            connection = getConnection();
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(SELECT_ALL_GOODS);
            while (resultSet.next()) {
                Goods goods = new Goods();
                goods.setProduct(resultSet.getString("product"));
                goods.setBrand(resultSet.getString("brand"));
                goods.setPrice(resultSet.getInt("price"));
                goods.setAvailability(resultSet.getBoolean("availability"));

                goodsList.add(goods);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
            close(statement);
        }
        return goodsList;
    }
}
