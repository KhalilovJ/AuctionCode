package az.code.auctionbackend.controllers;


import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.LotServiceImpl;
import az.code.auctionbackend.services.UploadcareService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/open/api/lots")
@RequiredArgsConstructor
@Log4j2
public class LotController {

    private final LotServiceImpl lotService;
    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;
    private final UploadcareService uploadcareService;

    //get all lots

    /**
     * Get all lots from DB
     */
    @GetMapping("/closed")
    public ResponseEntity<List<Lot>> getAllClosedLots() {

        return ResponseEntity.ok(lotService.getAllLots());
    }

    @GetMapping("/clear_redis")
    public void clear(){  // not working yet
        redisRepository.clearRedis();
    }

    /**
     * Get all active lots from Redis
     */
    @GetMapping("/active")
    public ResponseEntity<Map<Long, RedisLot>> getAllActiveLots() {

        return ResponseEntity.ok(redisRepository.getAllRedis());
    }

    // open lot !

    // place bid -> List<Bid>

    /**
     * Create a new lot
     * @param lot is the new lot
     * @return
     */
    @PostMapping("/lot")
    public ResponseEntity<Lot> save(@RequestBody Lot lot) {

        RedisLot redisLot = objectMapper.convertValue(lot, RedisLot.class);
        // TODO set real ID
        redisLot.setId(new Random().nextLong());

        return (redisRepository.saveRedis(redisLot) == null) ? new ResponseEntity<>(null, HttpStatus.BAD_REQUEST)
                : ResponseEntity.ok(objectMapper.convertValue(redisRepository.saveRedis(redisLot), Lot.class));
    }


    @CrossOrigin
    @GetMapping("/{lotId}")
    public Lot getLot(@PathVariable Long lotId){

//        ModelAndView model = new ModelAndView("auction");
//        System.out.println("lot " + lotService.findLotById(lotId));
//
        Lot lot = lotService.findLotById(lotId).orElse(null);
//        RedisLot redisLot = redisRepository.getRedis(lotId);
//        System.out.println("redisLot " + redisLot);


        log.error("getLot /{lotId} " + lot);
//        log.error("getLot /{lotId} redis lot " + redisLot);
//        model.addObject("auction", lot);
//return Lot.builder().id(redisLot.getId()).build();
        return lot;
    }

    // send details to winner

    @PostMapping("/save")
    public ModelAndView saveLot(@ModelAttribute LotDto lotDto, @RequestParam("files") MultipartFile[] files, @AuthenticationPrincipal UserDetails user){

        if (lotDto.getStartDate().isAfter(lotDto.getEndDate())){
            return new ModelAndView("redirect:/user/" + user.getUsername() + "/add_auction");
        }

        if (lotDto.getBidStep()<0.01){lotDto.setBidStep(0.01);}
        if (lotDto.getReservePrice() <0){lotDto.setReservePrice(0);}
        if (lotDto.getStartingPrice() <0){lotDto.setStartingPrice(0);}

        List<File> filesList = new ArrayList<>();
        List<String> fileIds = new ArrayList<>();
        StringBuilder imgs = new StringBuilder();

        JSONObject jsonOut = new JSONObject();

        if (files != null){
        Arrays.stream(files).toList().forEach(a-> filesList.add(convert(a)));

        filesList.forEach(i-> {
            fileIds.add(uploadcareService.sendFile(i));
        });

//        imgs.append("{");
//        for (int i = 0; i<fileIds.size(); i++){
//            if (i > 0){ imgs.append(",");}
//            imgs.append("\"" + i + "\":\"" + fileIds.get(i) + "\"");
//        }
//        imgs.append("}");
            for (Integer i = 0; i<fileIds.size(); i++){
                jsonOut.put(i.toString(), fileIds.get(i));
            }
        }

        log.info("saveLot /save DONE " + LocalDateTime.now());
        lotService.createLot(lotDto, jsonOut.toString(), user.getUsername());

        log.info("Lot created: " + lotDto);
        filesList.forEach(File::delete); // deleting temp files

        return new ModelAndView("redirect:/home");
    }

    public static File convert(MultipartFile file) {
        File convFile = new File("files\\" + file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e){
            log.warn(e);
        }

        return convFile;
    }

    @PostMapping("/payauction")
    public String payAuction(@AuthenticationPrincipal UserDetails user, @RequestBody JSONObject jsonRequest){

        Long lotId = jsonRequest.getLong("id");

        System.out.println("payment " + lotId + " user: " +user.getUsername());

        String result = lotService.closeOverduedLot(lotId, user.getUsername());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        jsonObject.put("id", lotId);
        return jsonObject.toString();
    }

    @PostMapping("/approve-lot")
    public String approveLot(@RequestBody JSONObject jsonRequest){

        Long lotId = jsonRequest.getLong("id");
        int status = jsonRequest.getInt("status");

        lotService.approveLot(lotId, status);

        JSONObject json = new JSONObject();
        json.put("result", "success");
        json.put("id", lotId);

        return json.toString();
    }
}
