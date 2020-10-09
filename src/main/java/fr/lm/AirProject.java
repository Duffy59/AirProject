package fr.lm;

import fr.lm.service.FlightPlanService;

public class AirProject {

    private final FlightPlanService flightPlanService = new FlightPlanService();

    public static void main(String[] args) {
        new AirProject().simulateFlightPlan();
    }

    public void simulateFlightPlan(){
        System.out.println(flightPlanService.calculateFlightPlan());
    }
}
