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
 * A GameTurn.
 */
@Entity
@Table(name = "game_turn")
public class GameTurn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "turn_number", nullable = false, unique = true)
    private Integer turnNumber;

    @OneToOne    @JoinColumn(unique = true)
    private TurnOutcome turnOutcome;

    @OneToOne    @JoinColumn(unique = true)
    private Landscape landscape;

    @OneToMany(mappedBy = "turn")
    private Set<Actor> actors = new HashSet<>();
    @OneToMany(mappedBy = "turn")
    private Set<Landscape> landscapes = new HashSet<>();
    @OneToMany(mappedBy = "turn")
    private Set<PlayerTurn> playerTurns = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("gameTurns")
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTurnNumber() {
        return turnNumber;
    }

    public GameTurn turnNumber(Integer turnNumber) {
        this.turnNumber = turnNumber;
        return this;
    }

    public void setTurnNumber(Integer turnNumber) {
        this.turnNumber = turnNumber;
    }

    public TurnOutcome getTurnOutcome() {
        return turnOutcome;
    }

    public GameTurn turnOutcome(TurnOutcome turnOutcome) {
        this.turnOutcome = turnOutcome;
        return this;
    }

    public void setTurnOutcome(TurnOutcome turnOutcome) {
        this.turnOutcome = turnOutcome;
    }

    public Landscape getLandscape() {
        return landscape;
    }

    public GameTurn landscape(Landscape landscape) {
        this.landscape = landscape;
        return this;
    }

    public void setLandscape(Landscape landscape) {
        this.landscape = landscape;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public GameTurn actors(Set<Actor> actors) {
        this.actors = actors;
        return this;
    }

    public GameTurn addActor(Actor actor) {
        this.actors.add(actor);
        actor.setTurn(this);
        return this;
    }

    public GameTurn removeActor(Actor actor) {
        this.actors.remove(actor);
        actor.setTurn(null);
        return this;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public Set<Landscape> getLandscapes() {
        return landscapes;
    }

    public GameTurn landscapes(Set<Landscape> landscapes) {
        this.landscapes = landscapes;
        return this;
    }

    public GameTurn addLandscape(Landscape landscape) {
        this.landscapes.add(landscape);
        landscape.setTurn(this);
        return this;
    }

    public GameTurn removeLandscape(Landscape landscape) {
        this.landscapes.remove(landscape);
        landscape.setTurn(null);
        return this;
    }

    public void setLandscapes(Set<Landscape> landscapes) {
        this.landscapes = landscapes;
    }

    public Set<PlayerTurn> getPlayerTurns() {
        return playerTurns;
    }

    public GameTurn playerTurns(Set<PlayerTurn> playerTurns) {
        this.playerTurns = playerTurns;
        return this;
    }

    public GameTurn addPlayerTurn(PlayerTurn playerTurn) {
        this.playerTurns.add(playerTurn);
        playerTurn.setTurn(this);
        return this;
    }

    public GameTurn removePlayerTurn(PlayerTurn playerTurn) {
        this.playerTurns.remove(playerTurn);
        playerTurn.setTurn(null);
        return this;
    }

    public void setPlayerTurns(Set<PlayerTurn> playerTurns) {
        this.playerTurns = playerTurns;
    }

    public Game getGame() {
        return game;
    }

    public GameTurn game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
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
        GameTurn gameTurn = (GameTurn) o;
        if (gameTurn.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gameTurn.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GameTurn{" +
            "id=" + getId() +
            ", turnNumber=" + getTurnNumber() +
            "}";
    }
}
