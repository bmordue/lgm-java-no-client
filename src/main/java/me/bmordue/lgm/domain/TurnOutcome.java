package me.bmordue.lgm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A TurnOutcome.
 */
@Entity
@Table(name = "turn_outcome")
public class TurnOutcome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne(mappedBy = "gameTurn")
    @JsonIgnore
    private GameTurn turn;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameTurn getTurn() {
        return turn;
    }

    public TurnOutcome turn(GameTurn gameTurn) {
        this.turn = gameTurn;
        return this;
    }

    public void setTurn(GameTurn gameTurn) {
        this.turn = gameTurn;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TurnOutcome turnOutcome = (TurnOutcome) o;
        if (turnOutcome.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turnOutcome.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TurnOutcome{" +
            "id=" + getId() +
            "}";
    }
}
