package me.bmordue.lgm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import me.bmordue.lgm.domain.enumeration.Direction;

/**
 * A GameOrder.
 */
@Entity
@Table(name = "game_order")
@Document(indexName = "gameorder")
public class GameOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private Direction direction;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("gameOrders")
    private Actor actor;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("gameOrders")
    private Turn turn;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Direction getDirection() {
        return direction;
    }

    public GameOrder direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Actor getActor() {
        return actor;
    }

    public GameOrder actor(Actor actor) {
        this.actor = actor;
        return this;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Turn getTurn() {
        return turn;
    }

    public GameOrder turn(Turn turn) {
        this.turn = turn;
        return this;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
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
        GameOrder gameOrder = (GameOrder) o;
        if (gameOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gameOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GameOrder{" +
            "id=" + getId() +
            ", direction='" + getDirection() + "'" +
            "}";
    }
}
