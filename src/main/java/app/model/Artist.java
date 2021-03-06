package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Artist {

	@Id @GeneratedValue
	private Long id;
	
	@NotNull
	private String name;
	
	private String alias;
	
	private Integer debutYear;

	public Artist() {
	}
	
	public Artist(String name) {
		this.name = name;
	}
	
	public Artist(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}
	
	public Artist(String name, String alias, Integer debutYear) {
		this.name = name;
		this.alias = alias;
		this.debutYear = debutYear;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String nome) {
		this.name = nome;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public Integer getDebutYear() {
		return debutYear;
	}
	
	public void setDebutYear(Integer debutYear) {
		this.debutYear = debutYear;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
