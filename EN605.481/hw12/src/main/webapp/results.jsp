<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
   <meta charset="utf-8" />
   <meta name="viewport" content="width=page-width, initial-scale=1.0">
   <title>Happy Flowers - Order</title>
   <link rel="stylesheet" href="happy.css" />
   <link href='https://fonts.googleapis.com/css?family=Tangerine' rel='stylesheet' type='text/css'>
</head>

<body>
   <div class="container">
      <header>
         <h1>
            Happy Flowers Plus
         </h1>
      </header>

      <nav>
         <ul>
            <li><a href="#">Floral Arrangements</a></li>
            <li><a href="#">Seasonal Bouquets</a></li>
            <li><a href="#">Live Plants</a></li>
            <li><a href="#">Shop by Price</a></li>
         </ul>
      </nav>
   </div>

   <article>
      <h2>Your form has been submitted</h2>
      <div id="errorText"></div>
      <fieldset id="results" class="text">
         <legend>You entered the following data:</legend>
         <ol style="margin-top: 50px;">
            <!-- Iterate through each item and display as list item -->
            <c:forEach items="${processedDataArray}" var="pair">
               <li><c:out value="${pair}"/></li>
            </c:forEach>
         </ol>
      </fieldset>
   </article>
</body>
</html>