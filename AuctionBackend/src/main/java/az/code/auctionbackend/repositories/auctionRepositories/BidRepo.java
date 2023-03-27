package az.code.auctionbackend.repositories.auctionRepositories;

import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
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

            // It's okay
            Lot lot = (Lot) em.createNativeQuery("select * from lots as lot where lot.id = ?1", Lot.class)
                    .setParameter(1, id).getSingleResult();

            return lot;
        }

        @Transactional
        public void updateLotStatus(Long id, int status){
            em.createNativeQuery("update lots set status = ?2 where id = ?1")
                    .setParameter(1, id).setParameter(2, status).executeUpdate();
        }

        @Transactional
        public Lot saveLot(Lot lot){
            return em.merge(lot);
        }

        public List<Lot> getUsersWonLots(String username){
            Query q = em.createQuery("SELECT l from Lot l where l.lotWinner.username = ?1 order by l.endDate").setParameter(1,username);
            return q.getResultList();
        }

    public List<Lot> getUsersLots(String username){
        Query q = em.createQuery("SELECT l from Lot l where l.user.username = ?1 order by l.endDate").setParameter(1,username);
        return q.getResultList();
    }

    public List<Lot> getWaitingLots(){
        Query q = em.createQuery("SELECT l from Lot l where l.status = 0 order by l.endDate");
        return q.getResultList();
    }


}
