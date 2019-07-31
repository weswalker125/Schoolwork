<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            <li>msg1 = <%= request.getParameter("msg1") %></li>
            <li>msg2 = <%= request.getParameter("msg2") %></li>
            <li>msg3 = <%= request.getParameter("msg3") %></li>
            <li>msg4 = <%= request.getParameter("msg4") %></li>
            <li>msg5 = <%= request.getParameter("msg5") %></li>
            <li>CustomText = <%= request.getParameter("CustomText") %></li>
            <li>BillingFirstName = <%= request.getParameter("BillingFirstName") %></li>
            <li>BillingLastName = <%= request.getParameter("BillingLastName") %></li>
            <li>BillingStreet = <%= request.getParameter("BillingStreet") %></li>
            <li>BillingCity = <%= request.getParameter("BillingCity") %></li>
            <li>BillingState = <%= request.getParameter("BillingState") %></li>
            <li>BillingZip = <%= request.getParameter("BillingZip") %></li>
            <li>BillingPhone = <%= request.getParameter("BillingPhone") %></li>
            <li>SameAddress = <%= request.getParameter("SameAddress") %></li>
            <li>DeliveryFirstName = <%= request.getParameter("DeliveryFirstName") %></li>
            <li>DeliveryLastName = <%= request.getParameter("DeliveryLastName") %></li>
            <li>DeliveryStreet = <%= request.getParameter("DeliveryStreet") %></li>
            <li>DeliveryCity = <%= request.getParameter("DeliveryCity") %></li>
            <li>DilveryState = <%= request.getParameter("DilveryState") %></li>
            <li>DeliveryZip = <%= request.getParameter("DeliveryZip") %></li>
            <li>DeliveryPhone = <%= request.getParameter("DeliveryPhone") %></li>
            <li>delivery_month = <%= request.getParameter("delivery_month") %></li>
            <li>delivery_day = <%= request.getParameter("delivery_day") %></li>
            <li>delivery_year = <%= request.getParameter("delivery_year") %></li>
            <li>PaymentType = <%= request.getParameter("PaymentType") %></li>
            <li>CardNumber = <%= request.getParameter("CardNumber") %></li>
            <li>ExpMonth = <%= request.getParameter("ExpMonth") %></li>
            <li>ExpYear = <%= request.getParameter("ExpYear") %></li>
            <li>CVVValue = <%= request.getParameter("CVVValue") %></li>
            <li>NewUsername = <%= request.getParameter("NewUsername") %></li>
            <li>Password1 = <%= request.getParameter("Password1") %></li>
            <li>Password2 = <%= request.getParameter("Password2") %></li>
         </ol>
      </fieldset>
   </article>
</body>
</html>