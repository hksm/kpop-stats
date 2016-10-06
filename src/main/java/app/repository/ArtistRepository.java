package app.repository;

import javax.persistence.NonUniqueResultException;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import app.model.Artist;

@Transactional
public interface ArtistRepository extends CrudRepository<Artist, Long> {
	
	@Query("SELECT a FROM Artist a WHERE a.name = :name OR a.alias = :name")
	public Artist findByNameOrAliasIgnoreCase(@Param("name") String name) throws NonUniqueResultException;

}
