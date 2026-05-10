package com.edsonfilho.adventure.book.infrastructure.web;

import com.edsonfilho.adventure.book.application.port.in.GameSessionView;
import com.edsonfilho.adventure.book.application.port.in.GetSessionUseCase;
import com.edsonfilho.adventure.book.application.port.in.ListPlayerSessionsUseCase;
import com.edsonfilho.adventure.book.application.port.in.MakeMoveResult;
import com.edsonfilho.adventure.book.application.port.in.MakeMoveUseCase;
import com.edsonfilho.adventure.book.application.port.in.StartGameCommand;
import com.edsonfilho.adventure.book.application.port.in.StartGameUseCase;
import com.edsonfilho.adventure.book.domain.entity.Consequence;
import com.edsonfilho.adventure.book.domain.entity.GameSession;
import com.edsonfilho.adventure.book.domain.entity.Option;
import com.edsonfilho.adventure.book.domain.entity.Section;
import com.edsonfilho.adventure.book.infrastructure.web.dto.GameSessionResponse;
import com.edsonfilho.adventure.book.infrastructure.web.dto.GameSessionSummaryResponse;
import com.edsonfilho.adventure.book.infrastructure.web.dto.StartGameRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * REST adapter for game sessions.
 *
 * <pre>
 * POST /api/sessions              → start a game  { "bookId": "..." }
 * GET  /api/sessions/{id}         → current state
 * POST /api/sessions/{id}/moves   → make a move   { "optionIndex": 0 }
 * </pre>
 */
@RestController
@RequestMapping("/api/sessions")
public class GameController {

    private final StartGameUseCase startGameUseCase;
    private final GetSessionUseCase getSessionUseCase;
    private final MakeMoveUseCase makeMoveUseCase;
    private final ListPlayerSessionsUseCase listPlayerSessionsUseCase;

    public GameController(StartGameUseCase startGameUseCase,
                          GetSessionUseCase getSessionUseCase,
                          MakeMoveUseCase makeMoveUseCase,
                          ListPlayerSessionsUseCase listPlayerSessionsUseCase) {
        this.startGameUseCase = startGameUseCase;
        this.getSessionUseCase = getSessionUseCase;
        this.makeMoveUseCase = makeMoveUseCase;
        this.listPlayerSessionsUseCase = listPlayerSessionsUseCase;
    }

    /** Start a new game session for the given book. */
    @PostMapping
    public ResponseEntity<GameSessionResponse> startGame(@RequestBody StartGameRequest request) {
        StartGameCommand command = new StartGameCommand(request.getBookId(), request.getPlayerName());
        GameSession session = startGameUseCase.execute(command);
        // After starting, fetch the full view (with BEGIN section content)
        GameSessionView view = getSessionUseCase.execute(session.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(view, null));
    }

    /** List game sessions for a specific player. */
    @GetMapping
    public ResponseEntity<List<GameSessionSummaryResponse>> getSessions(@RequestParam String playerName) {
        List<GameSessionSummaryResponse> responses = listPlayerSessionsUseCase.execute(playerName)
                .stream()
                .map(session -> new GameSessionSummaryResponse(
                        session.getId(),
                        session.getBookId(),
                        session.getPlayerName(),
                        session.getHealth(),
                        session.getStatus().name()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /** Get the current state of an existing session. */
    @GetMapping("/{id}")
    public ResponseEntity<GameSessionResponse> getSession(@PathVariable String id) {
        GameSessionView view = getSessionUseCase.execute(id);
        return ResponseEntity.ok(toResponse(view, null));
    }

    /** Make a move by choosing an option. */
    @PostMapping("/{id}/moves")
    public ResponseEntity<GameSessionResponse> makeMove(
            @PathVariable String id,
            @RequestBody Map<String, Integer> body) {
        int optionIndex = body.get("optionIndex");
        MakeMoveResult result = makeMoveUseCase.execute(id, optionIndex);
        return ResponseEntity.ok(toResponse(result.getSessionView(), result.getAppliedConsequence()));
    }

    // -------------------------------------------------------------------------
    // Mapping — domain/application → web DTO
    // -------------------------------------------------------------------------

    private GameSessionResponse toResponse(GameSessionView view, Consequence consequence) {
        GameSession session = view.getSession();
        Section section = view.getCurrentSection();

        GameSessionResponse response = new GameSessionResponse();
        response.setSessionId(session.getId());
        response.setBookId(session.getBookId());
        response.setPlayerName(session.getPlayerName());
        response.setHealth(session.getHealth());
        response.setStatus(session.getStatus().name());
        response.setCurrentSection(toSectionResponse(section));

        if (consequence != null) {
            response.setConsequence(new GameSessionResponse.ConsequenceResponse(
                    consequence.getType(), consequence.getValue(), consequence.getText()));
        }

        return response;
    }

    private GameSessionResponse.SectionResponse toSectionResponse(Section section) {
        List<Option> options = section.getOptions();
        List<GameSessionResponse.OptionResponse> optionResponses = options == null
                ? Collections.emptyList()
                : IntStream.range(0, options.size())
                        .mapToObj(i -> new GameSessionResponse.OptionResponse(i, options.get(i).getDescription()))
                        .collect(Collectors.toList());

        return new GameSessionResponse.SectionResponse(
                section.getId(), section.getText(),
                section.getType() != null ? section.getType().name() : null,
                optionResponses);
    }
}
