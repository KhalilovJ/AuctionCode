package az.code.auctionbackend.repositories.usersRepositories;

import az.code.auctionbackend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT R FROM Role R WHERE R.name = ?1")
    Optional<Role> findRoleByName(String name);
}
