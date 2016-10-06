package app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.model.Category;
import app.model.Image;
import app.repository.AlbumRepository;
import app.repository.ArtistRepository;
import app.repository.ImageRepository;
import app.service.CloudinaryService;
import app.service.LastfmService;

@RestController
public class ImageController {
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	CloudinaryService cloudinaryService;
	
	@Autowired
	LastfmService lastfmService;
	
	@Autowired
	ArtistRepository artistRepository;
	
	@Autowired
	AlbumRepository albumRepository;
	
	@RequestMapping(value="/images", method=RequestMethod.POST, produces=MediaType.TEXT_PLAIN_VALUE)
	public String postFromLastFm(@RequestParam("category") Category category,
								 @RequestParam("id") Long id) {
		String uploadErrorMessage = "Error uploading the image.";
		String lastfmUrl;
		switch(category) {
			case ARTIST:
				lastfmUrl = lastfmService.getMainArtistPicUrl(artistRepository.findOne(id));
				if (lastfmUrl == null) {
					return "Image for the especified artist not found.";
				}
				break;
			case ALBUM:
				lastfmUrl = lastfmService.getMainAlbumPicUrl(albumRepository.findOne(id));
				if (lastfmUrl == null) {
					return "Image for the especified album not found.";
				}
				break;
			default:
				return uploadErrorMessage;
		}
		try {
			Map<String, String> imgMap = cloudinaryService.upload(lastfmUrl);
			if (imgMap.isEmpty()) {
				return uploadErrorMessage;
			}
			imageRepository.save(new Image(category, id, imgMap.get("url"), imgMap.get("public_id"), new DateTime()));
			return "Image uploaded successfully!";
		} catch (IOException e) {
			e.printStackTrace();
			return uploadErrorMessage;
		}
	}
	
	@RequestMapping(value="/images/{category}/{id}/last", method=RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	public String getLinkFromLast(@PathVariable Category category, @PathVariable Long id) {
		Image image = imageRepository.findFirstByCategoryAndEspecificIdOrderByTimestampDesc(category, id);
		
		return image.getLink();
	}
	
	@RequestMapping(value="/images/{category}/{id}", method=RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	public List<String> getLinkFromAll(@PathVariable Category category, @PathVariable Long id) {
		List<Image> images = imageRepository.findAllByCategoryAndEspecificId(category, id);
		List<String> links = new ArrayList<>();
		for (Image img : images) {
			links.add(img.getLink());
		}
		return links;
	}
}