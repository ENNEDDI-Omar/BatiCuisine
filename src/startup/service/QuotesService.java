package startup.service;

import startup.domain.entities.Quotes;
import startup.exceptions.QuotesNotFoundException;
import startup.repository.implementations.QuotesRepository;

import java.util.Optional;
import java.util.List;

public class QuotesService {
    private final QuotesRepository quotesRepository;

    public QuotesService() {
        this.quotesRepository = new QuotesRepository();
    }

    public Quotes saveQuote(Quotes quote) {
        return quotesRepository.save(quote);
    }

    public Optional<Quotes> findQuoteById(Long id) {
        Optional<Quotes> quote = quotesRepository.findById(id);
        if (!quote.isPresent()) {
            throw new QuotesNotFoundException("Quote not found with id: " + id);
        }
        return quote;
    }

    public List<Quotes> findAllQuotes() {
        return quotesRepository.findAll();
    }

    public Quotes updateQuote(Quotes quote) {
        if (quote == null || quote.getId() == null) {
            throw new IllegalArgumentException("Quote and Quote ID must not be null.");
        }
        return quotesRepository.update(quote);
    }

    public boolean deleteQuote(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Quote ID must not be null.");
        }
        return quotesRepository.delete(id);
    }
}

