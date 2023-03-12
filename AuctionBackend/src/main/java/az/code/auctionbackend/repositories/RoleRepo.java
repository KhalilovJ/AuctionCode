package az.code.auctionbackend.repositories;

import az.code.auctionbackend.entities.Role;
import az.code.auctionbackend.repositories.usersRepositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleRepo {


    @Autowired
    private RoleRepository roleRepo;

    public Optional<Role> findById(Long id){
        return roleRepo.findById(id);
    }

}
