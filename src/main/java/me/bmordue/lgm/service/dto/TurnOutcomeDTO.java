package me.bmordue.lgm.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TurnOutcome entity.
 */
public class TurnOutcomeDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TurnOutcomeDTO turnOutcomeDTO = (TurnOutcomeDTO) o;
        if (turnOutcomeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turnOutcomeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TurnOutcomeDTO{" +
            "id=" + getId() +
            "}";
    }
}
