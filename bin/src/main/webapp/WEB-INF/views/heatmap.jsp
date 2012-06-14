<!DOCTYPE html>
<html lang='en'>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1.0" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	
	<style>
		
		body,html {
			margin: 0px;
			padding: 0px;
			height: 100%;
		}
		
		.heatmap {
			border-collapse: collapse;
			height: 100%;
			width: 100%;
			display: none;
		}
		
		.heatmap td {
			background: red;
			
		}
		
		.hm {
			height: 100%;
			border: 5px solid black;
			-webkit-box-orient: vertical;
			display: -webkit-box;
		}
		
		.q {
			text-align: center;
			
			-webkit-box-flex: 1;
			border: 1px solid black;
		}
	</style>
	<script>
		function init() {
			/Mobile/.test(navigator.userAgent) && !location.hash && setTimeout(function () {
			    if (!pageYOffset) window.scrollTo(0, 1);
			}, 1000);
		}
	</script>
</head>
<body>
	<div class='hm'>
		<div style='background: red' class='q'>34.54</div>
		
		<div style='background: red' class='q'>34.54</div>
		
		<div style='background: red' class='q'>34.54</div>
	</div>
	
	<table class='heatmap'>
		<tr><td>34.65</td></tr>
	</table>

</body>
</html>