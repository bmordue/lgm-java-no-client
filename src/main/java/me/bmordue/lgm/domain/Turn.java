package me.bmordue.lgm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Turn.
 */
@Entity
@Table(name = "turn")
@Document(indexName = "turn")
public class Turn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_number", nullable = false, unique = true)
    private Integer number;

    @OneToOne    @JoinColumn(unique = true)
    private Landscape landscape;

    @OneToMany(mappedBy = "turn")
    private Set<GameOrder> gameOrders = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("turns")
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public Turn number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Landscape getLandscape() {
        return landscape;
    }

    public Turn landscape(Landscape landscape) {
        this.landscape = landscape;
        return this;
    }

    public void setLandscape(Landscape landscape) {
        this.landscape = landscape;
    }

    public Set<GameOrder> getGameOrders() {
        return gameOrders;
    }

    public Turn gameOrders(Set<GameOrder> gameOrders) {
        this.gameOrders = gameOrders;
        return this;
    }

    public Turn addGameOrder(GameOrder gameOrder) {
        this.gameOrders.add(gameOrder);
        gameOrder.setTurn(this);
        return this;
    }

    public Turn removeGameOrder(GameOrder gameOrder) {
        this.gameOrders.remove(gameOrder);
        gameOrder.setTurn(null);
        return this;
    }

    public void setGameOrders(Set<GameOrder> gameOrders) {
        this.gameOrders = gameOrders;
    }

    public Game getGame() {
        return game;
    }

    public Turn game(Game game) {
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
        Turn turn = (Turn) o;
        if (turn.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turn.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Turn{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            "}";
    }
}
