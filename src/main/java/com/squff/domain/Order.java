package com.squff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.squff.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Order.
 */
@Table("jhi_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("generated_code")
    private Long generatedCode;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("created_at")
    private LocalDate createdAt;

    @Column("shipped_at")
    private LocalDate shippedAt;

    @Column("recieved_at")
    private LocalDate recievedAt;

    @Column("status")
    private Status status;

    @Column("is_active")
    private Boolean isActive;

    @JsonIgnoreProperties(value = { "user", "orders" }, allowSetters = true)
    @Transient
    private Driver driver;

    @Column("driver_id")
    private Long driverId;

    @JsonIgnoreProperties(value = { "user", "orders" }, allowSetters = true)
    @Transient
    private Client client;

    @Column("client_id")
    private Long clientId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order id(Long id) {
        this.id = id;
        return this;
    }

    public Long getGeneratedCode() {
        return this.generatedCode;
    }

    public Order generatedCode(Long generatedCode) {
        this.generatedCode = generatedCode;
        return this;
    }

    public void setGeneratedCode(Long generatedCode) {
        this.generatedCode = generatedCode;
    }

    public String getTitle() {
        return this.title;
    }

    public Order title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Order description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Order createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getShippedAt() {
        return this.shippedAt;
    }

    public Order shippedAt(LocalDate shippedAt) {
        this.shippedAt = shippedAt;
        return this;
    }

    public void setShippedAt(LocalDate shippedAt) {
        this.shippedAt = shippedAt;
    }

    public LocalDate getRecievedAt() {
        return this.recievedAt;
    }

    public Order recievedAt(LocalDate recievedAt) {
        this.recievedAt = recievedAt;
        return this;
    }

    public void setRecievedAt(LocalDate recievedAt) {
        this.recievedAt = recievedAt;
    }

    public Status getStatus() {
        return this.status;
    }

    public Order status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Order isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public Order driver(Driver driver) {
        this.setDriver(driver);
        this.driverId = driver != null ? driver.getId() : null;
        return this;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        this.driverId = driver != null ? driver.getId() : null;
    }

    public Long getDriverId() {
        return this.driverId;
    }

    public void setDriverId(Long driver) {
        this.driverId = driver;
    }

    public Client getClient() {
        return this.client;
    }

    public Order client(Client client) {
        this.setClient(client);
        this.clientId = client != null ? client.getId() : null;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
        this.clientId = client != null ? client.getId() : null;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long client) {
        this.clientId = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", generatedCode=" + getGeneratedCode() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", shippedAt='" + getShippedAt() + "'" +
            ", recievedAt='" + getRecievedAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
