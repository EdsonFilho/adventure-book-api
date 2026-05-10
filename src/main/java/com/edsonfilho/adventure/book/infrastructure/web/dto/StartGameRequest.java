package com.edsonfilho.adventure.book.infrastructure.web.dto;

/** Request body for starting a new game session. */
public class StartGameRequest {
    private String bookId;
    private String playerName;

    public StartGameRequest() {}

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
