package br.edu.ifba.inf008.plugins.model.payment;

public class PixPayment implements Payable{

    @Override
    public boolean processPayment(double amount){
        if(amount<=0){
            return false;
        }

        System.out.println("Processing Pix payment of amount: " + amount);
        System.out.println("Pix payment processed successfully.");
        return true;
    }

}
