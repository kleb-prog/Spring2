package ru.geekbrains.supershop.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.supershop.persistence.entities.ReviewImage;

import java.util.UUID;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, UUID> {
}
