package ru.stk.eshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.stk.eshop.entities.Product;
import ru.stk.eshop.exceptions.NotFoundException;
import ru.stk.eshop.services.BrandService;
import ru.stk.eshop.services.ProductService;
import ru.stk.eshop.services.TypeService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

/**
 * MVC controller for products
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;
    private BrandService brandService;
    private TypeService typeService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public void setService(ProductService service) {
        this.productService = service;
    }
    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }
    @Autowired
    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    /**
     * product list for administration
     * @param model - model
     * @param nameFilter - product name
     * @param minFilter - min price
     * @param maxFilter - max price
     * @param page - page number
     * @param size - lines on the page
     * @param brand - brand name
     * @param sortField - sorting by field
     * @param changeSortOrder - change sort order flag (asc/dsc)
     * @return product list page
     */
    @GetMapping("/admin")
    public String indexProductPage(Model model,
                                   @RequestParam(name = "nameFilter") Optional<String> nameFilter,
                                   @RequestParam(name = "minFilter") Optional<BigDecimal> minFilter,
                                   @RequestParam(name = "maxFilter") Optional<BigDecimal> maxFilter,
                                   @RequestParam(name = "page") Optional<Integer> page,
                                   @RequestParam(name = "size") Optional<Integer> size,
                                   @RequestParam(name = "brand") Optional<Integer> brand,
                                   @RequestParam(name = "sortField") Optional<String> sortField,
                                   @RequestParam(name = "changeSortOrder") Optional<Boolean> changeSortOrder) {

        model.addAttribute("products", productService.findWithFilter(nameFilter,
                minFilter,
                maxFilter,
                page,
                size,
                brand,
                sortField,
                changeSortOrder));
        logger.info("Product admin page is displayed");
        return "product";
    }

    /**
     * edit specified product
     * @param id - product id
     * @param model - model
     * @return product edit form
     */
    @GetMapping("/{id}")
    public String editProduct(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("types", typeService.findAll());
        logger.info("Edit form for product with id {} is displayed", id);
        return "product_edit";
    }

    /**
     * create new product
     * @param model - model
     * @return edit product form with new product
     */
    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute(new Product());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("types", typeService.findAll());
        logger.info("New product form is displayed");
        return "product_edit";
    }

    /**
     * saves new/edited product
     * @param product - edited product
     * @param bindingResult - validation
//     * @param principal - principal
     * @return product list
     */
    @PostMapping("/update")
    public String updateProduct(@Valid Product product, BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            model.addAttribute("types", typeService.findAll());
            return "product_edit";
        }
        productService.save(product);
        logger.info("Product with id {} is created/updated by user {}", product.getId(), principal.getName());
        return "redirect:/product/admin";
    }

    /**
     * deletes specified product
     * @param id - product id
     * @param principal - principal
     * @return product list
     */
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable(value = "id") Long id, Principal principal) {
        productService.deleteById(id);
        logger.info("Product with id {} is created/updated by user {}", id, principal.getName());
        return "redirect:/product/admin";
    }

    @ExceptionHandler
    private ModelAndView notFoundExceptionHandler(NotFoundException e){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        logger.error("Resource not found exception");
        return modelAndView;
    }
}
