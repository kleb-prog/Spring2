package ru.geekbrains.supershop.persistence.entities.utils;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import java.util.UUID;

@Data
@MappedSuperclass
public abstract class PersistableEntity {

    @Id
    @GeneratedValue
    private UUID id;

}
