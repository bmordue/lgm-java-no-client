package me.bmordue.lgm.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the GameTurn entity.
 */
public class GameTurnDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer turnNumber;

    private Long outcomeId;

    private Long landscapeId;

    private Long gameId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(Integer turnNumber) {
        this.turnNumber = turnNumber;
    }

    public Long getOutcomeId() {
        return outcomeId;
    }

    public void setOutcomeId(Long turnOutcomeId) {
        this.outcomeId = turnOutcomeId;
    }

    public Long getLandscapeId() {
        return landscapeId;
    }

    public void setLandscapeId(Long landscapeId) {
        this.landscapeId = landscapeId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameTurnDTO gameTurnDTO = (GameTurnDTO) o;
        if (gameTurnDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gameTurnDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GameTurnDTO{" +
            "id=" + getId() +
            ", turnNumber=" + getTurnNumber() +
            ", outcome=" + getOutcomeId() +
            ", landscape=" + getLandscapeId() +
            ", game=" + getGameId() +
            "}";
    }
}
