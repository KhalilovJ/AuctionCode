package az.code.auctionbackend.controllers;

import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.services.LotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
public class FrontController {

    @Autowired
    private LotServiceImpl lotService;

    @GetMapping("/lots/{lotId}")
    public ModelAndView getLot(@PathVariable Long lotId){

        ModelAndView model;

        Lot lot = lotService.findRedisLotByIdActive(lotId);

        if (lot.getStartDate() != null && lot.getStartDate().isAfter(LocalDateTime.now())){
            model = new ModelAndView("index");
        } else {
            model = new ModelAndView("auction");
            model.addObject("auction", lot);
        }

        return model;
    }

}
