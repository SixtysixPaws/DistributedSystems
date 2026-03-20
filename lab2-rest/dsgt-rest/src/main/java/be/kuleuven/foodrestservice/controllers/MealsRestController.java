package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.*;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class MealsRestController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/rest/meals/{id}")
    EntityModel<Meal> getMealById(@PathVariable String id) {
        Meal meal = mealsRepository.findMeal(id).orElseThrow(() -> new MealNotFoundException(id));

        return mealToEntityModel(id, meal);
    }

    @GetMapping("/rest/meals")
    CollectionModel<EntityModel<Meal>> getMeals() {
        Collection<Meal> meals = mealsRepository.getAllMeal();

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : meals) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).getMeals()).withSelfRel());
    }

    private EntityModel<Meal> mealToEntityModel(String id, Meal meal) {
        return EntityModel.of(meal,
                linkTo(methodOn(MealsRestController.class).getMealById(id)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("rest/meals"));
    }

    @GetMapping("/rest/meals/cheapest")
    public EntityModel<Meal> getCheapestMeal() {
        Meal meal = mealsRepository.findCheapestMeal();
        return mealToEntityModel(meal.getId(), meal);
    }

    @GetMapping("/rest/meals/largest")
    public EntityModel<Meal> getLargestMeal() {
        Meal meal = mealsRepository.findLargestMeal();
        return mealToEntityModel(meal.getId(), meal);
    }

    @PostMapping("/rest/meals")
    public EntityModel<Meal> addMeal(@RequestBody Meal meal) {
        mealsRepository.addMeal(meal);
        return mealToEntityModel(meal.getId(), meal);
    }

    @PutMapping("/rest/meals/{id}")
    public EntityModel<Meal> updateMeal(@PathVariable String id, @RequestBody Meal meal) {
        mealsRepository.updateMeal(id, meal);
        return mealToEntityModel(id, meal);
    }

    @DeleteMapping("/rest/meals/{id}")
    public void deleteMeal(@PathVariable String id) {
        mealsRepository.deleteMeal(id);
    }

    @PostMapping("/rest/orders")
    public EntityModel<OrderConfirmation> addOrder(@RequestBody Order order) {
        OrderConfirmation confirmation = mealsRepository.processOrder(order);
        return EntityModel.of(confirmation,
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("all_meals"));
    }
}
