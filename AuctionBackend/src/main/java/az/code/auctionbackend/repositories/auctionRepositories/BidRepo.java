package az.code.auctionbackend.repositories.auctionRepositories;

import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidRepo {

        @Autowired
        private EntityManager em;

        public List<Bid> getAllBids(){
            return em.createNativeQuery("SELECT * FROM bids").getResultList();
        };

        public List<Lot> getAllLots(){
            return em.createNativeQuery("SELECT * FROM lots").getResultList();
        }

        public Lot getLotById(Long id){
            System.out.println("error is here");
            return (Lot) em.createNativeQuery("select * from lots as lot where lot.id = ?1", Lot.class)
                    .setParameter(1, id).getSingleResult();
        }
}
