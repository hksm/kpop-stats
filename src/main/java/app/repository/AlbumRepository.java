package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import app.model.Album;

@Transactional
public interface AlbumRepository extends PagingAndSortingRepository<Album, Long> {

	@Query("SELECT a FROM Album a WHERE a.title = :title AND a.producerCompany LIKE CONCAT('%',:prod,'%') "
			+ "AND a.distributorCompany LIKE CONCAT('%',:dist,'%')")
	public Album findByTitleAndCompanyIgnoreCase(@Param("title") String title, @Param("prod") String prod, @Param("dist") String dist);
	
	@Query("SELECT al FROM Album al JOIN al.artists ar WHERE ar.id = :id")
	public List<Album> findByArtistId(@Param("id") Long id);
}
