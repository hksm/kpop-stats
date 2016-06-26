package app.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.NonUniqueResultException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.model.Album;
import app.model.Artist;
import app.model.Track;
import app.model.Week;
import app.repository.AlbumRepository;
import app.repository.ArtistRepository;
import app.repository.TrackRepository;
import app.repository.WeekRepository;

@Service
public class GaonService {
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private TrackRepository trackRepository;
	
	@Autowired
	private AlbumRepository albumRepository;
	
	@Autowired
	private WeekRepository weekRepository;
	
	public Elements getWeekSelectItems() throws IOException {
		Document doc = Jsoup.connect("http://gaonchart.co.kr/main/section/chart/online.gaon?nationGbn=T&serviceGbn=S1020").get();
		Elements options = doc.select("select#chart_week_select > option:not(:first-child)");
		return options;
	}
	
	public Elements getDownloadLines(Week week) throws IOException {
		String url = "http://gaonchart.co.kr/main/section/chart/online.gaon?nationGbn=T&serviceGbn=S1020"
					+ "&targetTime=" + week.getWeek() + "&hitYear=" + week.getYear() + "&termGbn=week";
		Document doc = Jsoup.connect(url).get();
		Element table = doc.select("table").first();
		Elements lines = table.getElementsByTag("tr");
		lines.remove(0); // Remove table header
		return lines;
	}
	
	public Map<String, Object> getMapFromLine(Element line) {
		Map<String, Object> map = new HashMap<>();
		
		String[] parts = line.getElementsByTag("td").get(3).child(1).text().split(Pattern.quote("|")); // Split artists + album cell
		map.put("album", parts[1]); // Add album name to map
		
		List<String> artistNames = Arrays.asList(parts[0].split(Pattern.quote(", "))); // Split multiple artists into a List
		map.put("artists", artistNames); // Add artists to map
		
		map.put("title", line.getElementsByTag("td").get(3).child(0).text()); // Add title to map
		map.put("producer", line.getElementsByTag("td").get(5).child(0).text());
		map.put("distributor", line.getElementsByTag("td").get(5).child(1).text());
		map.put("downloads", Integer.parseInt(line.getElementsByTag("td").get(4).text().replaceAll(",", "")));
		map.put("ranking", Integer.parseInt(line.getElementsByTag("td").get(0).text()));
		
		return map;
	}
	
	public Set<Artist> verifyArtists(List<String> artistStringList) {
		Set<Artist> artists = new HashSet<>();
		for (String artistString : artistStringList) {
			List<String> names = Arrays.asList(artistString.split("[\\(\\)]"));
			Artist artist = null;
			try {
				// Try searching by Alias first
				artist = artistRepository.findByNameOrAliasIgnoreCase(names.get(names.size()-1));
				if (artist == null && names.size() > 1) {
					// Then Searches by Name
					artist = artistRepository.findByNameOrAliasIgnoreCase(names.get(0));
				}
			} catch(NonUniqueResultException e) {
				// Tries to search by Name in case the alias one had multiple results
				artist = artistRepository.findByNameOrAliasIgnoreCase(names.get(0));
			}
			// If not yet included in the database
			if (artist == null) {
				artist = new Artist(names.get(names.size()-1));
				if (names.size() > 1) {
					artist.setAlias(names.get(0));
				}
				artistRepository.save(artist);
			}
			artists.add(artist);
		}
		return artists;
	}
	
	public Track verifyTrack(String title, Set<Artist> artists) {
		Track track = null;
		track = trackRepository.findByArtistAndTitleIgnoreCase(artists, new Long(artists.size()), title);
		if (track == null) {
			track = new Track(artists, title);
			trackRepository.save(track);
		}
		return track;
	}
	
	public Album verifyAlbum(String albumTitle, Track track, Set<Artist> artists, String producerCompany, String distributorCompany) {
		Album album = null;
		album = albumRepository.findByTitleAndCompanyIgnoreCase(albumTitle, producerCompany, distributorCompany);

		if (album == null) {
			// Create new
			Set<Track> tracks = new HashSet<Track>();
			tracks.add(track);
			album = new Album(artists, albumTitle, tracks, producerCompany, distributorCompany);
			albumRepository.save(album);
		} else {
			// Add track to album tracklist
			if (!album.getTracks().contains(track)) {
				Set<Track> tracks = album.getTracks() != null ? album.getTracks() : new HashSet<>();
				tracks.add(track);
				album.setTracks(tracks);
				albumRepository.save(album);
			}
		}
		return album;
	}
	
	public Week verifyWeek(int year, int weekNumber) {
		Week week = weekRepository.findByYearAndWeek(year, weekNumber);
		if (week == null) {
			week = weekRepository.save(week);
		}
		return week;
	}
}
