package com.hostel.foodmgmt.repository;

import com.hostel.foodmgmt.model.MealPreparation;
import com.hostel.foodmgmt.model.MealType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MealPreparationRepository extends MongoRepository<MealPreparation, String> {
    Optional<MealPreparation> findByDateAndMealType(LocalDate date, MealType mealType);
}
