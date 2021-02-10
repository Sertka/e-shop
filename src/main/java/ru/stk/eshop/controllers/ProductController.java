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
import ru.stk.eshop.entities.Greeting;
import ru.stk.eshop.entities.Product;
import ru.stk.eshop.exceptions.NotFoundException;
import ru.stk.eshop.services.BrandService;
import ru.stk.eshop.services.ProductService;
import ru.stk.eshop.services.TypeService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    private ProductService service;
    private BrandService brandService;
    private TypeService typeService;
    private WsController controllerWs;

    @Autowired
    public void setService(ProductService service) {
        this.service = service;
    }
    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }
    @Autowired
    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }
    @Autowired
    public void setControllerWs(WsController controllerWs) {
        this.controllerWs = controllerWs;
    }

    @GetMapping
    public String indexProductPage(Model model,
                                   @RequestParam(name = "nameFilter") Optional<String> nameFilter,
                                   @RequestParam(name = "minFilter") Optional<BigDecimal> minFilter,
                                   @RequestParam(name = "maxFilter") Optional<BigDecimal> maxFilter,
                                   @RequestParam(name = "page") Optional<Integer> page,
                                   @RequestParam(name = "size") Optional<Integer> size,
                                   @RequestParam(name = "brand") Optional<Integer> brand,
                                   @RequestParam(name = "sortField") Optional<String> sortField,
                                   @RequestParam(name = "changeSortOrder") Optional<Boolean> changeSortOrder) {

        logger.info("Product page update");


        model.addAttribute("products", service.findWithFilter(nameFilter,
                minFilter,
                maxFilter,
                page,
                size,
                brand,
                sortField,
                changeSortOrder));
        return "product";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable(value = "id") Long id, Model model) {
        logger.info("Edit product with id {}", id);
        model.addAttribute("product", service.findById(id).orElseThrow(() -> new NotFoundException()));
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("types", typeService.findAll());
        return "product_edit";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute(new Product());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("types", typeService.findAll());
        return "product_edit";
    }

    @PostMapping("/edit")
    public String updateProduct(@Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product_form";
        }
        service.save(product);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            controllerWs.sendMessage("/topic/greetings", new Greeting(product.getName()));
        }).start();

        return "redirect:/product";

    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable(value = "id") Long id) {
        logger.info("Delete product with id {}", id);
        service.deleteById(id);
        return "redirect:/product";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException e){
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
