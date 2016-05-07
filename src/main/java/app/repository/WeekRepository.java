package app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import app.model.Week;

@Transactional
public interface WeekRepository extends CrudRepository<Week, Integer> {

	public Week findByYearAndWeek(int year, int week);
}
