package org.vergolyas.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.vergolyas.library.models.Book;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM book", new BookMapper());
    }

    public Optional<Book> show(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id=?", new BookMapper(), id)
                .stream().findAny();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(author, year, name) VALUES(?, ?, ?)",
                book.getAuthor(), book.getYear(), book.getName());
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE Book SET author=?, year=?, name=? WHERE id=?",
                book.getAuthor(), book.getYear(), book.getName(), id);
    }

    public void delete(int id)  {
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    public void setOwnerId(int bookId, int ownerId) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE id=?", ownerId, bookId);
    }

    public void removeOwner(int bookId) {
        jdbcTemplate.update("UPDATE book SET person_id=null WHERE id=?", bookId);
    }

    public int getOwnerId(int id) {
        Optional<Book> book = show(id);
        if (book.isPresent()) {
            return book.get().getOwnerId();
        } else {
            return 0;
        }
    }
}
