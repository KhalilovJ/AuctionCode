package az.code.auctionbackend.aTest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/open")
    ResponseEntity<String> testOpen(){
        return ResponseEntity.ok("Open Get");
    }
    @PostMapping("/open")
    ResponseEntity<String> testOpenPost(){
        return ResponseEntity.ok("Open Post");
    }
    @GetMapping("/admin")
    ResponseEntity<String> testadmin(){
        return ResponseEntity.ok("Admin Get");
    }
    @GetMapping("/user")
    ResponseEntity<String> testUser(){
        return ResponseEntity.ok("user Get");
    }
    @PostMapping("/admin")
    ResponseEntity<String> testadminPost(){
        return ResponseEntity.ok("Admin Post");
    }
    @PostMapping("/user")
    ResponseEntity<String> testUserPost(){
        return ResponseEntity.ok("user Post");
    }

    @PostMapping("/both")
    ResponseEntity<String> testBothPost(){
        return ResponseEntity.ok("Both Post");
    }
    @GetMapping("/both")
    ResponseEntity<String> testBothGet(){
        return ResponseEntity.ok("Both Get");
    }
}
