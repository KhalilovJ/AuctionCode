package az.code.auctionbackend.deserializer;

import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.services.interfaces.LotService;
import az.code.auctionbackend.services.interfaces.UserService;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;


@Service
public class
BidCustomDeserializer extends JsonDeserializer<Bid> {

    private static final long serialVersionUID = 4799573377613685478L;

    @Autowired
    private LotService lotService;
    @Autowired
    private UserService userService;

    public BidCustomDeserializer(final ApplicationContext contex) {
//        this(null);
//        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        contex.getAutowireCapableBeanFactory().autowireBean(this);
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
                .bidTime(LocalDateTime.now())
                .build();
    }
}
