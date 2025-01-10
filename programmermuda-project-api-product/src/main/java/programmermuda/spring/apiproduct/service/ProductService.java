package programmermuda.spring.apiproduct.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import programmermuda.spring.apiproduct.dto.ProductDto;
import programmermuda.spring.apiproduct.entity.Category;
import programmermuda.spring.apiproduct.entity.Product;
import programmermuda.spring.apiproduct.mapper.ProductMapper;
import programmermuda.spring.apiproduct.repository.CategoryRepository;
import programmermuda.spring.apiproduct.repository.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${upload.path}")
    private String uploadPath;


    //Save
    public ProductDto saveProduct(ProductDto productDto, MultipartFile image) throws IOException {
        Category category = categoryRepository.findById(productDto.getCategory().getId())
                        .orElseThrow(()-> new RuntimeException("Category not found"));

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Product product = ProductMapper.toEntity(productDto, category, filePath.toString());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product saveProduct = productRepository.save(product);

        return ProductMapper.toDto(saveProduct);
    }

    // Get All Products
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toDto) // konversi setiap Product ke ProductDto
                .collect(Collectors.toList());
    }

    // Get Product By Id
    public Optional<ProductDto> getProductById(Long id){
        return productRepository.findById(id)
                .map(ProductMapper::toDto);
    }

    // Update Product
    public ProductDto updateProduct(Long id, ProductDto updateProductDto, MultipartFile image) throws IOException {
        // cari product berdasarkan ID
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found"));

        // cari category berdasarkan Id di DTO
        Category category = categoryRepository.findById(updateProductDto.getCategory().getId())
                .orElseThrow(()-> new RuntimeException("Category not found"));

        // update fields
        product.setName(updateProductDto.getName());
        product.setDescription(updateProductDto.getDescription());
        product.setPrice(updateProductDto.getPrice());
        product.setCategory(category);

        // check image
        if (image != null && !image.isEmpty()){
            String imageUrl = saveImage(image);
            product.setImageUrl(imageUrl);
        }

        Product updatedProduct = productRepository.save(product);

        return ProductMapper.toDto(updatedProduct);

    }

    private String saveImage(MultipartFile image) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadPath, fileName);

        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/images" + fileName;
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
}
