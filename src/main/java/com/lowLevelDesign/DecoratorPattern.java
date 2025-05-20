package main.java.com.lowLevelDesign;

interface Coffee {
    double getCost();
    String getDescription();
}

class BasicCoffee implements Coffee {
    public double getCost() {
        return 5.0;
    }

    public String getDescription() {
        return "Plain Coffee";
    }
}

abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    public double getCost() {
        return coffee.getCost();
    }

    public String getDescription() {
        return coffee.getDescription();
    }
}

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    public double getCost() {
        return super.getCost() + 1.5;
    }

    public String getDescription() {
        return super.getDescription() + ", Milk";
    }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    public double getCost() {
        return super.getCost() + 0.5;
    }

    public String getDescription() {
        return super.getDescription() + ", Sugar";
    }
}

class WhippedCreamDecorator extends CoffeeDecorator {
    public WhippedCreamDecorator(Coffee coffee) {
        super(coffee);
    }

    public double getCost() {
        return super.getCost() + 2.0;
    }

    public String getDescription() {
        return super.getDescription() + ", Whipped Cream";
    }
}

public class DecoratorPattern {
    public static void main(String[] args) {
        Coffee basicCoffee = new BasicCoffee();

        // Decorate coffee with Milk
        Coffee milkCoffee = new MilkDecorator(basicCoffee);

        // Add Sugar
        Coffee milkSugarCoffee = new SugarDecorator(milkCoffee);

        // Add Whipped Cream
        Coffee fancyCoffee = new WhippedCreamDecorator(milkSugarCoffee);

        System.out.println("Description: " + fancyCoffee.getDescription());
        System.out.println("Total Cost: $" + fancyCoffee.getCost());
    }
}


