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

import me.bmordue.lgm.domain.enumeration.ActorState;

/**
 * A Actor.
 */
@Entity
@Table(name = "actor")
@Document(indexName = "actor")
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

    @OneToMany(mappedBy = "actor")
    private Set<GameOrder> gameOrders = new HashSet<>();
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

    public Set<GameOrder> getGameOrders() {
        return gameOrders;
    }

    public Actor gameOrders(Set<GameOrder> gameOrders) {
        this.gameOrders = gameOrders;
        return this;
    }

    public Actor addGameOrder(GameOrder gameOrder) {
        this.gameOrders.add(gameOrder);
        gameOrder.setActor(this);
        return this;
    }

    public Actor removeGameOrder(GameOrder gameOrder) {
        this.gameOrders.remove(gameOrder);
        gameOrder.setActor(null);
        return this;
    }

    public void setGameOrders(Set<GameOrder> gameOrders) {
        this.gameOrders = gameOrders;
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
            "}";
    }
}
