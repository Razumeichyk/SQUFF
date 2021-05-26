package com.squff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Driver.
 */
@Table("driver")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("plate_number")
    private String plateNumber;

    private Long userId;

    @Transient
    private User user;

    @Transient
    @JsonIgnoreProperties(value = { "driver", "client" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver id(Long id) {
        this.id = id;
        return this;
    }

    public String getPlateNumber() {
        return this.plateNumber;
    }

    public Driver plateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
        return this;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public User getUser() {
        return this.user;
    }

    public Driver user(User user) {
        this.setUser(user);
        this.userId = user != null ? user.getId() : null;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long user) {
        this.userId = user;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public Driver orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Driver addOrders(Order order) {
        this.orders.add(order);
        order.setDriver(this);
        return this;
    }

    public Driver removeOrders(Order order) {
        this.orders.remove(order);
        order.setDriver(null);
        return this;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setDriver(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setDriver(this));
        }
        this.orders = orders;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Driver)) {
            return false;
        }
        return id != null && id.equals(((Driver) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Driver{" +
            "id=" + getId() +
            ", plateNumber='" + getPlateNumber() + "'" +
            "}";
    }
}
