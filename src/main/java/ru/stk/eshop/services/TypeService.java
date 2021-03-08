package ru.stk.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stk.eshop.entities.ProductType;
import ru.stk.eshop.repo.TypeRepository;

import java.util.List;
import java.util.Optional;

/**
 * Product type entity operations
 */
@Service
public class TypeService {

    private TypeRepository repo;

    @Autowired
    public void setTypeRepository(TypeRepository repo) {
        this.repo = repo;
    }

    /**
     * find all existed product types
     * @return product type list
     */
    public List<ProductType> findAll() {
        return repo.findAll();
    }

    /**
     * find type by id
     * @param id - product type id
     * @return product type (optional)
     */
    public Optional<ProductType> findById(Long id) {
        return repo.findById(id);
    }

    /**
     * save product type in db
     * @param type - product type
     */
    @Transactional
    public void save(ProductType type) {
        repo.save(type);
    }

    /**
     * delete product type from db
     * @param id - product type id
     */
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}