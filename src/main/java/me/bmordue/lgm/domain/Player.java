package me.bmordue.lgm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Player.
 */
@Entity
@Table(name = "player", uniqueConstraints=
        @UniqueConstraint(columnNames={"name", "game_id"}))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "player")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Actor> actors = new HashSet<>();
    @OneToMany(mappedBy = "player")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PlayerTurn> playerTurns = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("players")
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Player name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public Player actors(Set<Actor> actors) {
        this.actors = actors;
        return this;
    }

    public Player addActor(Actor actor) {
        this.actors.add(actor);
        actor.setPlayer(this);
        return this;
    }

    public Player removeActor(Actor actor) {
        this.actors.remove(actor);
        actor.setPlayer(null);
        return this;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public Set<PlayerTurn> getPlayerTurns() {
        return playerTurns;
    }

    public Player playerTurns(Set<PlayerTurn> playerTurns) {
        this.playerTurns = playerTurns;
        return this;
    }

    public Player addPlayerTurn(PlayerTurn playerTurn) {
        this.playerTurns.add(playerTurn);
        playerTurn.setPlayer(this);
        return this;
    }

    public Player removePlayerTurn(PlayerTurn playerTurn) {
        this.playerTurns.remove(playerTurn);
        playerTurn.setPlayer(null);
        return this;
    }

    public void setPlayerTurns(Set<PlayerTurn> playerTurns) {
        this.playerTurns = playerTurns;
    }

    public Game getGame() {
        return game;
    }

    public Player game(Game game) {
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
        Player player = (Player) o;
        if (player.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), player.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
