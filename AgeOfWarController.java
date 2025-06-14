package org.example.controller;

import org.example.service.WarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/battle")
public class AgeOfWarController {

    @Autowired
    WarService warService;

    @PostMapping("/arrangement")
    public String getWinningArrangement(@RequestParam String own, @RequestParam String opponent) {
        return warService.findWinningArrangement(own, opponent);
    }

}
