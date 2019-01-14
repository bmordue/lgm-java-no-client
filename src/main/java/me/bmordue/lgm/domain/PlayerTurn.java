package me.bmordue.lgm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PlayerTurn.
 */
@Entity
@Table(name = "player_turn")
public class PlayerTurn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToMany(mappedBy = "playerTurn")
    private Set<ActorCommand> actorCommands = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("playerTurns")
    private GameTurn turn;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("playerTurns")
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ActorCommand> getActorCommands() {
        return actorCommands;
    }

    public PlayerTurn actorCommands(Set<ActorCommand> actorCommands) {
        this.actorCommands = actorCommands;
        return this;
    }

    public PlayerTurn addActorCommand(ActorCommand actorCommand) {
        this.actorCommands.add(actorCommand);
        actorCommand.setPlayerTurn(this);
        return this;
    }

    public PlayerTurn removeActorCommand(ActorCommand actorCommand) {
        this.actorCommands.remove(actorCommand);
        actorCommand.setPlayerTurn(null);
        return this;
    }

    public void setActorCommands(Set<ActorCommand> actorCommands) {
        this.actorCommands = actorCommands;
    }

    public GameTurn getTurn() {
        return turn;
    }

    public PlayerTurn turn(GameTurn gameTurn) {
        this.turn = gameTurn;
        return this;
    }

    public void setTurn(GameTurn gameTurn) {
        this.turn = gameTurn;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerTurn player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
        PlayerTurn playerTurn = (PlayerTurn) o;
        if (playerTurn.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), playerTurn.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlayerTurn{" +
            "id=" + getId() +
            "}";
    }
}
