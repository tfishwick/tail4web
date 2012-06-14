var template = "<div><div class='date'>{{mydate}}</div><div class='message'>{{message}}</div><div>";

function format(event) {
	var s = "";
	
	//$date = date('Y-m-d H:i:s', (isset($d['date']) && is_object($d['date'])) ? $d['date']->sec : -1);
    //$level = isset($d['level']) ? $d['level'] : '[null]';
	
	event.mydate = event.date['$date'];
	
    return Mustache.to_html(template, event);
    
	/*
	$date = date('Y-m-d H:i:s', (isset($d['date']) && is_object($d['date'])) ? $d['date']->sec : -1);
    $level = isset($d['level']) ? $d['level'] : '[null]';
    $ts = str_repeat(' ', strlen($date) + 7);
            $caller = '';
    if (isset($d['caller'])) {
            
        //$caller = $d['caller']['class'] . $d['caller']['type'] . $d['caller']['function'] . '()' . ':' . $d['caller']['line'];
    
        $caller = $d['caller']['file'] . ':' . $d['caller']['line'];
    }

            $level = str_pad(strtoupper($level), 5, ' ');
            if ($verbose) {
                    $props = array();
                    if ($d['context']) {
                            foreach($d['context'] as $key=>$val)
                                    $props[] = "$key=$val";
                    }
                    $context = implode(',', $props);
                    print "$date $level $caller $context\n";
                    print "$ts$d[message]\n";
            } else {
            print "$date $level $caller $d[message]\n";
            }
    if (isset($d['exception']) && $d['exception']) {
        print "$ts" . $d['exception']['message'] . "\n";
        print $ts . "thrown from " . $d['exception']['file'] . ' (' . $d['exception']['line'] . "):\n";
        for ($i=0;$i < count($d['exception']['stackTrace']); $i++) {
            print "$ts " . $d['exception']['stackTrace'][$i] . "\n";
        }

    }
    */
}