package app.controller;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
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
import app.repository.DownloadRepository;
import app.repository.ImageRepository;
import app.service.CloudinaryService;
import app.service.GaonService;
import app.service.LastfmService;

@RestController
public class DownloadController {
	
	@Autowired
	private DownloadRepository downloadRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private LastfmService lastfmService;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired
	private GaonService gaonService;
	
	@RequestMapping(value="/downloads", method=RequestMethod.GET)
	public List<Download> listAll() {
		return (List<Download>) downloadRepository.findAll();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/downloads", method=RequestMethod.POST, produces=MediaType.TEXT_PLAIN_VALUE)
	public String insert(@RequestBody Week week) {
		try {
			Elements tableLines = gaonService.getDownloadLines(week);
			
			for (Element line : tableLines) {
				Map<String, Object> map = gaonService.getMapFromLine(line);
				
				// Add each Artist
				Set<Artist> artists = gaonService.verifyArtists((List<String>) map.get("artists"));
				
				for (Artist artist : artists) {
					Image image = null;
					image = imageRepository.findFirstByCategoryAndEspecificIdOrderByTimestampDesc(Category.ARTIST, artist.getId());
					if (image == null) {
						try {
							String lastfmLink = lastfmService.getMainArtistPicUrl(artist);
							if (lastfmLink != null && !lastfmLink.isEmpty()) {
								String link = cloudinaryService.upload(lastfmLink);
								image = new Image(Category.ARTIST, artist.getId(), link, new DateTime());
								imageRepository.save(image);
							}
						} catch(FileNotFoundException e) {
							System.out.println("File Not Found for Artist: " + artist.getName());
						} catch(Exception e) {
							System.out.println("Error adding image for Artist: " + artist.getName());
						}
					}
				}
				
				// Add Track
				Track track = gaonService.verifyTrack((String) map.get("title"), artists);
				
				// Add Album
				Album album = gaonService.verifyAlbum((String) map.get("album"), track, artists, 
						(String) map.get("producer"), (String) map.get("distributor"));
				
				try {
					String lastfmLink = lastfmService.getMainAlbumPicUrl(album);
					if (lastfmLink != null && !lastfmLink.isEmpty()) {
						String link = cloudinaryService.upload(lastfmLink);
						Image image = new Image(Category.ALBUM, album.getId(), link, new DateTime());
						imageRepository.save(image);
					}
				} catch(FileNotFoundException e) {
					System.out.println("File Not Found for Album: " + album.getTitle());
				} catch(Exception e) {
					System.out.println("Error adding image for Album: " + album.getTitle() + " - " + e.getMessage());
				}
								
				// Add week
				week = gaonService.verifyWeek(week.getYear(), week.getWeek());
				
				// Add download
				Download download = new Download(
						track, 
						(Integer) map.get("downloads"),
						week,
						(Integer) map.get("ranking"));
				downloadRepository.save(download);
				
				System.out.println("Added " + map.get("ranking") + ": " + track.getTitle() 
				+ " --- " + Arrays.toString(track.getArtists().toArray()) + " --- " + album.getTitle());
			}
			
			return "Downloads added successfully";
		} catch(Exception e) {
			e.printStackTrace();
			return "Error while adding downloads";
		}
	}
}