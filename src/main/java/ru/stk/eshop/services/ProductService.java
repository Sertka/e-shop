package ru.stk.eshop.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stk.eshop.entities.Product;
import ru.stk.eshop.exceptions.NotFoundException;
import ru.stk.eshop.repo.ProductRepository;
import ru.stk.eshop.repo.ProductSpec;
import ru.stk.eshop.utils.PriceFormatter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Product entity operations
 */
@Service
public class ProductService{
  private ProductRepository repo;
  private static final String STOCK_EMPTY = "отсутвует";
  private static final String STOCK_SMALL = "мало";
  private static final String STOCK_MEDIUM = "в наличии";
  private static final String STOCK_BIG = "много";
  private static final Logger logger = LoggerFactory.getLogger(ru.stk.eshop.controllers.ProductController.class);

  Sort.Direction sd = Sort.Direction.ASC;

  @Autowired
  public void setProductRepository (ProductRepository repo){
    this.repo = repo;
  }

  /**
   * find all products with provided criteria
   * @param nameFilter - product name
   * @param minFilter - min price
   * @param maxFilter - max price
   * @param page - page number
   * @param size - lines on the page
   * @param brand - brand name
   * @param sortField - sorting by field
   * @param changeSortOrder - change sort order flag (asc/dsc)
   *
   * @return page object with related products
   */
  public Page<Product> findWithFilter(Optional<String> nameFilter,
                                      Optional<BigDecimal> minFilter,
                                      Optional<BigDecimal> maxFilter,
                                      Optional<Integer> page,
                                      Optional<Integer> size,
                                      Optional<Integer> brand,
                                      Optional<String> sortField,
                                      Optional<Boolean> changeSortOrder) {

    Specification<Product> spec = Specification.where(null);
    Page<Product> currentPage;

    // revert sort order
    if (changeSortOrder.isPresent() && changeSortOrder.get()) {
      sd = (sd == Sort.Direction.ASC) ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    // fill in Specification
    if (nameFilter.isPresent()) {
      spec = spec.and(ProductSpec.nameLike(nameFilter.get()));
    }
    if (minFilter.isPresent()) {
      spec = spec.and(ProductSpec.priceBigger(minFilter.get()));
    }
    if (maxFilter.isPresent()) {
      spec = spec.and(ProductSpec.priceLess(maxFilter.get()));
    }
    if (brand.isPresent()) {
      spec = spec.and(ProductSpec.brandEqual(brand.get()));
    }

    if (sortField.isPresent() && !sortField.get().equals("")) {
      currentPage = repo.findAll(spec, PageRequest.of(page.orElse(1) - 1,
              size.orElse(10),
              Sort.by(sd, sortField.get())));
    }else{
      currentPage = repo.findAll(spec, PageRequest.of(page.orElse(1) - 1,
              size.orElse(10),
              Sort.by(Sort.Direction.ASC, "id")));
    }

    //update display fields
    for (Product p: currentPage){
        if (p.getStock() == 0) {
          p.setPrintStock(STOCK_EMPTY);
        }else if (p.getStock() < 10){
          p.setPrintStock(STOCK_SMALL);
        }else if (p.getStock() < 30) {
          p.setPrintStock(STOCK_MEDIUM);
        }else{
          p.setPrintStock(STOCK_BIG);
        }

        p.setPrintPrice(PriceFormatter.format(p.getPrice()));
    }

    return currentPage;
  }

  /**
   * find all products in accordance with spec conditions
   * @param spec - specification
   * @return product list
   */
  public List<Product> findAll(Specification<Product> spec) {
    return repo.findAll(spec);
  }

  /**
   * find product by product id
   * @param id - product id
   * @return product or NotFound exception
   */
  public Product findById(Long id) {
    Optional<Product> p = repo.findById(id);
    if (p.isPresent()){
      p.get().setPrintPrice(PriceFormatter.format(p.get().getPrice()));
      return p.get();
    }else{
      logger.warn("Product with id {} not found!", id);
      throw new NotFoundException();
    }
  }

  /**
   * save product in DB
   * @param product - product
   */
  @Transactional
  public void save(Product product) {
    repo.save(product);
  }

  /**
   * delete product from DB
   * @param id - product id
   */
  @Transactional
  public void deleteById(Long id) {
    repo.deleteById(id);
  }
}