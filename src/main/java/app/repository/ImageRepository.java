package app.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import app.model.Category;
import app.model.Image;

@Transactional
public interface ImageRepository extends CrudRepository<Image, Long> {

	public List<Image> findAllByCategoryAndEspecificId(Category category, Long especificId);
	
	public Image findFirstByCategoryAndEspecificIdOrderByTimestampDesc(Category category, Long especificId);
}
