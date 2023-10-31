<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <title>JSP Page</title>
</head>
<body>
<H1>Operations analytics</H1>
<H3>Top 1 IP search terms:  <%= request.getAttribute("IP")%> </H3>
<H3>Total time spend on getting data from Mongo:  <%= request.getAttribute("time")%> millisec </H3>
<H3>Count number of each timezone:  <%= request.getAttribute("timezone")%>  </H3>
<table class="w3-table w3-striped">
    <tr>
        <th>timeStamp</th>
        <th>ip</th>
        <th>city</th>
        <th>region</th>
        <th>country</th>
        <th>loc</th>
        <th>postal</th>
        <th>timezone</th>

    </tr>
    <%= request.getAttribute("parsedData")%>

</table>

</body>
</html>



