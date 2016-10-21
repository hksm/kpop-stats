package app.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Track {

	@Id @GeneratedValue
	private Long id;
	
	@ManyToMany @NotNull
	private Set<Artist> artists;
	
	@NotNull
	private String title;
	
	@ManyToOne
	private Genre genre;
	
	public Track() {
	}
	
	public Track(Set<Artist> artists, String title) {
		this.artists = artists;
		this.title = title;
	}

	public Track(Set<Artist> artists, String title, Genre genre) {
		this.artists = artists;
		this.title = title;
		this.genre = genre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Artist> getArtists() {
		return artists;
	}

	public void setArtists(Set<Artist> artists) {
		this.artists = artists;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}
}
