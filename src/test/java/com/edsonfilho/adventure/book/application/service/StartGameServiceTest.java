package com.edsonfilho.adventure.book.application.service;

import com.edsonfilho.adventure.book.application.port.in.StartGameCommand;
import com.edsonfilho.adventure.book.application.port.out.BookRepositoryPort;
import com.edsonfilho.adventure.book.application.port.out.GameSessionRepositoryPort;
import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.entity.GameSession;
import com.edsonfilho.adventure.book.domain.entity.Section;
import com.edsonfilho.adventure.book.domain.entity.SectionType;
import com.edsonfilho.adventure.book.domain.exception.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartGameServiceTest {

    @Mock
    private BookRepositoryPort bookRepositoryPort;

    @Mock
    private GameSessionRepositoryPort gameSessionRepositoryPort;

    @InjectMocks
    private StartGameService startGameService;

    private Book validBook;

    @BeforeEach
    void setUp() {
        Section beginSection = new Section();
        beginSection.setId("start-1");
        beginSection.setType(SectionType.BEGIN);

        validBook = new Book(
                "book-123",
                "Test Book",
                "Author",
                null,
                null,
                List.of(beginSection)
        );
    }

    @Test
    void execute_shouldCreateAndSaveGameSession() {
        // Arrange: Setup command and mock repository responses
        StartGameCommand command = new StartGameCommand("book-123", "Player1");
        when(bookRepositoryPort.findById("book-123")).thenReturn(Optional.of(validBook));
        
        // Mock the save operation to simulate MongoDB assigning an ID
        when(gameSessionRepositoryPort.save(any(GameSession.class))).thenAnswer(i -> {
            GameSession session = i.getArgument(0);
            session.setId("session-999");
            return session;
        });

        // Act: Execute the use case
        GameSession createdSession = startGameService.execute(command);

        // Assert: Verify the returned session has the expected data mapped from the book and command
        assertEquals("book-123", createdSession.getBookId());
        assertEquals("start-1", createdSession.getCurrentSectionId());
        assertEquals("Player1", createdSession.getPlayerName());

        // Assert: Verify the mocked ports were interacted with correctly
        verify(bookRepositoryPort).findById("book-123");
        verify(gameSessionRepositoryPort).save(any(GameSession.class));
    }

    @Test
    void execute_shouldThrowExceptionWhenBookNotFound() {
        // Arrange: Setup command for a non-existent book
        StartGameCommand command = new StartGameCommand("invalid-book", "Player1");
        when(bookRepositoryPort.findById("invalid-book")).thenReturn(Optional.empty());

        // Act & Assert: Verify that the exact domain exception is thrown
        assertThrows(BookNotFoundException.class, () -> startGameService.execute(command));

        // Assert: Verify that save was never called because it failed early
        verify(gameSessionRepositoryPort, never()).save(any());
    }
}
