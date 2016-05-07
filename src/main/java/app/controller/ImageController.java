package app.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.model.Category;
import app.repository.ImageRepository;
import app.service.CloudinaryService;

@RestController
public class ImageController {
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	CloudinaryService cloudinaryService;
	
	@RequestMapping(value="/upload", method=RequestMethod.POST, produces=MediaType.TEXT_PLAIN_VALUE)
	public String handleFileUpload(@RequestParam("name") String name,
								   @RequestParam("file") MultipartFile file,
								   @RequestParam("category") Category category,
								   @RequestParam("id") Long id) {
		if (!file.getContentType().matches("(?i)image/jpeg|image/jpg|image/image/png")) {
			return "File type not allowed";
		}
		
		if (!file.isEmpty()) {			
			try {
				File temp = File.createTempFile("img", file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
				file.transferTo(temp);
				cloudinaryService.upload(temp);
				temp.delete();
				return name + " successfully uploaded!";
			} catch (Exception e) {
				e.printStackTrace();
				return "The upload failed";
			}
		} else {
			return "The upload failed";
		}
	}
	/*
	@RequestMapping(value="/image/artist/{id}", method=RequestMethod.GET, produces={"image/jpg", "image/jpeg", "image/png"})
	public @ResponseBody ResponseEntity<ByteArrayResource> serveArtistImage(@PathVariable Long id) {
		Image img = imageRepository.findFirstByArtistOrderByTimestampDesc(imageRepository.findOne(id));
		
		
		File file = new File(img != null ? img.getAbsolutePath() : "../kpop-stats-img/artist/not-found.jpg");
		Path path = file.toPath();

	    try {
			return ResponseEntity.ok()
			        .contentLength(file.length())
			        .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
			        .body(new ByteArrayResource(Files.readAllBytes(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return null;
	}
	*/	
}