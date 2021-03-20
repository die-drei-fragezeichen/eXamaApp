package ch.diedreifragezeichen.exama.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findRoleById(Long id);
    public Role findRoleByName(String name);
}