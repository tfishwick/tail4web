<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<style>

	</style>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js"></script>
	
	</script>
</head>
<body>

<h2>Event Collections</h2>

<ul>
	<c:forEach items="${collections}" var="coll">
		<li><a href="/${coll}">${coll}</a><br>
	
	</c:forEach>
</ul>


</body>
</html>
