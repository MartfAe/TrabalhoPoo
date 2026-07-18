package br.edu.ifba.inf008.plugins.model.payment;

public class CreditCardPayment implements Payable {
    private String cardNumber;
    private String cardHolderName;
    private String cvv;

    public CreditCardPayment(String cardNumber, String cardHolderName, String cvv){
        if(cardNumber == null || cardHolderName == null || cvv == null){
            throw new IllegalArgumentException("Card number, card holder name, and CVV cannot be null.");
        }
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cvv = cvv;
    }

    @Override
    public boolean processPayment(double amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        System.out.println("Processing credit card payment of amount $: " + amount);
        if(cardNumber.startsWith("0000")){
            System.out.println("Payment failed: Invalid card number.");
            return false;
        }
        System.out.println("Payment processed successfully.");
        return true;
    }
}
