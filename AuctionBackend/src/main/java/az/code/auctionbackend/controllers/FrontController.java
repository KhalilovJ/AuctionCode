package az.code.auctionbackend.controllers;

import az.code.auctionbackend.DTOs.*;
import az.code.auctionbackend.deserializer.CustomMapper;
import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.Transaction;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.repositories.financeRepositories.TransactionRepository;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.LotServiceImpl;
import az.code.auctionbackend.services.TranactionService;
import az.code.auctionbackend.services.interfaces.LotService;
import az.code.auctionbackend.services.interfaces.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
public class FrontController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private LotService lotService;
    @Autowired
    private TranactionService tranactionService;

    @GetMapping("/")
    public ModelAndView getIndex(@AuthenticationPrincipal UserDetails user){
//        ModelAndView nextPage = new ModelAndView("index");
//        UserProfile userProfile = userService.findByUsername(user.getUsername()).orElse(null);
//
//        List<LotFrontDto> lotList = lotService.getAllActiveLotsFront();
//
//        nextPage.addObject("lotList", lotList);
//        nextPage.addObject("user", userProfile);
//
//        return nextPage;
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/admin")
    public ModelAndView getAdmin(@AuthenticationPrincipal UserDetails userIn){
        ModelAndView nextPage = new ModelAndView("adminapproval");

        UserProfile userProfile = userService.findByUsername(userIn.getUsername()).orElse(null);

            List<LotFrontDto> lotList = lotService.getApprovalWaitingLotsFront();
            nextPage.addObject("lots", lotList);
            nextPage.addObject("user", userProfile);

        return nextPage;

    }

    @GetMapping("/profile")
    public ModelAndView getProfile(@AuthenticationPrincipal UserDetails userIn){
        ModelAndView model = new ModelAndView("profile");
        UserFrontDTO user = userService.getUserToFront(userIn.getUsername());
        model.addObject("user", user);
        return model;
    }

    @GetMapping("/lots/{lotId}")
    public ModelAndView getLot(@PathVariable Long lotId, @AuthenticationPrincipal UserDetails userDetails){

        ModelAndView model;
        LotFrontDto lotFront = null;

        RedisLot redisLot = redisRepository.getRedis(lotId);

        if (redisLot != null) {
            log.error("getLot " + redisLot);
            lotFront = LotFrontDto.getLotFrontDto(redisLot);
            UserFrontDTO userFrontDTO = UserFrontDTO.convertToUserFront(userService.findProfileById(lotFront.getUserId()).get());
            lotFront.setUser(userFrontDTO);

            /**
             *  Создание пустого списка бидов
             */
            if (lotFront.getBids() == null) {
                lotFront.setBids(new ArrayList<>());
            }

        } else { //redislot is null
            Lot lot = lotService.findLotById(lotId).orElse(null);
            if (lot != null){
                lotFront = LotFrontDto.getLotFrontDto(lot);
            } else { // there's no lot with this id
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        log.info("lot " + lotFront);

        if (lotFront.getStatus() == -1){
            model = new ModelAndView("redirect:/home");
            return model;
        }
        else {
            model = new ModelAndView("auction");
            Collections.reverse(lotFront.getBids());
            model.addObject("auction", lotFront);
            model.addObject("user", lotFront.getUser());
            model.addObject("userMe", userDetails);
        }

        return model;
    }

    @GetMapping("/user/{username}/add_auction")
    public ModelAndView newAuction(@PathVariable String username, @AuthenticationPrincipal UserDetails user){

        String un = user.getUsername();

        if (username.equalsIgnoreCase(un)) {

            UserProfile userProfile = userService.findByUsername(username).get();

            // TODO validation
            // userService.findSellerProfileById(un).isChecked() - verified seller
            if (!userProfile.isSellerActive()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            ModelAndView model = new ModelAndView("newAuction");
            LotDto lotDto = LotDto.builder().build();
            model.addObject("lot", lotDto);

            log.info("newAuction " + lotDto);

            return model;
        } else {
            // if username is incorrect
            return new ModelAndView("redirect:/home");
        }
    }

    @GetMapping("/transactions")
    public ModelAndView getTransactionsPage(@AuthenticationPrincipal UserDetails user){

        List<Transaction> transactions = tranactionService.getUsersInvolvedTransactions(user.getUsername());
        List<TransactionDTO> transactionDTOS = new ArrayList<>();

        transactions.forEach(t->{
            transactionDTOS.add(TransactionDTO.getTransactionDto(t));
        });

        ModelAndView model = new ModelAndView("transactions");

        model.addObject("transactions", transactionDTOS);

        return model;
    }

    @GetMapping("/won-auctions")
    public ModelAndView getWonAuctions(@AuthenticationPrincipal UserDetails user){

        List<LotFrontDto> lots = lotService.getWonAuctions(user.getUsername());

        ModelAndView model = new ModelAndView("wonauctions");

        model.addObject("lots", lots);

        return model;
    }

    @GetMapping("/my-auctions")
    public ModelAndView getMyAuctions(@AuthenticationPrincipal UserDetails user){

        List<LotFrontDto> lots = lotService.getMyAuctions(user.getUsername());

        ModelAndView model = new ModelAndView("myauctions");

        model.addObject("lots", lots);

        return model;
    }

}
