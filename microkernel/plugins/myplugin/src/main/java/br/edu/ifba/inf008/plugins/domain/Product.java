package br.edu.ifba.inf008.plugins.domain;

public class Product {

    private String name;
    private String code;
    private String description;
    private double unitPrice;
    private int stockQuantity;

    public Product(String name, String code, String description, double unitPrice, int stockQuantity) {
        this.name = name;
        this.code = code;
        this.description = description;
        setUnitPrice(unitPrice);
        setStockQuantity(stockQuantity);
    }


    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getCode(){
        return this.code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public double getUnitPrice(){
        return this.unitPrice;
    }

    // ADICIONADO 'final' PARA SUMIR O AVISO NO CONSTRUTOR
    public final void setUnitPrice(double unitPrice){
        if(unitPrice<0){
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.unitPrice = unitPrice;
    }

    public int getStockQuantity(){
        return this.stockQuantity;
    }

    // ADICIONADO 'final' PARA SUMIR O AVISO NO CONSTRUTOR
    public final void setStockQuantity(int stockQuantity){
        if(stockQuantity<0){
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.stockQuantity = stockQuantity;
    }


    public void reduceStock(int quantity){
        if(quantity<=0){
            throw new IllegalArgumentException("Quantity to reduce cannot be negative");
        }
        if(this.stockQuantity<quantity){
            throw new IllegalArgumentException("Insufficient stock for product: "+this.name);
        }
        this.stockQuantity -=quantity;
    }
}