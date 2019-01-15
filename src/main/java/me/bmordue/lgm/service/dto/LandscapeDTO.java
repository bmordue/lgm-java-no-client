package me.bmordue.lgm.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Landscape entity.
 */
public class LandscapeDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer width;

    @NotNull
    private Integer height;

    @NotNull
    private String cells;

    private Long turnId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getCells() {
        return cells;
    }

    public void setCells(String cells) {
        this.cells = cells;
    }

    public Long getTurnId() {
        return turnId;
    }

    public void setTurnId(Long gameTurnId) {
        this.turnId = gameTurnId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LandscapeDTO landscapeDTO = (LandscapeDTO) o;
        if (landscapeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), landscapeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LandscapeDTO{" +
            "id=" + getId() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", cells='" + getCells() + "'" +
            ", turn=" + getTurnId() +
            "}";
    }
}
