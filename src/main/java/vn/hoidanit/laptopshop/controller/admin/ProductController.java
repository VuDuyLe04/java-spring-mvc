package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class ProductController {

    private ProductService productService;
    private UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    // table product
    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        List<Product> products = this.productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/product/show";
    }

    // Create product page
    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }
    // Detail product page

    @RequestMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        System.out.println("Check path id =" + id);
        model.addAttribute("id", id);
        model.addAttribute("product", product);
        return "admin/product/detail";
    }

    // Save new product
    @PostMapping("/admin/product/create")
    public String requestMethodName(Model model, @ModelAttribute("newProduct") @Valid Product product,
            BindingResult newProductbindingResult,
            @RequestParam("productFile") MultipartFile file) {
        // Validate
        List<FieldError> errors = newProductbindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }

        if (newProductbindingResult.hasErrors()) {
            return "/admin/product/create";
        }

        // realtive path: absolute path
        product.setImage(this.uploadService.handleSaveUploadFile(file, "product"));
        this.productService.handleSaveProduct(product);
        return "redirect:/admin/product";
    }
}
