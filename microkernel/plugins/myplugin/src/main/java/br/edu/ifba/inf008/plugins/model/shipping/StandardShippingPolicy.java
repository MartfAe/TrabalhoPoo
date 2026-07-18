package br.edu.ifba.inf008.plugins.model.shipping;

import br.edu.ifba.inf008.plugins.domain.Order;
public class StandardShippingPolicy implements ShippingPolicy{
    private static final double  BASE_FEE = 10.00;
    private static final double FREE_SHIPPING_THRESHOLD = 100.00;

    @Override
    public double calculateShippingCost(Order order){
        if(order == null){
            throw new IllegalArgumentException("Order cannot be null.");
        }

        if(order.calculateSubtotal() >= FREE_SHIPPING_THRESHOLD){
            System.out.println("Free shipping applied.");
            return 0.0;
        }

        System.out.println("Standard shipping fee applied: " + BASE_FEE);
        return BASE_FEE;
    }
    
}
