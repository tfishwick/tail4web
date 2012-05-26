package ca.tef.money.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EmailParserController {

	@RequestMapping(value = "/parse-email", method = RequestMethod.POST)
	public void parseEmail() {

	}

}
