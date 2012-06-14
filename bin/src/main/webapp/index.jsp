<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<script src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript">
        var config = {
            contextPath: '${pageContext.request.contextPath}'
        };
    </script>
</head>
<body>

	<p>contextPath=${pageContext.request.contextPath}  ${pageContext.response}</p>
	
	<p>pathInfo=${pageContext.request.session}</p>
	tefca-logging

</body>
</html>
