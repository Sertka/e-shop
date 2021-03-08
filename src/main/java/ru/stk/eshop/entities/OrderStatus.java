package ru.stk.eshop.entities;

/**
 * OrderStatus assigned as 'Confirmed' for the created order and then
 * has to be changed by manager
 */
public enum OrderStatus {
    CONFIRMED("размещен"),
    SENT("отправлен"),
    RECEIVED("получен");

    public final String displayName;

    private OrderStatus(String displayName){
        this.displayName = displayName;
    }
}

