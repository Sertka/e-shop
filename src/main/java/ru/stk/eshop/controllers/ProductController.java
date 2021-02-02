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
import ru.stk.eshop.services.ProductService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String indexProductPage(Model model,
                                   @RequestParam(name = "nameFilter") Optional<String> nameFilter,
                                   @RequestParam(name = "minFilter") Optional<BigDecimal> minFilter,
                                   @RequestParam(name = "maxFilter") Optional<BigDecimal> maxFilter,
                                   @RequestParam(name = "page") Optional<Integer> page,
                                   @RequestParam(name = "size") Optional<Integer> size,
                                   @RequestParam(name = "sortField") Optional<String> sortField,
                                   @RequestParam(name = "changeSortOrder") Optional<Boolean> changeSortOrder) {

        logger.info("Product page update");


        model.addAttribute("products", service.findWithFilter(nameFilter,
                minFilter,
                maxFilter,
                page,
                size,
                sortField,
                changeSortOrder));
        return "product";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable(value = "id") Long id, Model model) {
        logger.info("Edit product with id {}", id);
        model.addAttribute("product", service.findById(id).orElseThrow(() -> new NotFoundException()));
        return "product_form";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute(new Product());
        return "product_form";
    }

    @PostMapping("/update")
    public String updateProduct(@Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product_form";
        }
        service.save(product);
        return "redirect:/product/list";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable(value = "id") Long id) {
        logger.info("Delete product with id {}", id);
        service.deleteById(id);
        return "redirect:/product/list";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException e){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
