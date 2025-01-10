package programmermuda.spring.apiproduct.mapper;

import programmermuda.spring.apiproduct.dto.CategoryDto;
import programmermuda.spring.apiproduct.dto.ProductDto;
import programmermuda.spring.apiproduct.entity.Category;
import programmermuda.spring.apiproduct.entity.Product;

public class ProductMapper {

    public static ProductDto toDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                new CategoryDto(product.getCategory().getId(), product.getCategory().getName()),
                product.getImageUrl()
        );
    }

    public static Product toEntity(ProductDto productDto, Category category, String imageUrl){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        return product;
    }

}
