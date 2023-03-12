package az.code.auctionbackend.repositories.auctionRepositories;

import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuctionRealtimeRepo {

    HashMap<Long, Lot> auctionData = new HashMap<>();

    public void addLot(Lot lot){
        auctionData.put(lot.getId(), lot);
    }

    public Lot getLot(Long id){
        return auctionData.getOrDefault(id, null);
    }

    public Lot deletelot(Long id){
        Lot lot = getLot(id);
        auctionData.remove(lot);
        return lot;
    }

    public Lot makeBid(Long lotId, Bid bid){
        Lot lot = auctionData.get(lotId);
        lot.getBidHistory().add(bid);
        if (bid.getBid() > lot.getCurrentBid()){
            lot.setCurrentBid(bid.getBid());
        }
        return lot;
    }
}
