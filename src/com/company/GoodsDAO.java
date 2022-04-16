package com.company;

import java.util.List;

public interface GoodsDAO {

    String findByProduct(String product);

    List<Goods> addProductToCart(List<Goods> cart, String product);

    void removeProductFromCart(String product);

    void makeOrder(Order order);

    void cancelOrder(String product);

    List<Goods> findByBrand(String brand);

    List<Goods> goodsData();

    List<Order> ordersData();
}
