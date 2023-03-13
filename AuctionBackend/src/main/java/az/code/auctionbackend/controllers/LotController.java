package az.code.auctionbackend.controllers;


import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.DTOs.UserDto;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.UploadcareService;
import az.code.auctionbackend.services.interfaces.LotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/open/api/lots")
@RequiredArgsConstructor
@Log4j2
public class LotController {

    private final LotService lotService;
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
        Lot lot = lotService.findLotById(lotId).orElse(null);

//        model.addObject("auction", lot);

        return lot;
    }

    // send details to winner

    @PostMapping("/save")
    public ModelAndView saveLot(@ModelAttribute LotDto lotDto, @RequestParam("files") MultipartFile[] files){

        List<File> filesList = new ArrayList<>();
        List<String> fileIds = new ArrayList<>();

        Arrays.stream(files).toList().forEach(a-> filesList.add(convert(a)));

        filesList.forEach(i-> {
            fileIds.add(uploadcareService.sendFile(i));
        });

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
}
