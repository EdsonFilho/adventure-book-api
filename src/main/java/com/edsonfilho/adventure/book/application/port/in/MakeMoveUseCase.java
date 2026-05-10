package com.edsonfilho.adventure.book.application.port.in;

/** Input port: make a move in an active game session by choosing an option. */
public interface MakeMoveUseCase {

    /**
     * @param sessionId   the game session id
     * @param optionIndex 0-based index of the chosen option in the current section
     * @return the updated session view and the applied consequence (if any)
     */
    MakeMoveResult execute(String sessionId, int optionIndex);
}
