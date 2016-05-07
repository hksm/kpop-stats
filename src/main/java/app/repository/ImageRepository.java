package app.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import app.model.Image;

@Transactional
public interface ImageRepository extends CrudRepository<Image, Long> {

}
