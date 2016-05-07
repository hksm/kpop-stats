package app.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import app.model.Role;

@Transactional
public interface RoleRepository extends CrudRepository<Role, Long> {

	public Role findByRole(String role);
}
