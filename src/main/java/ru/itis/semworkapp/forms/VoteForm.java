package ru.itis.semworkapp.forms;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class VoteForm {
    private UUID userId;
    private UUID productId;
    private int voteValue;
}