package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Download {

	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne
	private Track track;
	
	private Integer downloads;
	
	@ManyToOne
	private Week week;
	
	private Integer ranking;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public Integer getDownloads() {
		return downloads;
	}
	
	public void setDownloads(Integer downloads) {
		this.downloads = downloads;
	}

	public Week getWeek() {
		return week;
	}

	public void setWeek(Week week) {
		this.week = week;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
}
