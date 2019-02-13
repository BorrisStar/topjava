<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>Meal</title>
    <link rel='stylesheet' href='css/bootstrap.min.css' type='text/css' >
</head>
<body>
<h3><a href="index.html">Home</a></h3>

<h2>Meal</h2>

<!-- Таблица -->
<table class="table table-hover table-striped table-bordered">

    <tr>
        <th>Description</th>
        <th>Calories</th>
        <th>Date</th>
    </tr>

    <tbody>
    <c:forEach var="mealTo" items="${pageContext.request.getAttribute('mealsFiltered')}">
        <c:set var="color" value="${mealTo.excess ? 'red': 'green'}"/>
        <tr style="color: ${color}">
            <td>${fn:replace(mealTo.dateTime,"T"," ")}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>