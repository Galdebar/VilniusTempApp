package lt.galdebar.vilniustemp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {

    @GetMapping("/nowcast")
    public ResponseEntity<String> nowcast() {
        return ResponseEntity.ok("Hello World");
    }

}
