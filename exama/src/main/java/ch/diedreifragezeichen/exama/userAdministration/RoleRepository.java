package ch.diedreifragezeichen.exama.userAdministration;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {
}