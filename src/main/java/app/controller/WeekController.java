package app.controller;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.Week;
import app.repository.WeekRepository;
import app.service.GaonService;

@RestController
public class WeekController {

	@Autowired
	private WeekRepository repository;
	
	@Autowired
	private GaonService gaonService;
	
	@RequestMapping(value="/weeks/list", method=RequestMethod.POST)
	public void createWeeksList() {
		try {
			Elements options = gaonService.getWeekSelectItems();
			for (Element e : options) {
				Week w = new Week();
				w.setYear(Integer.parseInt(e.attr("value").substring(0, 4)));
				w.setWeek(Integer.parseInt(e.attr("value").substring(4)));
				w.setDescription(e.text());
				repository.save(w);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/weeks/missing", method=RequestMethod.GET)
	public List<Week> getMissingWeeks() {
		return repository.findWhereDownloadsNotExists();
	}
}