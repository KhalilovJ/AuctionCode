package az.code.auctionbackend.repositories;

import az.code.auctionbackend.entities.Role;
import az.code.auctionbackend.entities.SellerData;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.repositories.usersRepositories.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SellerRepo {

    private EntityManager em;

    @Transactional
    public SellerData saveSeller(SellerData sellerData){
        return em.merge(sellerData);
    }
}
