package com.example.belajarspringboot.services;

import com.example.belajarspringboot.models.dto.SuccessApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseService<T, ID> {
    protected final JpaRepository<T, ID> repository;

    public SuccessApiResponse<Object> getAll(Pageable pageable) {
        return SuccessApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("Success")
                .data(repository.findAll(pageable))
                .build();
    }

    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    @Transactional
    public SuccessApiResponse<Object> create(T entity) {
        if (repository != null) {
            repository.save(entity);
        }
        log.info("Successfully created entity: {}", entity);
        return SuccessApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(null)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .build();
    }

    @Transactional
    public T update(ID id, T entityDetails) {
        return repository.findById(id)
                .map(entity -> {
                    updateEntity(entity, entityDetails);
                    T savedEntity = repository.save(entity);
                    log.info("Successfully updated entity with id {}: {}", id, savedEntity);
                    return savedEntity;
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Entity with id %s not found", id)
                ));
    }

    @Transactional
    public void delete(ID id) {
        repository.findById(id)
                .ifPresentOrElse(
                        entity -> {
                            repository.delete(entity);
                            log.info("Successfully deleted entity with id: {}", id);
                        },
                        () -> {
                            throw new EntityNotFoundException(
                                    String.format("Entity with id %s not found", id)
                            );
                        }
                );
    }

    protected abstract void updateEntity(T existingEntity, T entityDetails);

    public List<T> bulkInsert(List<T> entities) {
        return repository.saveAll(entities);
    }

    @Transactional
    public List<T> bulkUpdate(List<ID> ids, List<T> entityDetailsList) {
        if (ids.size() != entityDetailsList.size()) {
            throw new IllegalArgumentException("The size of ids and entityDetailsList must be the same.");
        }
        IntStream.range(0, ids.size()).forEach(i -> {
            ID id = ids.get(i);
            T entityDetails = entityDetailsList.get(i);
            repository.findById(id)
                    .ifPresent(existingEntity -> {
                        updateEntity(existingEntity, entityDetails);
                        repository.save(existingEntity);
                    });
        });
        return repository.findAllById(ids);
    }

    @Transactional
    public void bulkDelete(List<ID> ids) {
        List<T> entities = repository.findAllById(ids);
        repository.deleteAll(entities);
    }
}

