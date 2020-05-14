package com.flewp.flewpbot.model.jamisphere;

import java.util.List;

public class JamisphereGame {
    public String type; // guessingGame, multipleChoice, claim
    public String context; // Guessing Game - allDrums, cowbellOnly, Choice Game - poll, vote, quiz, Claim -

    public String description; //
    public String inputLabel; //

    public String choiceType; // if equal to "song", the API will try to resolve the choices into titles
    public List<String> resolvedChoices; // For multipleChoice games, if there
    public List<String> choices; // For multiple_choice
    public Long expiresAt;

    public String answer; // For guessing game / quiz, the answer, for polls/votes the majority
    public Integer numEntries;

    // Following fields are not stored by the API
    public String gameId; // Processed and returned in place of the "id"
    public Long startsAt; // Taken from the id and processed when returned from API

    public JamisphereUser winner;
    public JamisphereGameEntry winningEntry;

    public JamisphereRequest request;
    public JamisphereVideo song;
}
