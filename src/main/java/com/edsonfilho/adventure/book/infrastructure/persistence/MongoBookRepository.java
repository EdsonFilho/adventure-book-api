package com.edsonfilho.adventure.book.infrastructure.persistence;

import com.edsonfilho.adventure.book.domain.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the {@link Book} document.
 *
 * <p>Provides standard CRUD operations inherited from {@link MongoRepository}.
 * Custom search queries that require dynamic criteria are handled by
 * {@link BookRepositoryAdapter} using {@code MongoTemplate} directly.</p>
 */
public interface MongoBookRepository extends MongoRepository<Book, String> {
}
