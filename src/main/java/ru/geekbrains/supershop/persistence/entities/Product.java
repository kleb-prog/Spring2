package ru.geekbrains.supershop.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.NoArgsConstructor;
import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.supershop.persistence.entities.utils.PersistableEntity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends PersistableEntity implements Serializable{

    private String title;

    private String description;

    private Date added;

    private Double price;

    private boolean available;

    @Enumerated(EnumType.ORDINAL)
    private ProductCategory category;

    private UUID image;

//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
////    @JoinColumn(name = "image")
    @Transient
    private List<Image> images;
}