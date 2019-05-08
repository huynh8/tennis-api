package com.example.tennis.mvc;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = AccessLevel.PRIVATE)
class TennisControllerTest {

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TennisController()).setControllerAdvice(new TennisControllerAdvice()).build();
    }

    @DisplayName("Create a player")
    @Test
    void createPlayer() throws Exception {
        String player = "Venus Williams";
        String playerRequestJson = "{\"name\":\"" + player + "\"}";
        mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerRequestJson))
                .andExpect(status().isCreated())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("name").isEqualTo(player));
    }

    @DisplayName("Create a game")
    @Test
    void createGame() throws Exception {
        String player1 = "Venus Williams";
        String player1RequestJson = "{\"name\":\"" + player1 + "\"}";
        mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(player1RequestJson))
                .andExpect(status().isCreated())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("name").isEqualTo(player1));


        String player2 = "Serena Williams";
        String player2RequestJson = "{\"name\":\"" + player2 + "\"}";
        mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(player2RequestJson))
                .andExpect(status().isCreated())
                .andExpect(json().node("id").isEqualTo(2))
                .andExpect(json().node("name").isEqualTo(player2));

        String gameRequestJson = "{\"player1\":1,\"player2\":2}";
        mockMvc.perform(post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameRequestJson))
                .andExpect(status().isCreated())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("Love-Love"))
                .andExpect(json().node("points1").isAbsent())
                .andExpect(json().node("points2").isAbsent())
                .andExpect(json().node("player1Info").isAbsent())
                .andExpect(json().node("player2Info").isAbsent());
    }

    @DisplayName("Play a game to the end - No Deuces")
    @Test
    void playGame() throws Exception {
        String player1 = "Venus Williams";
        String player1RequestJson = "{\"name\":\"" + player1 + "\"}";
        mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(player1RequestJson))
                .andExpect(status().isCreated())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("name").isEqualTo(player1));


        String player2 = "Serena Williams";
        String player2RequestJson = "{\"name\":\"" + player2 + "\"}";
        mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(player2RequestJson))
                .andExpect(status().isCreated())
                .andExpect(json().node("id").isEqualTo(2))
                .andExpect(json().node("name").isEqualTo(player2));

        String gameRequestJson = "{\"player1\":1,\"player2\":2}";
        mockMvc.perform(post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameRequestJson))
                .andExpect(status().isCreated())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("Love-Love"));

        mockMvc.perform(post("/score/1/1"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("15-Love"));

        mockMvc.perform(post("/score/1/1"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("30-Love"));

        mockMvc.perform(post("/score/1/2"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("30-15"));

        mockMvc.perform(post("/score/1/1"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("40-15"));

        mockMvc.perform(post("/score/1/1"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("Winner, " + player1));
    }

    @DisplayName("Play a game to the end - Deuces and Advantages occur")
    @Test
    void playGame2() throws Exception {
        String player1 = "Venus Williams";
        String player1RequestJson = "{\"name\":\"" + player1 + "\"}";
        mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(player1RequestJson))
                .andExpect(status().isCreated())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("name").isEqualTo(player1));


        String player2 = "Serena Williams";
        String player2RequestJson = "{\"name\":\"" + player2 + "\"}";
        mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(player2RequestJson))
                .andExpect(status().isCreated())
                .andExpect(json().node("id").isEqualTo(2))
                .andExpect(json().node("name").isEqualTo(player2));

        String gameRequestJson = "{\"player1\":1,\"player2\":2}";
        mockMvc.perform(post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameRequestJson))
                .andExpect(status().isCreated())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("Love-Love"));

        mockMvc.perform(post("/score/1/1"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("15-Love"));

        mockMvc.perform(post("/score/1/1"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("30-Love"));

        mockMvc.perform(post("/score/1/2"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("30-15"));

        mockMvc.perform(post("/score/1/2"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("30-30"));

        mockMvc.perform(post("/score/1/2"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("30-40"));

        mockMvc.perform(post("/score/1/1"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("DEUCE!"));

        mockMvc.perform(post("/score/1/1"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("Advantage, " + player1));

        mockMvc.perform(post("/score/1/2"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("DEUCE!"));

        mockMvc.perform(post("/score/1/2"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("Advantage, " + player2));

        mockMvc.perform(post("/score/1/2"))
                .andExpect(status().isOk())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("player1").isEqualTo(1))
                .andExpect(json().node("player2").isEqualTo(2))
                .andExpect(json().node("score").isEqualTo("Winner, " + player2));
    }

    @DisplayName("Create game with players that don't exist")
    @Test
    void createGame_error() throws Exception {
        String player1 = "Venus Williams";
        String player1RequestJson = "{\"name\":\"" + player1 + "\"}";
        mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(player1RequestJson))
                .andExpect(status().isCreated())
                .andExpect(json().node("id").isEqualTo(1))
                .andExpect(json().node("name").isEqualTo(player1));

        String gameRequestJson = "{\"player1\":1,\"player2\":2}";
        mockMvc.perform(post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameRequestJson))
                .andExpect(status().isNotFound())
                .andExpect(json().node("msg").isEqualTo("Player(s) not found"));
    }

    @DisplayName("Play a game that doesn't exist")
    @Test
    void playGame_error() throws Exception {
        mockMvc.perform(post("/score/1/1"))
                .andExpect(status().isNotFound())
                .andExpect(json().node("msg").isEqualTo("Game not found"));
    }
}