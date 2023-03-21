package az.code.auctionbackend.repositories.auctionRepositories;

import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class AuctionRealtimeRepo {

    HashMap<Long, Lot> auctionData = new HashMap<>();

//    public void addLot(Lot lot){
//        auctionData.put(lot.getId(), lot);
//    }

//    public Lot getLot(Long id){
//        return auctionData.getOrDefault(id, null);
//    }

//    public Lot deleteLot(Long id){
//        Lot lot = getLot(id);
//        System.out.println("deleteLot " );
//        auctionData.remove(lot.getId());
//        System.out.println();
//        return lot;
//    }

//    public Lot makeBid(Long lotId, Bid bid){
//        Lot lot = auctionData.get(lotId);
//
//        if (lot.getBidHistory() == null) {
//            lot.setBidHistory(new ArrayList<>());
//            return lot;
//        }
//
//        System.out.println("makeBid ");
//
//        lot.getBidHistory().add(bid);
//        if (bid.getBid() > lot.getCurrentBid()){
//            lot.setCurrentBid(bid.getBid());
//        }
//        return lot;
//    }
}
