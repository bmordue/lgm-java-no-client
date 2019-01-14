package me.bmordue.lgm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import me.bmordue.lgm.domain.enumeration.ActorState;

/**
 * A Actor.
 */
@Entity
@Table(name = "actor")
public class Actor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private ActorState state;

    @NotNull
    @Column(name = "pos_x", nullable = false)
    private Integer posX;

    @NotNull
    @Column(name = "pos_y", nullable = false)
    private Integer posY;

    @OneToMany(mappedBy = "actor")
    private Set<ActorCommand> actorCommands = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("actors")
    private GameTurn turn;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("actors")
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActorState getState() {
        return state;
    }

    public Actor state(ActorState state) {
        this.state = state;
        return this;
    }

    public void setState(ActorState state) {
        this.state = state;
    }

    public Integer getPosX() {
        return posX;
    }

    public Actor posX(Integer posX) {
        this.posX = posX;
        return this;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public Actor posY(Integer posY) {
        this.posY = posY;
        return this;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    public Set<ActorCommand> getActorCommands() {
        return actorCommands;
    }

    public Actor actorCommands(Set<ActorCommand> actorCommands) {
        this.actorCommands = actorCommands;
        return this;
    }

    public Actor addActorCommand(ActorCommand actorCommand) {
        this.actorCommands.add(actorCommand);
        actorCommand.setActor(this);
        return this;
    }

    public Actor removeActorCommand(ActorCommand actorCommand) {
        this.actorCommands.remove(actorCommand);
        actorCommand.setActor(null);
        return this;
    }

    public void setActorCommands(Set<ActorCommand> actorCommands) {
        this.actorCommands = actorCommands;
    }

    public GameTurn getTurn() {
        return turn;
    }

    public Actor turn(GameTurn gameTurn) {
        this.turn = gameTurn;
        return this;
    }

    public void setTurn(GameTurn gameTurn) {
        this.turn = gameTurn;
    }

    public Player getPlayer() {
        return player;
    }

    public Actor player(Player player) {
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
        Actor actor = (Actor) o;
        if (actor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Actor{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", posX=" + getPosX() +
            ", posY=" + getPosY() +
            "}";
    }
}
