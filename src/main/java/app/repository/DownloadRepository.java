package app.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import app.model.Download;

@Transactional
public interface DownloadRepository extends PagingAndSortingRepository<Download, Long> {
	
}
