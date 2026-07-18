package br.edu.ifba.inf008.plugins.domain;
import br.edu.ifba.inf008.plugins.exceptions.InsufficientStockException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
public class Cart {

    private List<OrderItem> items;

    public Cart(){
        this.items = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) throws br.edu.ifba.inf008.plugins.exceptions.InsufficientStockException{
        if(product == null){
            throw new IllegalArgumentException("Product cannot be null");
        }

        if(quantity > product.getStockQuantity()){
            throw new br.edu.ifba.inf008.plugins.exceptions.InsufficientStockException("Insufficient stock for product: "+product.getName());
        }

        this.items.add(new OrderItem(product, quantity));
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
