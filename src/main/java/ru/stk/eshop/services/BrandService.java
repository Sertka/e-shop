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
     * all brands
     * @return list of all brands
     */
    public List<Brand> findAll() {
        return repo.findAll();
    }

    /**
     * brand by id
     * @param id - brand id
     * @return Brand object
     */
    public Optional<Brand> findById(Long id) {
        return repo.findById(id);
    }

    /**
     * brand saving in DB
     * @param brand - Brand object
     */
    @Transactional
    public void save(Brand brand) {
        repo.save(brand);
    }

    /**
     * brand delete
     * @param id - brand id
     */
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}