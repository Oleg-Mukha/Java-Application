package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner scan = new Scanner(System.in);

    static void showGoodsList() {
        List<Goods> goodsList = new GoodsServiceMySQL().goodsData();
        printArray(goodsList);
    }

    static void findByName() {
        System.out.print("Enter product name what do you want to find: ");
        String findProduct = scan.nextLine();
        System.out.println();
        System.out.println("Found according to your request:");
        System.out.println(new GoodsServiceMySQL().findByProduct(findProduct));
        System.out.println();
    }

    static void findByBrand() {
        System.out.print("Enter brand name what do you want to find: ");
        String findBrand = scan.nextLine();
        List<Goods> searchedList = new GoodsServiceMySQL().findByBrand(findBrand);
        if (searchedList.isEmpty()) {
            System.out.println("There are no products with brand '" + findBrand + "'.\n");
        } else {
            System.out.println("\nFound according to your request:");
            printArray(searchedList);
        }
    }

    static void addProductToCart(ArrayList<Goods> cart) {
        System.out.print("Enter the product name that you want to add to cart: ");
        String product = scan.nextLine();
        new GoodsServiceMySQL().addProductToCart(cart, product);
    }

    static void shopMainMenu() {
        ArrayList cart = new ArrayList();
        boolean menuFlag;
        while (menuFlag = true) {
            System.out.print("\t\t..::SHOP MENU::..\n'/list' - show product list\n'/fname' - find product by name\n'/fbrand' - find product by brand\n'/add' - add product to your cart\n'/cart' - go to cart menu\n'/exit' - stop program\n\nEnter command: ");
            String choose = scan.nextLine();
            System.out.println();
            switch (choose) {
                case ("/list"):
                    showGoodsList();
                    break;
                case ("/fname"):
                    findByName();
                    break;
                case ("/fbrand"):
                    findByBrand();
                    break;
                case ("/add"):
                    addProductToCart(cart);
                    break;
                case ("/cart"):
                    cartMenu(cart, menuFlag);
                    break;
                case ("/exit"):
                    drawKitty();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown command, try again.\n");
                    break;
            }
        }
    }

    static void showCart(ArrayList cart) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty! Nothing to display.\n");
        } else {
            System.out.println("\t\t\t\t..::CART LIST::..");
            int num = 1;
            for (int i = 0; i < cart.size(); i++) {
                System.out.println(num + ") " + cart.get(i));
                num++;
            }
            System.out.println();
        }
    }

    static void removeProductFromCart(ArrayList<Goods> cart) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty! Nothing to remove.\n");
        } else {
            try {
                Scanner scan = new Scanner(System.in);
                System.out.print("Enter product number, what do you want to remove from your cart: ");
                int delNum = scan.nextInt();
                new GoodsServiceMySQL().removeProductFromCart(cart.get(delNum - 1).getProduct());
                cart.remove(delNum - 1);
                System.out.println("You have successfully removed product from your cart.\n");
            } catch (IndexOutOfBoundsException IO) {
                System.out.println("#IndexOutOfBoundsException - There are not so many elements in ArrayList!\n");
            }
        }
    }

    static void makeOrder(ArrayList<Goods> cart) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty! You should add product to your cart with the command '/add' before creating an order!\n");
        } else {
            for (int i = 0; i < cart.size(); i++) {
                Order order = new Order(cart.get(i).getProduct(), cart.get(i).getBrand(), "processed", cart.get(i).getPrice());
                new GoodsServiceMySQL().makeOrder(order);
            }
            cart.clear();
            System.out.println("Order successfully created! You can view the contents of the cart with the command '/cart -> /orderl'.\n");
        }
    }

    static void showOrderList() {
        List<Order> orders = new GoodsServiceMySQL().ordersData();
        if (orders.isEmpty()) {
            System.out.println("Order list is empty! It's high time to order something!");
        }
        printOrders(orders);
    }

    static void declineOrder() {
        System.out.print("Enter product name in order that you want to cancel: ");
        String product = scan.nextLine();
        new GoodsServiceMySQL().cancelOrder(product);
        System.out.println("Order canceled successfully!\n");
    }

    static void cartMenu(ArrayList cart, boolean flag) {
        flag = false;
        while (!flag) {
            System.out.print("\t\t..::CART MENU::..\n'/list' - view the cart contents\n'/remove' - delete any good in your cart\n'/order' - make an order\n'/orderl' - view order list\n'/canorder' - cancel an order\n'/back' - back to main menu\n\nEnter command: ");
            String cartAnswer = scan.nextLine();
            System.out.println();
            switch (cartAnswer) {
                case ("/list"):
                    showCart(cart);
                    break;
                case ("/remove"):
                    removeProductFromCart(cart);
                    break;
                case ("/order"):
                    makeOrder(cart);
                    break;
                case ("/orderl"):
                    showOrderList();
                    break;
                case ("/canorder"):
                    declineOrder();
                    break;
                case ("/back"):
                    flag = true;
                    System.out.println("You returned to the main menu.\n");
                    break;
                default:
                    System.out.println("Unknown command, try again.\n");
                    break;
            }
        }
    }

    public static void drawKitty() {
        System.out.print("\t  ／＞  フ\n" + "     |  _  _)\n" + "    ／`ミ _x 彡\tBye Bye :*\n" + "     /      |\n" + "    /  ヽ   ﾉ\n" + " ／￣|   | | |\n" + " | (￣ヽ＿_ヽ_)_)\n" + " ＼二つ");
    }

    private static void printArray(List<Goods> range) {
        for (Goods goods : range) {
            System.out.println(goods);
        }
        System.out.println();
    }

    private static void printOrders(List<Order> range) {
        int id = 1;
        for (Order order : range) {
            System.out.println("Order №[" + id + "]" + order);
            id++;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        shopMainMenu();
    }
}
