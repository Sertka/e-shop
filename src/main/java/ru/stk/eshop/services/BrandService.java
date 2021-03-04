package ru.stk.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stk.eshop.entities.Brand;
import ru.stk.eshop.entities.Role;
import ru.stk.eshop.repo.BrandRepository;
import ru.stk.eshop.repo.RoleRepository;

import java.util.List;
import java.util.Optional;

/**
 * Brand entity operations
 */
@Service
public class BrandService {

    private BrandRepository repo;

    @Autowired
    public void setBrandRepository(BrandRepository repo) {
        this.repo = repo;
    }

    /**
     * All brands
     * @return list of all brands
     */
    public List<Brand> findAll() {
        return repo.findAll();
    }

    /**
     * Brand by id
     * @param id - brand id
     * @return Brand object
     */
    public Optional<Brand> findById(Long id) {
        return repo.findById(id);
    }

    /**
     * Brand saving in DB
     * @param brand - Brand object
     */
    @Transactional
    public void save(Brand brand) {
        repo.save(brand);
    }

    /**
     * Brand deleting
     * @param id - brand id
     */
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}