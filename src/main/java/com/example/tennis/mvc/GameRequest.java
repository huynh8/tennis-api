package com.example.tennis.mvc;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class GameRequest {
    int player1;
    int player2;
}
