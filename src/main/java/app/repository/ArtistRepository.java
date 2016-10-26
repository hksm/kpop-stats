package app.repository;

import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import app.model.Artist;

@Transactional
public interface ArtistRepository extends PagingAndSortingRepository<Artist, Long> {
	
	@Query("SELECT a FROM Artist a WHERE a.name = :name OR a.alias = :name")
	public Artist findByNameOrAliasIgnoreCase(@Param("name") String name) throws NonUniqueResultException;
	
	@Query("SELECT a FROM Artist a WHERE a.name like CONCAT('%',:query,'%') OR a.alias like CONCAT('%',:query,'%')")
	public List<Artist> findByQueryIgnoreCase(@Param("query") String query);

}
