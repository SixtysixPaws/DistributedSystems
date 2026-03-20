package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.*;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }



    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }

    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    // === 新增：根据名称查询 (建议路径与 ID 查询区分开) ===

    @GetMapping("/restrpc/meals/search/{name}")
    Meal getMealByName(@PathVariable String name) {
        return mealsRepository.findMealByName(name)
                .orElseThrow(() -> new MealNotFoundException(name));
    }

    // === 新增：订单处理 (保持 /restrpc 前缀) ===

    @PostMapping("/restrpc/orders")
    public OrderConfirmation addOrder(@RequestBody Order order) {
        return mealsRepository.processOrder(order);
    }

    @GetMapping("/restrpc/meals/cheapest")
    public Meal getCheapestMeal() {
        return mealsRepository.findCheapestMeal();
    }

    @GetMapping("/restrpc/meals/largest")
    public Meal getLargestMeal() {
        return mealsRepository.findLargestMeal();
    }

    // 在 MealsRestRpcStyleController 中继续添加

    @PostMapping("/restrpc/meals")
    public void addMeal(@RequestBody Meal meal) {
        mealsRepository.addMeal(meal);
    }

    @PutMapping("/restrpc/meals/{id}")
    public void updateMeal(@PathVariable String id, @RequestBody Meal meal) {
        mealsRepository.updateMeal(id, meal);
    }

    @DeleteMapping("/restrpc/meals/{id}")
    public void deleteMeal(@PathVariable String id) {
        mealsRepository.deleteMeal(id);
    }


}
