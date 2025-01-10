package programmermuda.spring.apiproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programmermuda.spring.apiproduct.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
