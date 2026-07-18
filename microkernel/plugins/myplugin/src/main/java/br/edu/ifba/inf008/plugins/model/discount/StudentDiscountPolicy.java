package br.edu.ifba.inf008.plugins.model.discount;

public class StudentDiscountPolicy implements DiscountPolicy{
    private static final double STUDENT_DISCOUNT_RATE = 0.15;

    @Override
    public double calculateDiscount(double subtotal){
        return subtotal * STUDENT_DISCOUNT_RATE;
    }
}