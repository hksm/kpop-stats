package app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import app.model.Download;

@Transactional
public interface DownloadRepository extends CrudRepository<Download, Long> {
	
}
