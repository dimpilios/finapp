package com.dimitris.finapp.persistence.entities;

import com.dimitris.finapp.enums.CurrencyEnum;

import javax.persistence.*;
import java.util.Objects;

/**
 * JPA entity class mapping currency type data
 */
@Entity
@Table(name = "currency_type")
public class CurrencyType {

    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 3)
    private CurrencyEnum name;

    public CurrencyType() {
    }

    public CurrencyType(CurrencyEnum currencyEnum) {
        this.id = currencyEnum.id;
        this.name = currencyEnum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CurrencyEnum getName() {
        return name;
    }

    public void setName(CurrencyEnum name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyType that = (CurrencyType) o;
        return Objects.equals(id, that.id) && name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "CurrencyType{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
