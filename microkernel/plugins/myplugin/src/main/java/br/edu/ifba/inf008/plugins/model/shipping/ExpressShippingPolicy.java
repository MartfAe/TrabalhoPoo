package br.edu.ifba.inf008.plugins.model.shipping;


import br.edu.ifba.inf008.plugins.domain.Order;
import br.edu.ifba.inf008.plugins.domain.OrderItem;
public class ExpressShippingPolicy implements ShippingPolicy{

    private static final double BASE_FEE = 25.00;
    private static final double PER_ITEM_FEE = 2.00;

    @Override
    public double calculateShippingCost(Order order){
        if(order == null){
            throw new IllegalArgumentException("Order cannot be null.");

        }

        int totalItemsQuantity = 0;
        for(OrderItem item : order.getItems()){
            totalItemsQuantity += item.getQuantity();
        }
        double finalShhippingCost = BASE_FEE + (totalItemsQuantity * PER_ITEM_FEE);
        System.out.println("Express shipping fee applied: " + finalShhippingCost);
        return finalShhippingCost;
    }
}
