package ru.stk.eshop.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stk.eshop.services.CartService;

@Aspect
@Component
public class AopLogging {
    private static final Logger logger = LoggerFactory.getLogger(AopLogging.class);
    private CartService cartService;

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Before("execution(public void ru.stk.eshop.services.CartService.addById(..))")
    public void beforeAddByIdInCartServiceClass() {
        //System.out.println("!!!!!!!!!!!AOP: Поймали добавление товара");
        logger.info("В корзину добавляется новый товар!");
    }

    @After("execution(public void ru.stk.eshop.services.CartService.addById(..))")
    public void afterAddByIdInCartServiceClass() {
        logger.info("В корзину успешно добавлен новый товар!");
        // пересчитываем стоимость крзины
        cartService.recalculate();
    }


    /*    @Around("execution(public * ru.stk.eshop.services.CartService.*(..))")
    public void methodProfiling(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("start profiling");
        long begin = System.currentTimeMillis();
        proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        long duration = end - begin;
        System.out.println((MethodSignature) proceedingJoinPoint.getSignature() + " duration: " + duration);
        System.out.println("end profiling");
    }*/
}



