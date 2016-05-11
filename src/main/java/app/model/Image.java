package app.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

@Entity
public class Image {

	@Id @GeneratedValue
	private Long id;
	
	@Enumerated @NotNull
	private Category category;
	
	@NotNull
	private Long especificId;
	
	@NotNull
	private String link;
	
	@NotNull
	private DateTime timestamp;

	public Image() {
	}

	public Image(Category category, Long especificId, String link, DateTime timestamp) {
		this.category = category;
		this.especificId = especificId;
		this.link = link;
		this.timestamp = timestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getEspecificId() {
		return especificId;
	}

	public void setEspecificId(Long especificId) {
		this.especificId = especificId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}
}
