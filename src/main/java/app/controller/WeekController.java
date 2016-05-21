package app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.Week;
import app.repository.WeekRepository;

@RestController
public class WeekController {

	@Autowired
	private WeekRepository repository;
	
	@RequestMapping(value="/weeks/list", method=RequestMethod.GET)
	public List<Week> getWeeksList() {
		List<Week> weeks = new ArrayList<>();
		try {
			Document doc = Jsoup.connect("http://gaonchart.co.kr/main/section/chart/online.gaon?nationGbn=T&serviceGbn=S1020").get();
			Elements options = doc.select("select#chart_week_select > option:not(:first-child)");
			for (Element e : options) {
				Week w = new Week();
				w.setYear(Integer.parseInt(e.attr("value").substring(0, 4)));
				w.setWeek(Integer.parseInt(e.attr("value").substring(4)));
				w.setDescription(e.text());
				weeks.add(w);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return weeks;
	}
	
	@RequestMapping(value="/weeks/list", method=RequestMethod.POST)
	public void createWeeksList() {
		try {
			Document doc = Jsoup.connect("http://gaonchart.co.kr/main/section/chart/online.gaon?nationGbn=T&serviceGbn=S1020").get();
			Elements options = doc.select("select#chart_week_select > option:not(:first-child)");
			for (Element e : options) {
				Week w = new Week();
				w.setYear(Integer.parseInt(e.attr("value").substring(0, 4)));
				w.setWeek(Integer.parseInt(e.attr("value").substring(4)));
				w.setDescription(e.text());
				repository.save(w);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/weeks/missing", method=RequestMethod.GET)
	public List<Week> getMissingWeeks() {
		return repository.findWhereDownloadsNotExists();
	}
}