package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
/*
1.2.5 Вариант реализации:
из сервлета преобразуете еду в List<MealTo>;
кладете список в запрос (request.setAttribute);
делаете forward на jsp для отрисовки таблицы (при redirect атрибуты теряются).
в JSP для цикла можно использовать JSTL tag forEach. Для подключения JSTL в pom.xml и шапку JSP нужно добавить:
 */

public class MealServlet extends HttpServlet {
	private static final Logger log = getLogger(UserServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("redirect to meals");
		//request.setAttribute("name", "BorrisStar");
		List<Meal> listOfMeals = MealsUtil.getMealsList();//Получить список еды
		List<MealTo> mealsFiltered = MealsUtil
				.getFilteredWithExcess(listOfMeals, LocalTime.of(7, 0), LocalTime.of(22, 0), 2000);
		request.setAttribute("mealsFiltered", mealsFiltered);
				request.getRequestDispatcher("/meals.jsp").forward(request, response);
		//response.sendRedirect("meals.jsp");
	}
}
