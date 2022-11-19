package com.otec.demo.player;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/players")
@AllArgsConstructor
public class PlayerController {


    private final PlayerService playerService;

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @PostMapping
    public void addPlayer(@Valid @RequestBody Player player){
        playerService.addPlayer(player);
    }

    @DeleteMapping(path = "{playerID}")
    public void deletePlayer(@PathVariable("playerID") Long playerId){
        playerService.deletePlayer(playerId);
    }

}
