<!DOCTYPE html>
<html lang="en">
<head>
    <title>Charts</title>
    <link rel="stylesheet" type="text/css" href="/js/ext-4.0.7/resources/css/ext-all.css" />
    <style>
    	.x-border-layout-ct {
    		background: #fff;
    	}
    </style>
    <script type="text/javascript" src="/js/ext-4.0.7/bootstrap.js"></script>
    <script>
    var accountGuid = '';
    var contextPath = '${pageContext.request.contextPath}';
    
  	var portfolio = {
  		id: '${portfolio.id}',
  		name: '${portfolio.name}'
  	};
  	
  	var portfolio2 = {
 		id: 2,
 		name: 'test2'
 	};
  	
  	var portfolios = [portfolio, portfolio2];
    </script>
    <script type="text/javascript" src="/money/app.js"></script>
</head>
<body>
</body>
</html>
