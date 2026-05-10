package com.edsonfilho.adventure.book.application.port.in;

/** Input port: retrieve the current state of a game session. */
public interface GetSessionUseCase {
    GameSessionView execute(String sessionId);
}
