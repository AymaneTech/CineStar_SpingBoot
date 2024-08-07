package dev.codex.cinestar.Movie.Application.Services.Impl;

import dev.codex.cinestar.Movie.Application.DTOs.Requests.AuthorRequest;
import dev.codex.cinestar.Movie.Application.Services.AuthorService;
import dev.codex.cinestar.Movie.Domain.Entities.Author;
import dev.codex.cinestar.Movie.Infrastructure.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;

    public List<Author> createAll(List<AuthorRequest> authors) {
        final List<Author> authorList = authors.stream()
                .map(author -> new Author(author.name()))
                .toList();
        return repository.saveAll(authorList);
    }

    public List<Author> sync(List<AuthorRequest> requestAuthors) {
        final List<Author> existingAuthors = repository.findAll();
        final Map<String, Author> existingAuthorsMap = existingAuthors.stream()
                .collect(Collectors.toMap(Author::getName, author -> author));

        final List<Author> authorsToSave = requestAuthors.stream()
                .map(requestAuthor -> {
                    final Author existingAuthor = existingAuthorsMap.get(requestAuthor.name());
                    if (existingAuthor != null) {
                        existingAuthor.setName(requestAuthor.name());
                        return existingAuthor;
                    } else {
                        return new Author(requestAuthor.name());
                    }
                })
                .collect(Collectors.toList());

        return repository.saveAll(authorsToSave);
    }
}
