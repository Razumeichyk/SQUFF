package com.squff.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.squff.domain.Driver} entity.
 */
public class DriverDTO implements Serializable {

    private Long id;

    private String plateNumber;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DriverDTO)) {
            return false;
        }

        DriverDTO driverDTO = (DriverDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, driverDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DriverDTO{" +
            "id=" + getId() +
            ", plateNumber='" + getPlateNumber() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
