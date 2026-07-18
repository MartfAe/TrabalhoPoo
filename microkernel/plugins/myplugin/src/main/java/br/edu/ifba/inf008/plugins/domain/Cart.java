package br.edu.ifba.inf008.plugins.domain;
import br.edu.ifba.inf008.plugins.exceptions.InsufficientStockException;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<OrderItem> items;

    public Cart(){
        this.items = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) throws InsufficientStockException{
        if(product == null){
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        int existingQuantity = 0;
        OrderItem existingItem = null;

        for (OrderItem item : items) {
            if (item.getProduct().getName().equals(product.getName())) { 
                existingItem = item;
                existingQuantity = item.getQuantity();
                break;
            }
        }   

       if (existingQuantity + quantity > product.getStockQuantity()) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }
        if (existingItem != null) {
            existingItem.setQuantity(existingQuantity + quantity);
        } else {
            this.items.add(new OrderItem(product, quantity));
        }
    }

    public void removeItem(OrderItem item){
        this.items.remove(item);
    }

    public List<OrderItem> getItems(){
        return this.items;
    }

    public void clear(){
        this.items.clear();
    }


}
