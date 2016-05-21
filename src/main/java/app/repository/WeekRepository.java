package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import app.model.Week;

@Transactional
public interface WeekRepository extends CrudRepository<Week, Integer> {

	public Week findByYearAndWeek(int year, int week);
	
	@Query("SELECT w FROM Week w WHERE NOT EXISTS (SELECT d FROM Download d WHERE d.week = w) ORDER BY w.description DESC")
	public List<Week> findWhereDownloadsNotExists();
}
