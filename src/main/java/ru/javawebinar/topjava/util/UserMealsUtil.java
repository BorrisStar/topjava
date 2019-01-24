package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserMealsUtil {
	public static void main(String[] args) {
		List<UserMeal> mealList = Arrays.asList(
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
		);
		List<UserMealWithExceed> userMealWithExceeds = new ArrayList<UserMealWithExceed>();
		userMealWithExceeds.addAll(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
		for (UserMealWithExceed userMealWithExceed : userMealWithExceeds) {
			System.out.println(userMealWithExceed.toString());
		}
	}

	/*-  должны возвращаться только записи между startTime и endTime
	 -  поле UserMealWithExceed.exceed должно показывать, превышает ли сумма калорий за весь день параметра метода caloriesPerDay
	*/
	public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {


		//List of UserMealWithExceed's for output
		List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
		//List of UserMeal's for Breakfast+Lunch+Dinner,
		List<UserMeal> mealListOfDay = new ArrayList<>();

		int i = 0;
		boolean computeNow = false;
		boolean finalElementAlone = false;

		//Take current userMeal from list
		for (UserMeal userMeal : mealList) {
			//Take current Date
			LocalDate dateFromDateTime = userMeal.getDateTime().toLocalDate();

			//System.out.println(userMeal.getCalories());
			//System.out.println(dateFromDateTime.toString());

			if (mealListOfDay.isEmpty()) {
				mealListOfDay.add(userMeal);
				i++;
				continue;
			}

			//Если дата текущая совпадает с предыдущим
			if (dateFromDateTime.equals(mealListOfDay.get(i - 1).getDateTime().toLocalDate())) {
				//It's current day's Breakfast,Lunch or Dinner
				mealListOfDay.add(userMeal);
				i++;
				computeNow = false;
				finalElementAlone = false;

			} else {
				computeNow = true;
				finalElementAlone = true;//Maybe
			}

			//Если элемент последний, то рассчитать в любом случае  и
			if (userMeal.equals(mealList.get(mealList.size() - 1))) {
				computeNow = true;
			}
//Повести расчет суммы, сравнить с заданными калориями и если сумма больше и время подходящее - занести в выходной список
			if (computeNow) { //It's a new day
				int caloriesSumTemp = 0;
				//Count Sum of calories for Day
				for (UserMeal userMealTemp : mealListOfDay) {
					caloriesSumTemp += userMealTemp.getCalories();
					//System.out.println(caloriesSumTemp);
				}

				if (caloriesSumTemp > caloriesPerDay) {    //if calories sum more then is available per Day
					for (UserMeal userMealTemp : mealListOfDay) {
						LocalTime timeFromDateTime = userMealTemp.getDateTime().toLocalTime();
						if (TimeUtil.isBetween(timeFromDateTime, startTime, endTime)) { //Check startTime>Time>endTime
							userMealWithExceeds.add(new UserMealWithExceed(userMealTemp.getDateTime(), userMealTemp.getDescription(), userMealTemp.getCalories(), true));
							//System.out.println(userMealWithExceeds.get(0).toString());
						}
					}
				}

				mealListOfDay.clear();
				i = 0;

				mealListOfDay.add(userMeal);
				i++;
			}
			//Если последний элемент один
			if (computeNow && finalElementAlone) {
				if (userMeal.getCalories() > caloriesPerDay) {    //if calories sum more then is available per Day

					LocalTime timeFromDateTime = userMeal.getDateTime().toLocalTime();
					if (TimeUtil.isBetween(timeFromDateTime, startTime, endTime)) {//Check startTime>Time>endTime
						userMealWithExceeds.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
						//System.out.println(userMealWithExceeds.get(0).toString());
					}

				}
			}

		}
		return userMealWithExceeds;
	}
}
