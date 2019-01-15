package me.bmordue.lgm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import me.bmordue.lgm.domain.enumeration.Direction;

/**
 * A ActorCommand.
 */
@Entity
@Table(name = "actor_command")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActorCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private Direction direction;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("actorCommands")
    private Actor actor;

    @ManyToOne
    @JsonIgnoreProperties("actorCommands")
    private PlayerTurn playerTurn;

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

    public ActorCommand direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Actor getActor() {
        return actor;
    }

    public ActorCommand actor(Actor actor) {
        this.actor = actor;
        return this;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public PlayerTurn getPlayerTurn() {
        return playerTurn;
    }

    public ActorCommand playerTurn(PlayerTurn playerTurn) {
        this.playerTurn = playerTurn;
        return this;
    }

    public void setPlayerTurn(PlayerTurn playerTurn) {
        this.playerTurn = playerTurn;
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
        ActorCommand actorCommand = (ActorCommand) o;
        if (actorCommand.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actorCommand.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActorCommand{" +
            "id=" + getId() +
            ", direction='" + getDirection() + "'" +
            "}";
    }
}
