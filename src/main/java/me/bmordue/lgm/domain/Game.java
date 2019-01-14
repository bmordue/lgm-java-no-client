package me.bmordue.lgm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToMany(mappedBy = "game")
    private Set<Player> players = new HashSet<>();
    @OneToMany(mappedBy = "game")
    private Set<GameTurn> gameTurns = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Game players(Set<Player> players) {
        this.players = players;
        return this;
    }

    public Game addPlayer(Player player) {
        this.players.add(player);
        player.setGame(this);
        return this;
    }

    public Game removePlayer(Player player) {
        this.players.remove(player);
        player.setGame(null);
        return this;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<GameTurn> getGameTurns() {
        return gameTurns;
    }

    public Game gameTurns(Set<GameTurn> gameTurns) {
        this.gameTurns = gameTurns;
        return this;
    }

    public Game addGameTurn(GameTurn gameTurn) {
        this.gameTurns.add(gameTurn);
        gameTurn.setGame(this);
        return this;
    }

    public Game removeGameTurn(GameTurn gameTurn) {
        this.gameTurns.remove(gameTurn);
        gameTurn.setGame(null);
        return this;
    }

    public void setGameTurns(Set<GameTurn> gameTurns) {
        this.gameTurns = gameTurns;
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
        Game game = (Game) o;
        if (game.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), game.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            "}";
    }
}
