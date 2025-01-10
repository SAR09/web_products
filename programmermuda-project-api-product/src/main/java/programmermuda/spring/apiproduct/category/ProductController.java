package programmermuda.spring.apiproduct.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import programmermuda.spring.apiproduct.dto.ProductDto;
import programmermuda.spring.apiproduct.repository.CategoryRepository;
import programmermuda.spring.apiproduct.service.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;



    @PostMapping(value = "/product",consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> saveProduct(@RequestPart("product") ProductDto productDto,
                                              @RequestPart(value = "image") MultipartFile image){
        log.info("Content-Type: " + image.getContentType());
        System.out.println("Content-Type: " + image.getContentType());
        try {
            ProductDto savedProduct = productService.saveProduct(productDto, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        }catch(IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (IOException exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/products")
    public List<ProductDto> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
        Optional<ProductDto> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
                                                    @RequestPart(name = "product") ProductDto productDto,
                                                    @RequestPart(name = "image", required = false) MultipartFile image){
        try {
            ProductDto updatedProduct = productService.updateProduct(id, productDto, image);
            return ResponseEntity.ok(updatedProduct);
        }
        catch (RuntimeException exception){
            log.error("Error occured while update product : {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (IOException exception){
            log.error("Error occured while saving image : {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deteletedProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
