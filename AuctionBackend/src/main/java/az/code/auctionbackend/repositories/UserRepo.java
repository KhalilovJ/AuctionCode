package az.code.auctionbackend.repositories;

import az.code.auctionbackend.entities.users.UserProfile;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRepo {

    @Autowired
    private EntityManager em;


    @Transactional
    public UserProfile saveUser(UserProfile userProfile){
        return em.merge(userProfile);
    }
}
