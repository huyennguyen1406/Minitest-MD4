package controller;

import model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.IProductService;
import service.impl.ProductServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final IProductService productService = new ProductServiceImpl();

    @Value("F:\\CODE\\CODEMD4\\Minitest-MD4\\Minitest1-CRUDProduct\\src\\main\\webapp\\image")
    private String fileUpload;

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/create");
        modelAndView.addObject("productForm", new Product());
        return modelAndView;
    }

    @PostMapping("/save")
    public ModelAndView saveProduct(@ModelAttribute Product product) {
        MultipartFile multipartFile = product.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(product.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Product product1 = new Product(product.getId(), product.getName(), product.getPrice(), product.getDescription(), fileName);
        productService.saveProduct(product);
        ModelAndView modelAndView = new ModelAndView("/create");
        modelAndView.addObject("product", product);
        modelAndView.addObject("message", "Created new product successfully !");
        return modelAndView;
    }

    @GetMapping
    public ModelAndView showProducts() {
        ModelAndView modelAndView = new ModelAndView("list");
        ArrayList<Product> products = productService.getAllProduct();
        if (products.isEmpty()) {
            modelAndView.addObject("message", "No products!");
            modelAndView.addObject("color", "red");
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("list");
        Product product = productService.deleteProduct(id);
        if (product == null) {
            modelAndView.addObject("message", "Id invalid!");
            modelAndView.addObject("color", "red");
        }
        ArrayList<Product> products = productService.getAllProduct();
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView showDetail(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("detail");
        Product product = productService.getProduct(id);
        if (product != null) {
            modelAndView.addObject("product", product);
        } else {
            modelAndView.addObject("message", "Id invalid!");
        }
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView createProduct(Model model) {
        ModelAndView modelAndView = new ModelAndView("create");
        model.addAttribute("product", new Product());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView create(@ModelAttribute Product product) {
        ModelAndView modelAndView = new ModelAndView("create");
        Product productCreate = productService.saveProduct(product);
        if (productCreate != null) {
            modelAndView.addObject("message", "Create successfully!");
        }
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editProduct(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("edit");
        Product product = productService.getProduct(id);
        if (product != null) {
            modelAndView.addObject("product", product);
        } else {
            modelAndView.addObject("message", "Id invalid!");
        }
        return modelAndView;
    }

    @PostMapping("/{id}")
    public ModelAndView edit(@ModelAttribute Product product, @PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("edit");
        product.setId(id);
        Product productEdit = productService.saveProduct(product);
        if (productEdit != null) {
            modelAndView.addObject("message", "Update successfully!");
        }
        return modelAndView;
    }
}
