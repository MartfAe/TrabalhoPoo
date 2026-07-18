package br.edu.ifba.inf008.plugins.domain;

public class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity){
        if(product == null){
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.product = product;
        setQuantity(quantity);
    }


    public Product getProduct(){
        return product;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        if(quantity <=0){
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.quantity = quantity;
    }

    public double getSubtotal(){
        return this.product.getUnitPrice() *this.quantity;
    }

    
}
