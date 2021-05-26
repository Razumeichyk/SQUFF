package com.squff.service.dto;

import com.squff.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.squff.domain.Order} entity.
 */
public class OrderDTO implements Serializable {

    private Long id;

    private Long generatedCode;

    private String title;

    private String description;

    private LocalDate createdAt;

    private LocalDate shippedAt;

    private LocalDate recievedAt;

    private Status status;

    private Boolean isActive;

    private DriverDTO driver;

    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGeneratedCode() {
        return generatedCode;
    }

    public void setGeneratedCode(Long generatedCode) {
        this.generatedCode = generatedCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getShippedAt() {
        return shippedAt;
    }

    public void setShippedAt(LocalDate shippedAt) {
        this.shippedAt = shippedAt;
    }

    public LocalDate getRecievedAt() {
        return recievedAt;
    }

    public void setRecievedAt(LocalDate recievedAt) {
        this.recievedAt = recievedAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public DriverDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverDTO driver) {
        this.driver = driver;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + getId() +
            ", generatedCode=" + getGeneratedCode() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", shippedAt='" + getShippedAt() + "'" +
            ", recievedAt='" + getRecievedAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", driver=" + getDriver() +
            ", client=" + getClient() +
            "}";
    }
}
