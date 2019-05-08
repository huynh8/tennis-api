package com.example.tennis.mvc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class Game {
    int id;

    @JsonIgnore
    Player player1Info;
    @JsonIgnore
    Player player2Info;

    int player1;
    int player2;

    @JsonIgnore
    int points1;
    @JsonIgnore
    int points2;

    String score;

    public Game(int id, Player player1Info, Player player2Info) {
        this.id = id;
        this.player1Info = player1Info;
        this.player2Info = player2Info;
        this.player1 = player1Info.getId();
        this.player2 = player2Info.getId();
        score = determineScore();
    }

    private String determineScore() {
        if (points1 > 2 && points2 > 2) {
            if (points1 == points2) {
                return "DEUCE!";
            }
            if (points1 == points2 + 1) {
                return "Advantage, " + player1Info.getName();
            }
            if (points2 == points1 + 1) {
                return "Advantage, " + player2Info.getName();
            }
            if (points1 > points2 + 1) {
                return "Winner, " + player1Info.getName();
            }
            if (points2 > points1 + 1) {
                return "Winner, " + player2Info.getName();
            }
        }

        if (points1 > 3) {
            return "Winner, " + player1Info.getName();
        }

        if (points2 > 3) {
            return "Winner, " + player2Info.getName();
        }

        String score1 = determineCall(points1);
        String score2 = determineCall(points2);
        return score1 + "-" + score2;
    }

    private String determineCall(int points) {
        switch (points) {
            case 0:
                return "Love";
            case 1:
                return "15";
            case 2:
                return "30";
            case 3:
                return "40";
            default:
                throw new IllegalArgumentException();
        }
    }

    public void point(int player) {
        if (player == 1) {
            points1++;
        } else if (player == 2) {
            points2++;
        } else {
            throw new IllegalArgumentException("Game has only player 1 or player 2.");
        }
        score = determineScore();
    }
}
