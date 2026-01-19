package com.sboot.service;

import com.sboot.dto.SupplierRatingRequest;
import com.sboot.dto.SupplierRatingResponse;
import com.sboot.entity.Supplier;
import com.sboot.entity.SupplierRating;
import com.sboot.repository.SupplierRatingRepository;
import com.sboot.repository.SuppliersRepository;
import com.sboot.util.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierRatingServiceImpl implements SupplierRatingService {

    private final SupplierRatingRepository repository;
    private final SuppliersRepository supplierRepository;

    public SupplierRatingServiceImpl(SupplierRatingRepository repository, SuppliersRepository supplierRepository) {
        this.repository = repository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public SupplierRatingResponse create(SupplierRatingRequest request) {

        // ✅ Check if supplier exists before creating a rating
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier not found with ID: " + request.getSupplierId()));

        // ✅ Validate that answers are provided
        if (request.getAnswers() == null || request.getAnswers().isEmpty()) {
            throw new IllegalArgumentException("Answers cannot be empty");
        }

        // ✅ Calculate average rating
        double avg = request.getAnswers().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        int finalRating = (int) Math.round(avg);

        SupplierRating entity = new SupplierRating();
        entity.setSupplier(supplier); // ✅ FIXED
        entity.setRating(finalRating);
        entity.setReview(request.getReview());
        entity.setCreatedAt(LocalDate.now());
        entity.setUpdatedAt(LocalDate.now());

        SupplierRating saved = repository.save(entity);
        return SupplierRatingMapper.toDto(saved);
    }

    @Override
    public SupplierRatingResponse getById(Long id) {
        SupplierRating rating = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierRating not found with id: " + id));
        return SupplierRatingMapper.toDto(rating);
    }

    @Override
    public Page<SupplierRatingResponse> getBySupplierId(Long supplierId, Pageable pageable) {
        int start = pageable.getPageNumber() * pageable.getPageSize() + 1;
        int end = start + pageable.getPageSize() - 1;

        List<SupplierRating> ratings = repository.findBySupplierIdPaged(supplierId, start, end);
        long total = repository.countBySupplierId(supplierId);

        List<SupplierRatingResponse> responses = ratings.stream()
                .map(SupplierRatingMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, total);
    }

    @Override
    public Page<SupplierRatingResponse> getAllRatings(Pageable pageable) {
        int start = pageable.getPageNumber() * pageable.getPageSize() + 1;
        int end = start + pageable.getPageSize() - 1;

        List<SupplierRating> ratings = repository.findAllPaged(start, end);
        long total = repository.countAllRatings();

        List<SupplierRatingResponse> responses = ratings.stream()
                .map(SupplierRatingMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, total);
    }

    @Override
    public SupplierRatingResponse update(Long id, SupplierRatingRequest request) {
        SupplierRating existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierRating not found with id: " + id));

        // ✅ Check if supplier exists before updating
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier not found with ID: " + request.getSupplierId()));

        // ✅ Handle answer-based rating calculation
        if (request.getAnswers() == null || request.getAnswers().isEmpty()) {
            throw new IllegalArgumentException("Answers cannot be empty");
        }

        double avg = request.getAnswers().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        int finalRating = (int) Math.round(avg);

        existing.setSupplier(supplier); // ✅ FIXED
        existing.setRating(finalRating);
        existing.setReview(request.getReview());
        existing.setUpdatedAt(LocalDate.now());

        SupplierRating saved = repository.save(existing);
        return SupplierRatingMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        SupplierRating existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierRating not found with id: " + id));
        repository.delete(existing);
    }

    @Override
    public List<SupplierRatingResponse> getAllRatingsList() {
        List<SupplierRating> ratings = repository.findAll();
        return ratings.stream()
                .map(SupplierRatingMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<SupplierRatingResponse> getLatestSupplierRatings() {
        List<SupplierRating> ratings = repository.findLatestRatingsPerSupplier();
        return ratings.stream()
                .map(SupplierRatingMapper::toDto)
                .collect(Collectors.toList());
    }
}
