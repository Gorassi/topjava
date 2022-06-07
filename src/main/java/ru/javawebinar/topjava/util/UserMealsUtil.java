package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        Set<LocalDate> dateSet = new HashSet<>();
        Set<LocalDate> dateSetWitExcess = new HashSet<>();
        for(UserMeal users : meals) dateSet.add(users.getDateTime().toLocalDate());
        int dateSetSize = dateSet.size();
        System.out.println("TODO return filtered list with excess. Implement by cycles");

        for(LocalDate date : dateSet) {
            int sum = 0;
            for(UserMeal userMeal : meals){
                if(userMeal.getDateTime().toLocalDate().isEqual(date)) sum += userMeal.getCalories();
                if(sum > caloriesPerDay) dateSetWitExcess.add(date);
            }
        }

        if(!dateSetWitExcess.isEmpty()) {
            List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
            for(LocalDate localDate: dateSetWitExcess){
                for(UserMeal userMeal : meals){
                    if(userMeal.getDateTime().toLocalDate().isEqual(localDate)
                    && userMeal.getDateTime().getHour() > startTime.getHour()
                    && userMeal.getDateTime().getHour() < endTime.getHour()) {
                        userMealWithExcesses.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                                userMeal.getCalories(), true));
                    }
                }
            }
            return userMealWithExcesses;
        }

        return null;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        System.out.println("TODO Implement by streams");
        Set<LocalDate> dateSet = new HashSet<>();
        Set<LocalDate> dateSetWitExcess = new HashSet<>();
        for(UserMeal users : meals) dateSet.add(users.getDateTime().toLocalDate());
        int dateSetSize = dateSet.size();
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();

        for(LocalDate localDate : dateSet) {

            int summa = meals.stream().reduce(0, (x,y) -> {
                if(y.getDateTime().toLocalDate().isEqual(localDate))
                    return x + y.getCalories();
                else
                    return x + 0;
            },
                    (x, y) -> x + y);
            System.out.println("summa = " + summa);
            if(summa > caloriesPerDay) dateSetWitExcess.add(localDate);
        }

        LocalDate localDate = LocalDate.of(2020, 1, 30);
//        Map<Boolean, List<UserMeal>> mapMeal = new HashMap<>();
//        mapMeal = meals.stream().collect((Collectors.partitioningBy((u)-> u.getDateTime().toLocalDate().isEqual(localDate))));
        Map<Boolean, List<UserMeal>> mapMeal = meals.stream().collect((Collectors.partitioningBy((u)-> u.getDateTime().toLocalDate().isEqual(localDate))));
        for(Map.Entry<Boolean, List<UserMeal>> entry : mapMeal.entrySet()){
            System.out.println("boolean = " + entry.getKey());
            for(UserMeal userMeal : entry.getValue()) System.out.println(userMeal.getDateTime() + " description = " + userMeal.getDescription() + " calories = " + userMeal.getCalories());
        }
        System.out.println("Attempts 2");
//        Map<LocalDate, List<UserMeal>> list2 = meals.stream().collect(Collectors.groupingBy((u)-> u.getDateTime().toLocalDate()));
        Map<LocalDate, Integer> list3 = meals.stream().collect(Collectors.groupingBy((u)-> u.getDateTime().toLocalDate(), Collectors.summingInt((u)-> u.getCalories())));

    //    for(Map.Entry<LocalDate, List<UserMeal>> entry : list2.entrySet()){
        for(Map.Entry<LocalDate, Integer> entry : list3.entrySet()){
//            Map<LocalDateTime, List<UserMeal>> list2 = meals.stream().collect(Collectors.groupingBy(UserMeal::getDateTime));
//        for(Map.Entry<LocalDateTime, List<UserMeal>> entry : list2.entrySet()){
            System.out.println("key = " + entry.getKey() + " value = " + entry.getValue());
   //         for(UserMeal userMeal : entry.getValue()) System.out.println(userMeal.getDateTime() + " description = " + userMeal.getDescription() + " calories = " + userMeal.getCalories());
        }
        return null;
    }
}
