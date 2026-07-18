package br.edu.ifba.inf008.plugins.model.payment;

public interface Payable {
    boolean processPayment(double amount);
}
