package br.edu.ifba.inf008.plugins.model.discount;

public interface DiscountPolicy {
    double calculateDiscount(double subtotal);
}