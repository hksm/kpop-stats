package app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.NonUniqueResultException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.Album;
import app.model.Artist;
import app.model.Download;
import app.model.Track;
import app.model.Week;
import app.repository.AlbumRepository;
import app.repository.ArtistRepository;
import app.repository.DownloadRepository;
import app.repository.TrackRepository;
import app.repository.WeekRepository;

@RestController
public class DownloadController {

	@Autowired
	private DownloadRepository dao;
	
	@Autowired
	private ArtistRepository aDao;
	
	@Autowired
	private TrackRepository tDao;
	
	@Autowired
	private AlbumRepository alDao;
	
	@Autowired
	private WeekRepository wDao;
	
	@RequestMapping(value="/weeklist", method=RequestMethod.GET)
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
	
	@RequestMapping(value="/test", method=RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	public Track test() {
		Set<Artist> artists = new HashSet<>();
		artists.add(aDao.findByNameOrAliasIgnoreCase("IU"));
		artists.add(aDao.findByNameOrAliasIgnoreCase("HIGH4"));
		
		System.out.println(artists.size());
		Track t = tDao.findByArtistAndTitleIgnoreCase(artists, new Long(artists.size()), "사랑");
		System.out.println(t == null ? "null" : t.getTitle());
		return t;
	}
	
	@RequestMapping(value="/downloads", method=RequestMethod.GET)
	public List<Download> listAll() {
		return (List<Download>) dao.findAll();
	}
	
	@RequestMapping(value="/downloads", method=RequestMethod.POST, produces=MediaType.TEXT_PLAIN_VALUE)
	public String insert(@RequestBody Week week) {
		try {
			String url = "http://gaonchart.co.kr/main/section/chart/online.gaon?nationGbn=T&serviceGbn=S1020"
						+ "&targetTime=" + week.getWeek() + "&hitYear=" + week.getYear() + "&termGbn=week";
			Document doc = Jsoup.connect(url).get();
			Element table = doc.select("table").first();
			Elements lines = table.getElementsByTag("tr");
			lines.remove(0);
			
			for (Element line : lines) {
				String[] parts = line.getElementsByTag("td").get(3).child(1).text().split(Pattern.quote("|"));
				String[] multiArtists = parts[0].split(Pattern.quote(", "));
				Set<Artist> artists = new HashSet<>();
				for (String a : multiArtists) {
					String[] nameAndAlias = a.split("[\\(\\)]");
					Artist artist = null;
					try {
						artist = aDao.findByNameOrAliasIgnoreCase(nameAndAlias[nameAndAlias.length-1]);
					} catch(NonUniqueResultException e) {
						artist = aDao.findByNameOrAliasIgnoreCase(nameAndAlias[0]);
					}
					if (artist == null) {
						artist = new Artist();
						artist.setName(nameAndAlias[nameAndAlias.length-1]);
						if (nameAndAlias[nameAndAlias.length-1] != nameAndAlias[0]) {
							artist.setAlias(nameAndAlias[0]);
						}
						aDao.save(artist);
					}
					artists.add(artist);
				}
				Track track = null;
				String title = line.getElementsByTag("td").get(3).child(0).text();
				System.out.println(artists.iterator().next().getName());
				track = tDao.findByArtistAndTitleIgnoreCase(artists, new Long(artists.size()), title);
				if (track == null) {
					track = new Track();
					track.setTitle(title);
					track.setArtists(artists);
					tDao.save(track);
				}
				
				String producerCompany = line.getElementsByTag("td").get(5).child(0).text();
				String distributorCompany = line.getElementsByTag("td").get(5).child(1).text();
				
				Album album = null;
				String albumTitle = parts[1];
				album = alDao.findByTitleAndCompanyIgnoreCase(albumTitle, producerCompany, distributorCompany);

				if (album == null) {
					album = new Album();
					album.setTitle(albumTitle);
					album.setArtists(artists);
					album.setProducerCompany(producerCompany);
					album.setDistributorCompany(distributorCompany);
					Set<Track> tracks = new HashSet<Track>();
					tracks.add(track);
					album.setTracks(tracks);
					alDao.save(album);
				} else {
					if (!album.getTracks().contains(track)) {
						Set<Track> tracks = album.getTracks() != null ? album.getTracks() : new HashSet<>();
						tracks.add(track);
						album.setTracks(tracks);
						alDao.save(album);
					}
				}
				
				if (wDao.findByYearAndWeek(week.getYear(), week.getWeek()) == null) {
					wDao.save(week);
				}
				
				week = wDao.findByYearAndWeek(week.getYear(), week.getWeek());
				
				Download download = new Download();
				download.setRanking(Integer.parseInt(line.getElementsByTag("td").get(0).text()));
				download.setWeek(week);
				download.setTrack(track);
				download.setDownloads(Integer.parseInt(line.getElementsByTag("td").get(4).text().replaceAll(",", "")));
				dao.save(download);
			}
			System.out.println("Process finalized!");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "Downloads added successfully";
	}
}
