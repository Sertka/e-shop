package ru.stk.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stk.eshop.entities.Brand;
import ru.stk.eshop.entities.Type;
import ru.stk.eshop.repo.BrandRepository;
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

    public List<Type> findAll() {
        return repo.findAll();
    }

    public Optional<Type> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public void save(Type type) {
        repo.save(type);
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}