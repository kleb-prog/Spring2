package ru.geekbrains.supershop.persistence.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.ToString;
import ru.geekbrains.supershop.persistence.entities.utils.PersistableEntity;

import javax.persistence.*;

import java.io.Serializable;

@Data
@Entity
@ToString(exclude = {"product"})
@EqualsAndHashCode(callSuper = true)
public class Image extends PersistableEntity implements Serializable {

    private static final long SUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_image", referencedColumnName = "image")
    private Product product;

    private String name;

}