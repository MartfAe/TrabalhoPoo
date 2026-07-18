package br.edu.ifba.inf008.plugins.model.payment;

public class BoletoPayment implements Payable{

    @Override
    public boolean processPayment(double amount){
        if(amount<=0){
            return false;
        }

        System.out.println("Processing bank slip for amount: $" + amount);
        System.out.println("Slip payment processed successfully.");
        return true;
    }

}
