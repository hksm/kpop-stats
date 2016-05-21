package app.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.NonUniqueResultException;

import org.joda.time.DateTime;
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
import app.model.Category;
import app.model.Download;
import app.model.Image;
import app.model.Track;
import app.model.Week;
import app.repository.AlbumRepository;
import app.repository.ArtistRepository;
import app.repository.DownloadRepository;
import app.repository.ImageRepository;
import app.repository.TrackRepository;
import app.repository.WeekRepository;
import app.service.CloudinaryService;
import app.service.LastfmService;

@RestController
public class DownloadController {

	@Autowired
	private DownloadRepository downloadRepository;
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private TrackRepository trackRepository;
	
	@Autowired
	private AlbumRepository albumRepository;
	
	@Autowired
	private WeekRepository weekRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private LastfmService lastfmService;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@RequestMapping(value="/downloads", method=RequestMethod.GET)
	public List<Download> listAll() {
		return (List<Download>) downloadRepository.findAll();
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
						artist = artistRepository.findByNameOrAliasIgnoreCase(nameAndAlias[nameAndAlias.length-1]);
					} catch(NonUniqueResultException e) {
						artist = artistRepository.findByNameOrAliasIgnoreCase(nameAndAlias[0]);
					}
					if (artist == null) {
						artist = new Artist();
						artist.setName(nameAndAlias[nameAndAlias.length-1]);
						if (nameAndAlias[nameAndAlias.length-1] != nameAndAlias[0]) {
							artist.setAlias(nameAndAlias[0]);
						}
						artistRepository.save(artist);
						
						String lastfmLink = lastfmService.getMainArtistPicUrl(artist);
						if (lastfmLink != null) {
							String link = cloudinaryService.upload(lastfmLink);
							Image image = new Image();
							image.setCategory(Category.ARTIST);
							image.setEspecificId(artist.getId());
							image.setLink(link);
							image.setTimestamp(new DateTime());
							imageRepository.save(image);
						}
					}
					artists.add(artist);
				}
				Track track = null;
				String title = line.getElementsByTag("td").get(3).child(0).text();
				System.out.println(artists.iterator().next().getName());
				track = trackRepository.findByArtistAndTitleIgnoreCase(artists, new Long(artists.size()), title);
				if (track == null) {
					track = new Track();
					track.setTitle(title);
					track.setArtists(artists);
					trackRepository.save(track);
				}
				
				String producerCompany = line.getElementsByTag("td").get(5).child(0).text();
				String distributorCompany = line.getElementsByTag("td").get(5).child(1).text();
				
				Album album = null;
				String albumTitle = parts[1];
				album = albumRepository.findByTitleAndCompanyIgnoreCase(albumTitle, producerCompany, distributorCompany);

				if (album == null) {
					album = new Album();
					album.setTitle(albumTitle);
					album.setArtists(artists);
					album.setProducerCompany(producerCompany);
					album.setDistributorCompany(distributorCompany);
					Set<Track> tracks = new HashSet<Track>();
					tracks.add(track);
					album.setTracks(tracks);
					albumRepository.save(album);
					
					String lastfmLink = lastfmService.getMainAlbumPicUrl(album);
					if (lastfmLink != null) {
						String link = cloudinaryService.upload(lastfmLink);
						Image image = new Image();
						image.setCategory(Category.ALBUM);
						image.setEspecificId(album.getId());
						image.setLink(link);
						image.setTimestamp(new DateTime());
						imageRepository.save(image);
					}
				} else {
					if (!album.getTracks().contains(track)) {
						Set<Track> tracks = album.getTracks() != null ? album.getTracks() : new HashSet<>();
						tracks.add(track);
						album.setTracks(tracks);
						albumRepository.save(album);
					}
				}
				
				if (weekRepository.findByYearAndWeek(week.getYear(), week.getWeek()) == null) {
					weekRepository.save(week);
				}
				
				week = weekRepository.findByYearAndWeek(week.getYear(), week.getWeek());
				
				Download download = new Download();
				download.setRanking(Integer.parseInt(line.getElementsByTag("td").get(0).text()));
				download.setWeek(week);
				download.setTrack(track);
				download.setDownloads(Integer.parseInt(line.getElementsByTag("td").get(4).text().replaceAll(",", "")));
				downloadRepository.save(download);
			}
			System.out.println("Process finalized!");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "Downloads added successfully";
	}
}
