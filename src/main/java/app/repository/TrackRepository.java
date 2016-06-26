package app.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import app.model.Artist;
import app.model.Track;

@Transactional
public interface TrackRepository extends CrudRepository<Track, Long> {
	
	@Query("SELECT t FROM Track t JOIN t.artists a WHERE a IN (:artists) AND t.title = :title GROUP BY t HAVING COUNT(t) = :size")
	public Track findByArtistAndTitleIgnoreCase(@Param("artists") Set<Artist> artists, @Param("size") Long size, @Param("title") String title);
}
