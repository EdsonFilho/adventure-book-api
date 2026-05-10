package com.edsonfilho.adventure.book.application.port.in;

/** Command object for starting a new game session. */
public class StartGameCommand {
    private final String bookId;
    private final String playerName;

    public StartGameCommand(String bookId, String playerName) {
        this.bookId = bookId;
        this.playerName = playerName;
    }

    public String getBookId() {
        return bookId;
    }

    public String getPlayerName() {
        return playerName;
    }
}
