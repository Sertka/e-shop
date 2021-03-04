package ru.stk.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stk.eshop.entities.ProductType;
import ru.stk.eshop.repo.TypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TypeService {

    private TypeRepository repo;

    @Autowired
    public void setTypeRepository(TypeRepository repo) {
        this.repo = repo;
    }

    public List<ProductType> findAll() {
        return repo.findAll();
    }

    public Optional<ProductType> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public void save(ProductType type) {
        repo.save(type);
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}