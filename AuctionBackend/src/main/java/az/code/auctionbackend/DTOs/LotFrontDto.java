package az.code.auctionbackend.DTOs;

import az.code.auctionbackend.entities.redis.RedisLot;
import lombok.Builder;
import lombok.Data;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@Builder
public class LotFrontDto {
    private long id;
    private String description;
    private String lotName;
    private double reservePrice;
    private double startingPrice;
    private double bidStep;
    private double currentBid;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    List<String> ids;

    public static LotFrontDto getLotFrontDto(RedisLot redisLot){

        List<String> idsLocal = new ArrayList<>();

        JSONObject obj = new JSONObject(redisLot.getImgs());
        Iterator<String> keys = obj.keys();

        while(keys.hasNext()) {
            String key = keys.next();
                idsLocal.add(obj.get(key).toString());
                System.out.println("JSON parse result" + obj.get(key).toString());

        }

        LotFrontDto lot = LotFrontDto.builder()
                .id(redisLot.getId())
                .lotName(redisLot.getLotName())
                .description(redisLot.getDescription())
                .reservePrice(redisLot.getReservePrice())
                .startingPrice(redisLot.getStartingPrice())
                .bidStep(redisLot.getBidStep())
                .currentBid(redisLot.getCurrentBid())
                .startDate(redisLot.getStartDate())
                .endDate(redisLot.getEndDate())
                .ids(idsLocal)
                .build();

        return lot;
    }
}
