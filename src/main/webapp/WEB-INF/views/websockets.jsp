<!DOCTYPE html>
<html>
<head>
	<style>

	</style>
	<script>
		var sock;
		function init() {
			console.log("new websocket()");
			sock = new WebSocket('ws://home.tef.ca:4500/status');
			console.log(sock);
			sock.onmessage = function(e) {
				console.log('message');
			};
			sock.onopen = function() {
				console.log("opened");
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
			console.log("sending message");
			sock.send(new Date().getTime() + "\r\n");
		}
	</script>
</head>
<body>

<button onclick="send()">Send Message</button>

<div id='console' style='height: 300px; position: absolute; bottom: 0px; right: 0px; left: 0px; margin: 10px;
	border: 2px solid #aaa; padding: 4px; font-family: courier'>
	
</div>

</body>
</html>
