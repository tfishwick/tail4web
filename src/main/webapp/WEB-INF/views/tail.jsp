<!DOCTYPE html>
<html>
<head>
	<style>

	</style>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js"></script>
	<script src="${page.context}/js/mustache.js"></script>
	<script src="${page.context}/js/tail4web.js"></script>
	<script>
		var sock;
		function init() {
			console.log(window.location);
			console.log("new websocket()");
			sock = new WebSocket('ws://home.tef.ca:8080/socket/test');
			console.log(sock);
			var scrollTimeout = null;
			sock.onmessage = function(e) {
				console.log('< ' + e.data);
				var doc = JSON.parse(e.data);
				
				
				//$('#console').append("<div><span>" + doc.date['$date'] + "</span>" + doc.message + "</div>");
				
				$('#console').append(format(doc));
				
				
				// Schedule a timer to scroll to the bottom in 10ms. If there is an existing timer, clear it.
				if (scrollTimeout) {
					window.clearTimeout(scrollTimeout);
					console.log("Cleared scrollTimeout: " + scrollTimeout);
					scrollTimeout = null;
				}
				
				scrollTimeout = window.setTimeout(function() {
					console.log("Scrolling to bottom.");
					var top = document.getElementById('console').scrollHeight;
					$("#center").animate({ scrollTop: top }, 2000);
				}, 100);
				console.log("bufferredAmount = " + sock.bufferedAmount);
			};
			sock.onopen = function() {
				var collection = window.location.pathname;
				collection = collection.replace("/", "");
				var tailCommand = {
					method: "tail",
					params: {
						collectionName: collection
					}
				};
				sock.send(JSON.stringify(tailCommand));
			};
			sock.onerror = function() {
				console.log("error");	
			}
			sock.onclose = function() {
				console.log("closed");	
			}
			//sock.send("hellasdfo\r\n");
			//sock.close();
		}
	
		window.addEventListener('load', init);
		
		function log(s) {
			
		}
		
		function send() {
			var j = {
				name: "tom\ntom"
			};
		
			console.log("sending message");
			sock.send("asdf\nasdf");
		}
	</script>
	<style>
		html,body { height: 100%; }
		* { padding: 0px; margin: 0px; }
		
		body {
			-webkit-box-orient: vertical;
			display: -webkit-box;
		}
		
		#north {
			-webkit-box-flex: 0;
			padding: 10px;
			border-bottom: 2px solid #bbb;
		}
		
		#center {
			-webkit-box-flex: 1;
			overflow: auto;
		}
		
		#console > div {
			font-family: Courier New, monospace;
			font-size: 12px;
			border-bottom: 1px solid #eee;
			margin: 2px;
		}
		
		#console ul {
			
		}
	</style>
</head>
<body>

<div id="north">
	${collection.name}
</div>

<div id="center">
		<div id="console" style="-webkit-box-flex: 1;">
		</div>
</div>




</body>
</html>
