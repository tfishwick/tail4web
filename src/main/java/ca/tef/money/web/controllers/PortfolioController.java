package ca.tef.money.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ca.tef.money.domain.Portfolio;
import ca.tef.tail4web.services.*;

@Controller
public class PortfolioController {

	// private static final Logger logger;

	@Autowired
	PortfolioService portfolioService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView createPortfolio(@RequestParam String name) {
		System.out.println("create " + name);

		Portfolio portfolio = new Portfolio();
		portfolio.setName(name);
		portfolioService.save(portfolio);

		ModelAndView model = new ModelAndView("redirect:" + portfolio.getName());

		// model.getModel().put("portfolio", "");

		// response.getResult().put("portfolio", portfolio);
		return model;
	}

	@RequestMapping(value = "heatmap.html", method = RequestMethod.GET)
	public ModelAndView showHeatmap() {
		ModelAndView model = new ModelAndView("heatmap");
		// model.addObject(portfolio);

		return model;
	}

	@RequestMapping(value = "/websockets", method = RequestMethod.GET)
	public ModelAndView showWebsockets() {
		ModelAndView model = new ModelAndView("websockets");
		return model;
	}

	@RequestMapping(value = "{portfolioId}/json", method = RequestMethod.GET)
	@ResponseBody
	public Portfolio showPortfolioJson(@PathVariable("portfolioId") String portfolioId) {
		System.out.println("portfolio " + portfolioId);

		Portfolio portfolio = portfolioService.findByName(portfolioId);

		return portfolio;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String get() {
		System.out.println("get /");

		return "create_portfolio";
	}

}
