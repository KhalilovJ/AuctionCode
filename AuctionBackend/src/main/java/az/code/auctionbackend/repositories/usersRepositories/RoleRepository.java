package az.code.auctionbackend.repositories.usersRepositories;

import az.code.auctionbackend.entities.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
