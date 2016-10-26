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

import app.model.Genre;
import app.repository.GenreRepository;

@RestController
public class GenreController {
	
	@Autowired
	private GenreRepository dao;
	
	@RequestMapping(value="/genres", method=RequestMethod.GET)
	public Page<Genre> listAll(Pageable pageable) {
		return (Page<Genre>) dao.findAll(pageable);
	}
	
	@RequestMapping(value="/genres/{id}", method=RequestMethod.GET)
	public Genre getGenreById(@PathVariable Long id) {
		return dao.findOne(id);
	}
	
	@RequestMapping(value="/genres", method=RequestMethod.POST, produces=MediaType.TEXT_PLAIN_VALUE)
	public String insert(@RequestBody Genre genre) {
		try {
			dao.save(genre);	
		} catch(Exception e) {
			return "Error: " + e.toString();
		}
		if (genre.getId() != null) {
			return "Genre updated successfully";
		} else {
			return "Genre added successfully";
		}
	}
	
	@RequestMapping(value="/genres/{id}", method=RequestMethod.DELETE, produces=MediaType.TEXT_PLAIN_VALUE)
	public String delete(@PathVariable Long id) {
		try {
			dao.delete(id);
		} catch(Exception e) {
			return "Error: " + e.toString();
		}
		return "Genre deleted successfully";
	}
}
