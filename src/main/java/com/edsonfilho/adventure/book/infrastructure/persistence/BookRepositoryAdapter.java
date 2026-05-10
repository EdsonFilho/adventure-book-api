package com.edsonfilho.adventure.book.infrastructure.persistence;

import com.edsonfilho.adventure.book.application.port.in.BookSearchQuery;
import com.edsonfilho.adventure.book.application.port.out.BookRepositoryPort;
import com.edsonfilho.adventure.book.domain.entity.Book;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB adapter that implements {@link BookRepositoryPort}.
 *
 * <p>This is the only class in the codebase that is allowed to use
 * {@link MongoTemplate} or {@link MongoBookRepository} for book queries.
 * Everything inward sees only the port interface.</p>
 */
@Component
public class BookRepositoryAdapter implements BookRepositoryPort {

    private final MongoTemplate mongoTemplate;
    private final MongoBookRepository mongoBookRepository;

    public BookRepositoryAdapter(MongoTemplate mongoTemplate,
                                 MongoBookRepository mongoBookRepository) {
        this.mongoTemplate = mongoTemplate;
        this.mongoBookRepository = mongoBookRepository;
    }

    @Override
    public List<Book> search(BookSearchQuery query) {
        List<Criteria> criteria = new ArrayList<>();

        if (hasValue(query.getTitle())) {
            criteria.add(Criteria.where("title").regex(query.getTitle().trim(), "i"));
        }
        if (hasValue(query.getAuthor())) {
            criteria.add(Criteria.where("author").regex(query.getAuthor().trim(), "i"));
        }
        if (hasValue(query.getCategory())) {
            criteria.add(Criteria.where("categories").regex(query.getCategory().trim(), "i"));
        }
        if (hasValue(query.getDifficulty())) {
            criteria.add(Criteria.where("difficulty").is(query.getDifficulty().trim().toUpperCase()));
        }

        Query mongoQuery = new Query();
        if (!criteria.isEmpty()) {
            mongoQuery.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(mongoQuery, Book.class);
    }

    @Override
    public Optional<Book> findById(String id) {
        return mongoBookRepository.findById(id);
    }

    @Override
    public Book save(Book book) {
        return mongoBookRepository.save(book);
    }

    // -------------------------------------------------------------------------

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }
}
