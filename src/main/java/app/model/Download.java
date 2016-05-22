package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity @Table(uniqueConstraints=@UniqueConstraint(columnNames={"ranking", "week"}))
public class Download {

	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne @NotNull
	private Track track;
	
	@NotNull
	private Integer downloads;
	
	@ManyToOne  @NotNull
	private Week week;
	
	@NotNull
	private Integer ranking;
	
	public Download() {
	}

	public Download(Track track, Integer downloads, Week week, Integer ranking) {
		this.track = track;
		this.downloads = downloads;
		this.week = week;
		this.ranking = ranking;
	}

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
