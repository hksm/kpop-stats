package app.repository;

import javax.persistence.NonUniqueResultException;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import app.model.Genre;

@Transactional
public interface GenreRepository extends PagingAndSortingRepository<Genre, Long> {
	
	public Genre findByNameIgnoreCase(String name) throws NonUniqueResultException;

}
