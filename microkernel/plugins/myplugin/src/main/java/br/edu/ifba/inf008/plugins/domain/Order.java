package br.edu.ifba.inf008.plugins.domain;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.inf008.plugins.exceptions.InvalidPaymentException;
import br.edu.ifba.inf008.plugins.model.discount.DiscountPolicy;
import br.edu.ifba.inf008.plugins.model.payment.Payable;
import br.edu.ifba.inf008.plugins.model.shipping.ShippingPolicy;

public class Order {

    public enum OrderStatus{
        PENDING, PAID, CANCELLED, INVALID_PAYMENT
    }

    private final List<OrderItem> items;
    private OrderStatus status;

    private DiscountPolicy discountPolicy;
    private ShippingPolicy shippingPolicy;
    private Payable paymentMethod;

    public Order(Cart cart){
        if(cart == null || cart.getItems().isEmpty()){
            throw new IllegalArgumentException("Cannot create an order from an empty cart");
        }

        this.items = new ArrayList<>(cart.getItems());
        this.status = OrderStatus.PENDING;

    }

    public void setDiscountPolicy(DiscountPolicy discountPolicy){
        this.discountPolicy = discountPolicy;
    }

    public void setShippingPolicy(ShippingPolicy shippingPolicy){
        this.shippingPolicy = shippingPolicy;
    }

    public void setPaymentMethod(Payable paymentMethode){
        this.paymentMethod = paymentMethode;
    }

    public List<OrderItem> getItems(){
        return items;
    }


    public OrderStatus getStatus(){
        return status;
    }



    //Regras de negócio e cálculos


    public double calculateSubtotal(){
        double subtotal = 0.0;
        for(OrderItem item:items){
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }


    public double calculateTotal(){
        double subtotal = calculateSubtotal();

        //Não existindo política de desconto aplicada
        double discount = (discountPolicy != null) ? discountPolicy.calculateDiscount(subtotal) : 0.0;

        // Não existindo política de frete selecionada
        double shippingCost = (shippingPolicy != null) ?shippingPolicy.calculateShippingCost(this) : 0.0;

        double total = subtotal - discount +shippingCost;
        return Math.max(0.0, total);
    }


    //Processa o pagamenot e atualiza o estoque

    public void processOrderPayment() throws InvalidPaymentException{
        if(paymentMethod == null){
            throw new IllegalStateException("Payment method must be selected before processing.");

        }

        double finalAmount = calculateTotal();

        //Execução da validação polimórfica de pagamento 
        boolean paymentApproved = paymentMethod.processPayment(finalAmount);

        if(paymentApproved){
            this.status = OrderStatus.PAID;
            for(OrderItem item : items){
                item.getProduct().reduceStock(item.getQuantity());
            }

        }else{
            this.status = OrderStatus.INVALID_PAYMENT;
            throw new InvalidPaymentException("Payment processing failed or was declined");
        }
    }


}
