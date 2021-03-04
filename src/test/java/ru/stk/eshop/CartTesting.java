package ru.stk.eshop;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import ru.stk.eshop.entities.Product;
import ru.stk.eshop.services.CartService;
import ru.stk.eshop.services.ProductService;

import java.math.BigDecimal;

public class CartTesting {
    CartService cartService;
    ProductService ps = new ProductService();
    Product p;

    @Before
    public void init(){
        cartService = new CartService();

        ProductService ps = new ProductService();
        p = new Product();
        p.setId(0L);
        p.setName("TestProduct");
        p.setPrice(new BigDecimal(100));

    }

    @Test
    public void addProductTest(){
        cartService.add(p);
        assertEquals((Integer)1, cartService.getTotalCartQuantity());
        assertEquals((Long)0L, cartService.getItems().get(0).getProduct().getId());
        assertEquals("Names equals", "TestProduct", cartService.getItems().get(0).getProduct().getName());
    }


    @Test
    public void  deleteProductTest(){
        cartService.add(p);
        assertEquals((Integer)1, cartService.getTotalCartQuantity());
        cartService.remove(p);
        assertEquals((Integer)0, cartService.getTotalCartQuantity());

    }
}
