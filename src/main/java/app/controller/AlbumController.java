package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.Album;
import app.repository.AlbumRepository;

@RestController
public class AlbumController {

	@Autowired
	private AlbumRepository dao;
	
	@RequestMapping(value="/albums", method=RequestMethod.GET)
	public List<Album> listAll() {
		return (List<Album>) dao.findAll();
	}
	
	@RequestMapping(value="/albums", method=RequestMethod.POST, produces=MediaType.TEXT_PLAIN_VALUE)
	public String insert(@RequestBody Album album) {
		try {
			dao.save(album);
		} catch(Exception e) {
			return "Error: " + e.toString();
		}
		return "Album added successfully";
	}
	
	@RequestMapping(value="/albums/{id}", method=RequestMethod.DELETE, produces=MediaType.TEXT_PLAIN_VALUE)
	public String delete(@PathVariable Long id) {
		try {
			dao.delete(id);
		} catch(Exception e) {
			return "Error: " + e.toString();
		}
		return "Album deleted successfully";
	}
	
	@RequestMapping(value="/albums/artist/{id}")
	public List<Album> listAlbumsByArtist(@PathVariable Long id) {
		return (List<Album>) dao.findByArtistId(id);
	}
}
