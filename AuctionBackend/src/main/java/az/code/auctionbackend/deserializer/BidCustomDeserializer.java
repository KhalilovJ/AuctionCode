package az.code.auctionbackend.deserializer;

import az.code.auctionbackend.entities.auction.Bid;
import az.code.auctionbackend.entities.auction.Lot;
import az.code.auctionbackend.entities.users.UserProfile;
import az.code.auctionbackend.services.interfaces.LotService;
import az.code.auctionbackend.services.interfaces.UserService;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.time.LocalDate;


public class BidCustomDeserializer extends JsonDeserializer<Bid> {

    private static final long serialVersionUID = 4799573377613685478L;

    @Autowired
    LotService lotService;
    @Autowired
    UserService userService;

    public BidCustomDeserializer() {
//        this(null);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public Bid deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException, JsonProcessingException {

        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        JsonNode customBid = jsonNode.get("bid");
        JsonNode customLot = jsonNode.get("lotId");
        JsonNode customUser = jsonNode.get("userId");

        Lot lot = lotService.findLotById(customLot.asLong()).get();
        UserProfile user = userService.findProfileById(customUser.asLong()).get();

        return Bid.builder()
                .bid(customBid.asDouble())
                .user(user)
                .lot(lot)
                .bidTime(LocalDate.now())
                .build();
    }
}
