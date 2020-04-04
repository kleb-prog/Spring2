package ru.geekbrains.supershop.persistence.entities;

import lombok.*;

import ru.geekbrains.supershop.persistence.entities.utils.PersistableEntity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Image extends PersistableEntity implements Serializable {

    private static final long SUID = 1L;

    @Column(name = "id_image", insertable = false, updatable = false)
    private UUID idImage;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_image", referencedColumnName = "image")
    private Product product;

    private String name;

}