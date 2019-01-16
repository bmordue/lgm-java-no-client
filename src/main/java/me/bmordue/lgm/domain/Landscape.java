package me.bmordue.lgm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Landscape.
 */
@Entity
@Table(name = "landscape")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Landscape implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "width", nullable = false)
    private Integer width;

    @NotNull
    @Column(name = "height", nullable = false)
    private Integer height;

    @NotNull
    @Column(name = "cells", nullable = false)
    private String cells;

    @OneToOne(mappedBy = "landscape")
    @JsonIgnore
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWidth() {
        return width;
    }

    public Landscape width(Integer width) {
        this.width = width;
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public Landscape height(Integer height) {
        this.height = height;
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getCells() {
        return cells;
    }

    public Landscape cells(String cells) {
        this.cells = cells;
        return this;
    }

    public void setCells(String cells) {
        this.cells = cells;
    }

    public Game getGame() {
        return game;
    }

    public Landscape game(Game game) {
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
        Landscape landscape = (Landscape) o;
        if (landscape.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), landscape.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Landscape{" +
            "id=" + getId() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", cells='" + getCells() + "'" +
            "}";
    }
}
