package az.code.auctionbackend.repositories;

import az.code.auctionbackend.entities.UserProfile;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRepo {

    @Autowired
    private EntityManager em;


    @Transactional
    public UserProfile saveUser(UserProfile userProfile){
        return em.merge(userProfile);
    }

    public List<UserProfile> allUsers(){
        return em.createNativeQuery("SELECT * FROM profiles").getResultList();
    };

    public UserProfile getUserById(Long id){
        return em.find(UserProfile.class, id);
    }

    public List<UserProfile>getByIdList(List<Long> ids){
        return em.createQuery("select b from UserProfile b where b.id in (:isbn)", UserProfile.class)
    .setParameter("isbn", ids).getResultList();
        }

    @Transactional
    public void blockUser(Long userId, boolean block){
        em.createQuery("update UserProfile u set u.isBlocked = ?1 where u.id = ?2")
                .setParameter(1, block)
                .setParameter(2, userId)
                .executeUpdate();
    }

    }
