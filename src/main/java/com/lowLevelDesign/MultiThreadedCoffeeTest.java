package main.java.com.lowLevelDesign;

public class MultiThreadedCoffeeTest {

    public static void main(String[] args) throws InterruptedException {
        Runnable customer1 = () -> {
            Coffee order = new BasicCoffee();
            order = new MilkDecorator(order);
            order = new SugarDecorator(order);
            printOrder("Customer-1", order);
        };

        Runnable customer2 = () -> {
            Coffee order = new BasicCoffee();
            order = new WhippedCreamDecorator(order);
            printOrder("Customer-2", order);
        };

        Runnable customer3 = () -> {
            Coffee order = new BasicCoffee();
            order = new MilkDecorator(order);
            order = new WhippedCreamDecorator(order);
            order = new SugarDecorator(order);
            printOrder("Customer-3", order);
        };

        Thread t1 = new Thread(customer1);
        Thread t2 = new Thread(customer2);
        Thread t3 = new Thread(customer3);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for all to finish
        t1.join();
        System.out.println("\n✅ T1 coffee orders processed concurrently.");
        t2.join();
        t3.join();

        System.out.println("\n✅ All coffee orders processed concurrently.");
    }

    private static void printOrder(String customerName, Coffee coffee) {
        System.out.println("[" + customerName + "] Order: " + coffee.getDescription() + " | Cost: $" + coffee.getCost());
    }
}
