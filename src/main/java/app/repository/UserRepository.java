package app.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import app.model.User;

@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

	public User findByUsername(String username);
}
