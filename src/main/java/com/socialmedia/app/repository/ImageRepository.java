package com.socialmedia.app.repository;

import com.socialmedia.app.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Image entities.
 * It provides methods for CRUD operations and querying Image objects in the database.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
}
