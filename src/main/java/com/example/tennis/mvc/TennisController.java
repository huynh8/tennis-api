package com.example.tennis.mvc;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
public class TennisController {

    int playerCounter = 0;
    int gameCounter = 0;
    Map<Integer, Player> players = new HashMap<>();
    Map<Integer, Game> games = new HashMap<>();

    @PostMapping(value = "/player", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Player createPlayer(@RequestBody PlayerRequest playerRequest) {
        Player player = new Player(++playerCounter, playerRequest.getName());
        players.put(player.getId(), player);
        return player;
    }

    @PostMapping(value = "/game", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody GameRequest gameRequest) {
        if (players.containsKey(gameRequest.getPlayer1()) && players.containsKey(gameRequest.getPlayer2())) {
            Game game = new Game(++gameCounter, players.get(gameRequest.getPlayer1()), players.get(gameRequest.getPlayer2()));
            games.put(game.getId(), game);
            return game;
        } else {
            throw new NoSuchElementException("Player(s) not found");
        }
    }

    @PostMapping(value = "/score/{gameId}/{player}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Game score(@PathVariable int gameId, @PathVariable int player) {
        if (games.containsKey(gameId)) {
            Game game = games.get(gameId);
            game.point(player);
            return game;
        } else {
            throw new NoSuchElementException("Game not found");
        }
    }
}
