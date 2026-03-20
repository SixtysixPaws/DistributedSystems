package com.example.springsoap;

import javax.annotation.PostConstruct;
import java.util.*;


import io.foodmenu.gt.webservice.*;


import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MealRepository {
    private static final Map<String, Meal> meals = new HashMap<String, Meal>();

    @PostConstruct
    public void initData() {

        Meal a = new Meal();
        a.setName("Steak");
        a.setDescription("Steak with fries");
        a.setMealtype(Mealtype.MEAT);
        a.setKcal(1100);
        a.setPrice(25.50);


        meals.put(a.getName(), a);

        Meal b = new Meal();
        b.setName("Portobello");
        b.setDescription("Portobello Mushroom Burger");
        b.setMealtype(Mealtype.VEGAN);
        b.setKcal(637);
        b.setPrice(15.0);

        meals.put(b.getName(), b);

        Meal c = new Meal();
        c.setName("Fish and Chips");
        c.setDescription("Fried fish with chips");
        c.setMealtype(Mealtype.FISH);
        c.setKcal(950);
        c.setPrice(22.0);


        meals.put(c.getName(), c);
    }

    public Meal findMeal(String name) {
        Assert.notNull(name, "The meal's code must not be null");
        return meals.get(name);
    }

    public Meal findBiggestMeal() {

        if (meals == null) return null;
        if (meals.size() == 0) return null;

        var values = meals.values();
        return values.stream().max(Comparator.comparing(Meal::getKcal)).orElseThrow(NoSuchElementException::new);

    }

    public Meal findCheapestMeal() {
        return meals.values().stream()
                .min(java.util.Comparator.comparing(Meal::getPrice))
                .orElseThrow(java.util.NoSuchElementException::new);
    }

    // 在类中添加处理订单的方法
    public OrderConfirmation processOrder(Order order) {
        OrderConfirmation confirmation = new OrderConfirmation();
        // 使用 UUID 生成一个唯一的订单号
        confirmation.setId(java.util.UUID.randomUUID().toString());
        confirmation.setStatus("Order received for address: " + order.getAddress());
        return confirmation;
    }

    public List<Meal> findByMealType(Mealtype type) {
        return meals.values().stream()
                .filter(meal -> meal.getMealtype().equals(type))
                .collect(java.util.stream.Collectors.toList());
    }
}