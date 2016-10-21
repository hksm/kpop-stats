package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.Artist;
import app.repository.ArtistRepository;

@RestController
public class ArtistController {
	
	@Autowired
	private ArtistRepository dao;
	
	@RequestMapping(value="/artists", method=RequestMethod.GET)
	public Page<Artist> listAll(Pageable pageable) {
		return (Page<Artist>) dao.findAll(pageable);
	}
	
	@RequestMapping(value="/artists/{id}", method=RequestMethod.GET)
	public Artist getArtistById(@PathVariable Long id) {
		return dao.findOne(id);
	}
	
	@RequestMapping(value="/artists", method=RequestMethod.POST, produces=MediaType.TEXT_PLAIN_VALUE)
	public String insert(@RequestBody Artist artist) {
		try {
			dao.save(artist);	
		} catch(Exception e) {
			return "Error: " + e.toString();
		}
		if (artist.getId() != null) {
			return "Artists updated successfully";
		} else {
			return "Artist added successfully";
		}
	}
	
	@RequestMapping(value="/artists/{id}", method=RequestMethod.DELETE, produces=MediaType.TEXT_PLAIN_VALUE)
	public String delete(@PathVariable Long id) {
		try {
			dao.delete(id);
		} catch(Exception e) {
			return "Error: " + e.toString();
		}
		return "Artist deleted successfully";
	}
}
