package app.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.model.Category;
import app.model.Image;
import app.repository.ImageRepository;
import app.service.CloudinaryService;

@RestController
public class ImageController {
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	CloudinaryService cloudinaryService;
	
	@RequestMapping(value="/upload", method=RequestMethod.POST, produces=MediaType.TEXT_PLAIN_VALUE)
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
								   @RequestParam("category") Category category,
								   @RequestParam("id") Long id) {
		if (!file.getContentType().matches("(?i)image/jpeg|image/jpg|image/image/png")) {
			return "File type not allowed";
		}
		
		if (!file.isEmpty()) {			
			try {
				File temp = File.createTempFile("img", file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
				file.transferTo(temp);
				String link = cloudinaryService.upload(temp);
				temp.delete();
				
				Image image = new Image();
				image.setCategory(category);
				image.setEspecificId(id);
				image.setLink(link);
				image.setTimestamp(new DateTime());
				
				imageRepository.save(image);
				
				return "Image successfully uploaded!";
			} catch (Exception e) {
				e.printStackTrace();
				return "The upload failed";
			}
		} else {
			return "The upload failed";
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