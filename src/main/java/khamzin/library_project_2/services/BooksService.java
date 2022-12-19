package khamzin.library_project_2.services;

import khamzin.library_project_2.models.Book;
import khamzin.library_project_2.models.Person;
import khamzin.library_project_2.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean isSorted) {
        if (isSorted)
            return booksRepository.findAll(Sort.by("yearOfWriting"));
        else
            return booksRepository.findAll();
    }

    public Book findById(int id) {
        return booksRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook) {

        updateBook.setId(id);
        updateBook.setOwner(booksRepository.findById(id).get().getOwner());
        booksRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public Person getBookOwner(int id) {
        return booksRepository.findById(id)
                .map(Book::getOwner)
                .orElse(null);
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        Book book = findById(id);

        book.setAssignedAt(new Date());
        book.setOwner(selectedPerson);
    }

    @Transactional
    public void release(int id) {
        Book book = findById(id);

        book.setAssignedAt(null);
        book.setOwner(null);
    }

    @Transactional
    public List<Book> findWithPagination(int page, int itemsPerPage, boolean isSortedByYear) {
        if (isSortedByYear)
            return booksRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("yearOfWriting"))).getContent();
    }

    @Transactional
    public List<Book> findByTitle(String query) {
        return booksRepository.findByTitleStartingWith(query);
    }
}
