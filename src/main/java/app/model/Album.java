package app.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Album {

	@Id @GeneratedValue
	private Long id;
	
	@ManyToMany @NotNull
	private Set<Artist> artists;
	
	@NotNull
	private String title;
	
	@ManyToMany
	private Set<Track> tracks;
	
	@NotNull
	private String producerCompany;
	
	@NotNull
	private String distributorCompany;

	@ManyToOne
	private Week firstChartingWeek;
	
	public Album() {
	}
	
	public Album(Set<Artist> artists, String title, Set<Track> tracks, String producerCompany,
			String distributorCompany) {
		this.artists = artists;
		this.title = title;
		this.tracks = tracks;
		this.producerCompany = producerCompany;
		this.distributorCompany = distributorCompany;
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
	
	public Set<Track> getTracks() {
		return tracks;
	}

	public void setTracks(Set<Track> tracks) {
		this.tracks = tracks;
	}

	public String getProducerCompany() {
		return producerCompany;
	}

	public void setProducerCompany(String producerCompany) {
		this.producerCompany = producerCompany;
	}

	public String getDistributorCompany() {
		return distributorCompany;
	}

	public void setDistributorCompany(String distributorCompany) {
		this.distributorCompany = distributorCompany;
	}
	
	public Week getFirstChartingWeek() {
		return firstChartingWeek;
	}
	
	public void setFirstChartingWeek(Week firstChartingWeek) {
		this.firstChartingWeek = firstChartingWeek;
	}
}
