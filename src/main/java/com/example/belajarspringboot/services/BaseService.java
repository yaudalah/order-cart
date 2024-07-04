package com.example.belajarspringboot.services;

import com.example.belajarspringboot.models.DTO.SuccessApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public abstract class BaseService<T, ID> {
    protected final JpaRepository<T, ID> repository;

    public Page<T> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    public SuccessApiResponse<Object> create(T entity) {
        repository.save(entity);
        return SuccessApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(null)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .build();
    }

    public Optional<T> update(ID id, T entityDetails) {
        return repository.findById(id)
                .map(entity -> {
                    updateEntity(entity, entityDetails);
                    return repository.save(entity);
                });
    }

    public Optional<Void> delete(ID id) {
        return repository.findById(id)
                .map(entity -> {
                    repository.delete(entity);
                    return null;
                });
    }

    protected abstract void updateEntity(T existingEntity, T entityDetails);

    public List<T> bulkInsert(List<T> entities) {
        return repository.saveAll(entities);
    }

    public List<T> bulkUpdate(List<ID> ids, List<T> entityDetailsList) {
        if (ids.size() != entityDetailsList.size()) {
            throw new IllegalArgumentException("The size of ids and entityDetailsList must be the same.");
        }
        for (int i = 0; i < ids.size(); i++) {
            ID id = ids.get(i);
            T entityDetails = entityDetailsList.get(i);
            repository.findById(id)
                    .ifPresent(existingEntity -> {
                        updateEntity(existingEntity, entityDetails);
                        repository.save(existingEntity);
                    });
        }
        return repository.findAllById(ids);
    }

    public void bulkDelete(List<ID> ids) {
        List<T> entities = repository.findAllById(ids);
        repository.deleteAll(entities);
    }
}

