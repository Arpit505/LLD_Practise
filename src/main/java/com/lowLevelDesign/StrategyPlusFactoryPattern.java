package main.java.com.lowLevelDesign;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

interface PaymentStrategy {
     void pay(double amount);
}

 class CreditCardPayment implements PaymentStrategy {

    private final String cardNumber;

    public CreditCardPayment(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }

    public void pay(double amount)
    {
        System.out.println(amount + " payed through Credit Card");
    }
 }

 class PayPalPayment implements PaymentStrategy {

    private final String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    public void pay(double amount) {
        System.out.println(amount + " payed through paypal by " + email);
    }
 }

class UPIPayment implements PaymentStrategy {
    private final String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using UPI ID [" + upiId + "]");
    }
}

enum PaymentType {
    CREDIT_CARD,
    PAYPAL,
    UPI
}

class paymentFactory {
//    Function<String, PaymentStrategy> CreditCardPayment = null;
    private static final Map<PaymentType, Function<String,PaymentStrategy>> factory_map =
            Map.of(
            PaymentType.CREDIT_CARD, CreditCardPayment::new,
            PaymentType.PAYPAL, PayPalPayment::new,
            PaymentType.UPI, UPIPayment::new
    );

    public static PaymentStrategy getStrategy(PaymentType strtgy, String data) {
        Function<String, PaymentStrategy> creator = factory_map.get(strtgy);
        if(creator == null)
        {
            throw new IllegalArgumentException("No Proper Payment Type " + creator);
        }
        return creator.apply(data);
    }
}

class PaymentService {
    public void pay(PaymentStrategy strategy, double amount) {
        if(strategy == null)
            System.out.println("Wrong Payment method");
        else
            strategy.pay(amount);
    }
}


public class StrategyPlusFactoryPattern {
    public static void main(String[] args)
    {
        PaymentService paymentService = new PaymentService();

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> {
            PaymentStrategy strategy = paymentFactory.getStrategy(
                    PaymentType.CREDIT_CARD, "4111-1111-1111-1111");
            paymentService.pay(strategy, 120.0);
        });

        executor.submit(() -> {
            PaymentStrategy strategy = paymentFactory.getStrategy(
                    PaymentType.PAYPAL, "alice@example.com");
            paymentService.pay(strategy, 85.5);
        });

        executor.submit(() -> {
            PaymentStrategy strategy = paymentFactory.getStrategy(
                    PaymentType.UPI, "bob@upi");
            paymentService.pay(strategy, 42.75);
        });

        executor.shutdown();
        
        System.out.println("Payment done.");
    }
}
