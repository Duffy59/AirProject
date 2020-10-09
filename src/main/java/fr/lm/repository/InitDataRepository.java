package fr.lm.repository;

import fr.lm.model.Customer;
import fr.lm.model.Drone;
import fr.lm.model.Order;
import fr.lm.model.Product;
import fr.lm.model.ProductQuantities;
import fr.lm.model.Store;

import java.util.ArrayList;
import java.util.List;

public class InitDataRepository {

    public List<Drone> findAllDrone() {
        Drone drone1 = Drone.builder()
                .id("LMDR-1")
                .x(5.0)
                .y(10.0)
                .autonomy(100.0)
                .build();

        Drone drone2 = Drone.builder()
                .id("LMDR-2")
                .x(2.0)
                .y(7.0)
                .autonomy(100.0)
                .build();
        Drone drone3 = Drone.builder()
                .id("LMDR-3")
                .x(6.0)
                .y(20.0)
                .autonomy(100.0)
                .build();
        Drone drone4 = Drone.builder()
                .id("LMDR-4")
                .x(4.0)
                .y(43.0)
                .autonomy(100.0)
                .build();
        Drone drone5 = Drone.builder()
                .id("LMDR-5")
                .x(9.0)
                .y(9.0)
                .autonomy(100.0)
                .build();

        return List.of(drone1, drone2, drone3, drone4, drone5);
    }

    public List<Customer> findAllCustomer() {
        Customer customer1 = Customer.builder()
                .id("CUS-1")
                .x(5.0)
                .y(8.0)
                .build();
        Customer customer2 = Customer.builder()
                .id("CUS-2")
                .x(20.0)
                .y(20.0)
                .build();
        Customer customer3 = Customer.builder()
                .id("CUS-3")
                .x(12.0)
                .y(3.0)
                .build();
        ;

        return List.of(customer1, customer2, customer3);
    }

    public List<Product> findAllProduct() {
        Product product1 = Product.builder()
                .productId("LMFRPRD-1")
                .name("Shovel").
                        build();
        Product product2 = Product.builder()
                .productId("LMFRPRD-2")
                .name("Pickaxe")
                .build();
        Product product3 = Product.builder()
                .productId("LMFRPRD-3")
                .name("Rake")
                .build();

        return List.of(product1, product2, product3);
    }

    public List<Store> findAllStore() {

        // store 1
        List<ProductQuantities> productQuantitiesForStore1 = new ArrayList<>();
        productQuantitiesForStore1.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-1", 10));
        productQuantitiesForStore1.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-3", 2));
        Store store1 = Store.builder()
                .id("LMFR-VA")
                .x(3.0)
                .y(3.0)
                .productQuantities(productQuantitiesForStore1)
                .build();

        // store 2
        List<ProductQuantities> productQuantitiesForStore2 = new ArrayList<>();
        productQuantitiesForStore2.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-1", 1));
        productQuantitiesForStore2.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-2", 3));
        Store store2 = Store.builder()
                .id("LMFR-RO")
                .x(23.0)
                .y(45.0)
                .productQuantities(productQuantitiesForStore2)
                .build();

        // store 3
        List<ProductQuantities> productQuantitiesForStore3 = new ArrayList<>();
        productQuantitiesForStore3.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-2", 2));
        productQuantitiesForStore3.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-3", 1));
        Store store3 = Store.builder()
                .id("LMFR-LE")
                .x(10.0)
                .y(14.0)
                .productQuantities(productQuantitiesForStore3)
                .build();

        return List.of(store1, store2, store3);
    }

    public List<Order> findAllOrder() {

        // first order
        List<ProductQuantities> productQuantitiesForOrder1 = new ArrayList<>();
        productQuantitiesForOrder1.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-1", 5));
        productQuantitiesForOrder1.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-2", 1));
        productQuantitiesForOrder1.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-3", 1));
        Order order1 = Order.builder()
                .id("LMFRORDER-1")
                .customer(getCustomerById("CUS-1"))
                .productQuantities(productQuantitiesForOrder1)
                .build();

        // second order
        List<ProductQuantities> productQuantitiesForOrder2 = new ArrayList<>();
        productQuantitiesForOrder2.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-2", 1));
        productQuantitiesForOrder2.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-3", 1));
        Order order2 = Order.builder()
                .id("LMFRORDER-2")
                .customer(getCustomerById("CUS-2"))
                .productQuantities(productQuantitiesForOrder2)
                .build();

        // third order
        List<ProductQuantities> productQuantitiesForOrder3 = new ArrayList<>();
        productQuantitiesForOrder3.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-1", 5));
        productQuantitiesForOrder3.add(getProductQuantitiesByProductIdAndQuantity("LMFRPRD-3", 1));
        Order order3 = Order.builder()
                .id("LMFRORDER-3")
                .customer(getCustomerById("CUS-3"))
                .productQuantities(productQuantitiesForOrder3)
                .build();

        return List.of(order1, order2, order3);
    }

    private ProductQuantities getProductQuantitiesByProductIdAndQuantity(String productId, Integer quantity) {
        List<Product> products = findAllProduct();
        ProductQuantities productQuantities = new ProductQuantities();
        productQuantities.setQuantity(quantity);
        productQuantities.setProduct(products.stream().filter(product -> productId.equals(product.getProductId())).findAny().orElse(null));
        return productQuantities;
    }

    private Customer getCustomerById(String customerId) {
        List<Customer> customers = findAllCustomer();
        return customers.stream().filter(customer -> customerId.equals(customer.getId())).findAny().orElse(null);
    }
}
