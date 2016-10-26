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

import app.model.Track;
import app.repository.TrackRepository;

@RestController
public class TrackController {

	@Autowired
	private TrackRepository dao;
	
	@RequestMapping(value="/tracks", method=RequestMethod.GET)
	public Page<Track> listAll(Pageable pageable) {
		return (Page<Track>) dao.findAll(pageable);
	}
	
	@RequestMapping(value="/tracks", method=RequestMethod.POST, produces=MediaType.TEXT_PLAIN_VALUE)
	public String insert(@RequestBody Track track) {
		try {
			dao.save(track);
		} catch(Exception e) {
			return "Error: " + e.toString();
		}
		return "Track added successfully";
	}
	
	@RequestMapping(value="/tracks/{id}", method=RequestMethod.DELETE, produces=MediaType.TEXT_PLAIN_VALUE)
	public String delete(@PathVariable Long id) {
		try {
			dao.delete(id);
		} catch(Exception e) {
			return "Error: " + e.toString();
		}
		return "Track deleted successfully";
	}
}
