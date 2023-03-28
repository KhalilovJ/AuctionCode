package az.code.auctionbackend;

import az.code.auctionbackend.controllers.BidController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuctionBackendApplicationTests {

    @Autowired
    private BidController bidController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(bidController).isNotNull();
    }
}
