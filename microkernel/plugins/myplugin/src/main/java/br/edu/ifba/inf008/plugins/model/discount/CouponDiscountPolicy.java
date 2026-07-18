package br.edu.ifba.inf008.plugins.model.discount;

public class CouponDiscountPolicy implements DiscountPolicy{
    private double couponValue;

    public CouponDiscountPolicy(double couponValue){
        if(couponValue < 0){
            throw new IllegalArgumentException("Coupon value cannot be negative.");
        }
        this.couponValue = couponValue;
    }

    @Override
    public double calculateDiscount(double subtotal){
        if(subtotal < 0){
            throw new IllegalArgumentException("Subtotal cannot be negative.");
        }else if(couponValue > subtotal){
            throw new IllegalArgumentException("Coupon value cannot exceed subtotal.");
        }
        return Math.min(couponValue, subtotal);
    }
}