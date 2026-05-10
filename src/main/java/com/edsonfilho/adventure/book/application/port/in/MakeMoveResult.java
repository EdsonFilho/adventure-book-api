package com.edsonfilho.adventure.book.application.port.in;

import com.edsonfilho.adventure.book.domain.entity.Consequence;

/**
 * Result returned by {@link MakeMoveUseCase} after a move is processed.
 * Wraps the updated {@link GameSessionView} and the consequence that was applied (if any).
 */
public class MakeMoveResult {

    private final GameSessionView sessionView;
    private final Consequence appliedConsequence; // null if the chosen option had no consequence

    public MakeMoveResult(GameSessionView sessionView, Consequence appliedConsequence) {
        this.sessionView = sessionView;
        this.appliedConsequence = appliedConsequence;
    }

    public GameSessionView getSessionView() { return sessionView; }
    public Consequence getAppliedConsequence() { return appliedConsequence; }
}
