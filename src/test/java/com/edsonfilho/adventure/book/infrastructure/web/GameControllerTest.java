package com.edsonfilho.adventure.book.infrastructure.web;

import com.edsonfilho.adventure.book.application.port.in.GameSessionView;
import com.edsonfilho.adventure.book.application.port.in.GetSessionUseCase;
import com.edsonfilho.adventure.book.application.port.in.ListPlayerSessionsUseCase;
import com.edsonfilho.adventure.book.application.port.in.MakeMoveUseCase;
import com.edsonfilho.adventure.book.application.port.in.StartGameCommand;
import com.edsonfilho.adventure.book.application.port.in.StartGameUseCase;
import com.edsonfilho.adventure.book.domain.entity.GameSession;
import com.edsonfilho.adventure.book.domain.entity.Section;
import com.edsonfilho.adventure.book.domain.entity.SectionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StartGameUseCase startGameUseCase;

    @MockitoBean
    private GetSessionUseCase getSessionUseCase;

    @MockitoBean
    private MakeMoveUseCase makeMoveUseCase;

    @MockitoBean
    private ListPlayerSessionsUseCase listPlayerSessionsUseCase;

    @Test
    void startGame_shouldReturn201AndSessionData() throws Exception {
        // Arrange
        GameSession fakeSession = GameSession.start("book-123", "start-1", "Edson");
        fakeSession.setId("session-999");

        Section fakeSection = new Section();
        fakeSection.setId("start-1");
        fakeSection.setText("Welcome to the adventure!");
        fakeSection.setType(SectionType.BEGIN);

        GameSessionView fakeView = new GameSessionView(fakeSession, fakeSection);

        when(startGameUseCase.execute(any(StartGameCommand.class))).thenReturn(fakeSession);
        when(getSessionUseCase.execute(eq("session-999"))).thenReturn(fakeView);

        // Act & Assert
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookId\": \"book-123\", \"playerName\": \"Edson\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sessionId").value("session-999"))
                .andExpect(jsonPath("$.bookId").value("book-123"))
                .andExpect(jsonPath("$.playerName").value("Edson"))
                .andExpect(jsonPath("$.health").value(10))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.currentSection.id").value("start-1"))
                .andExpect(jsonPath("$.currentSection.text").value("Welcome to the adventure!"));
    }
}
