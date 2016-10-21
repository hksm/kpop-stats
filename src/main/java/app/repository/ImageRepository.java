package app.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import app.model.Category;
import app.model.Image;

@Transactional
public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {

	public List<Image> findAllByCategoryAndEspecificId(Category category, Long especificId);
	
	public Image findFirstByCategoryAndEspecificIdOrderByTimestampDesc(Category category, Long especificId);
}
