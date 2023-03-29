package az.code.auctionbackend.DTOs;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.json.JSONObject;

import java.time.LocalDateTime;

@Builder
@Data
@ToString
public class BidResponseDto {

    private long id;

    private double lotCurrentBidPrice;

    private long lotId;
    private long userId;

    private String username;

    private double bid;
    private LocalDateTime bidTime;

    public JSONObject getJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("lotCurrentBidPrice", lotCurrentBidPrice);
        jsonObject.put("lotId", lotId);
        jsonObject.put("userId", userId);
        jsonObject.put("username", username);
        jsonObject.put("bid", bid);
        jsonObject.put("bidTime", bidTime);
        return jsonObject;
    }
}
