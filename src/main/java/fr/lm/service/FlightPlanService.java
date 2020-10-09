package fr.lm.service;

import fr.lm.model.Customer;
import fr.lm.model.Drone;
import fr.lm.model.FlightPlan;
import fr.lm.model.Order;
import fr.lm.model.Product;
import fr.lm.model.Store;
import fr.lm.repository.InitDataRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class FlightPlanService {

    private final InitDataRepository initDataService = new InitDataRepository();

    public List<FlightPlan> calculateFlightPlan() {
        List<FlightPlan> flightPlans = new ArrayList<>();
        List<Order> orderList = initDataService.findAllOrder();
        List<Store> storeList = initDataService.findAllStore();
        List<Drone> droneList = initDataService.findAllDrone();

        orderList.forEach(order -> order.getProductQuantities()
                .forEach(productQuantities -> IntStream.range(0, productQuantities.getQuantity())
                        .forEach(iteration -> {
                            Store store = findClosestStoreWithProductInStock(storeList, productQuantities.getProduct(), order.getCustomer()).get();
                            Drone drone = findClosestDrone(droneList, store, order.getCustomer()).get();
                            flightPlans.add(newFlightPlan(drone, store, productQuantities.getProduct(), order.getCustomer()));

                            updateStoreStock(storeList, store, productQuantities.getProduct());
                            updateDroneList(droneList, drone, store, order.getCustomer());
                        })));
        return flightPlans;
    }

    private Optional<Store> findClosestStoreWithProductInStock(List<Store> storeList, Product product, Customer customer) {
        return storeList.stream()
                .filter(store -> isProductAndQuantityAvailableInTheStore(store, product))
                .min(Comparator.comparing(store -> calculateDistance(store.getX(), store.getY(), customer.getX(), customer.getY())));
    }

    private Optional<Drone> findClosestDrone(List<Drone> droneList, Store store, Customer customer) {
        return droneList.stream()
                .filter(drone -> isDroneHaveEnoughAutonomy(drone, store, customer))
                .min(Comparator.comparing(drone -> calculateDistance(drone.getX(), drone.getY(), store.getX(), store.getY())));

    }

    private Boolean isProductAndQuantityAvailableInTheStore(Store store, Product product) {
        return store.getProductQuantities().stream()
                .anyMatch(productQuantities -> product.getProductId().equals(productQuantities.getProduct().getProductId()) && productQuantities.getQuantity() > 0);
    }

    private Boolean isDroneHaveEnoughAutonomy(Drone drone, Store store, Customer customer) {
        double droneToStoreDistance = calculateDistance(drone.getX(), drone.getY(), store.getX(), store.getY());
        double toTravelDistance = droneToStoreDistance + calculateDistance(store.getX(), store.getY(), customer.getX(), customer.getY());
        return drone.getAutonomy() > toTravelDistance;
    }

    private FlightPlan newFlightPlan(Drone drone, Store store, Product product, Customer customer) {
        return FlightPlan.builder()
                .droneId(drone.getId())
                .storeId(store.getId())
                .productId(product.getProductId())
                .customerId(customer.getId())
                .build();
    }

    private Drone moveDrone(Drone drone, Store store, Customer customer) {
        double droneToStoreDistance = calculateDistance(drone.getX(), drone.getY(), store.getX(), store.getY());
        double toTravelDistance = droneToStoreDistance + calculateDistance(store.getX(), store.getY(), customer.getX(), customer.getY());
        return Drone.builder()
                .id(drone.getId())
                .autonomy(drone.getAutonomy() - toTravelDistance)
                .x(customer.getX())
                .y(customer.getY())
                .build();
    }

    private void updateDroneList(final List<Drone> droneList, Drone usedDrone, Store store, Customer customer) {
        droneList.stream().filter(drone -> drone.getId().equals(usedDrone.getId()))
                .forEach(drone -> {
                    Drone movedDrone = moveDrone(drone, store, customer);
                    drone.setAutonomy(movedDrone.getAutonomy());
                    drone.setX(movedDrone.getX());
                    drone.setY(movedDrone.getY());
                });
    }

    private void updateStoreStock(final List<Store> storeList, Store storeToUpdate, Product product) {
        storeList.stream().filter(store -> store.getId().equals(storeToUpdate.getId()))
                .forEach(store -> store.getProductQuantities()
                        .stream()
                        .filter(productQuantities -> productQuantities.getProduct().getProductId().equals(product.getProductId()))
                        .forEach(productQuantities -> productQuantities.setQuantity(productQuantities.getQuantity() - 1)));

    }

    private Double calculateDistance(Double x1, Double y1, Double x2, Double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
}
