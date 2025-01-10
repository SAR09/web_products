package programmermuda.spring.apiproduct.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import programmermuda.spring.apiproduct.entity.Category;
import programmermuda.spring.apiproduct.repository.CategoryRepository;

import java.util.List;

@Component
public class CategoryUtils implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CategoryUtils.class);

    private final CategoryRepository categoryRepository;

    public CategoryUtils(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!categoryRepository.existsByName("Laptop")){
            Category laptop = new Category();
            laptop.setName("Laptop");

            categoryRepository.save(laptop);
        }else {
            log.info("Katehori Laptop sudah ada di database");
        }

        if (!categoryRepository.existsByName("Komputer")){
            Category komputer = new Category();
            komputer.setName("Komputer");

            categoryRepository.save(komputer);
        }else {
            log.info("Kategori Komputer sudah ada di database");
        }

        if (!categoryRepository.existsByName("Handhpone")){
            Category handphone = new Category();
            handphone.setName("Handhpone");

            categoryRepository.save(handphone);
        }else {
            log.info("Kategori Handphone sudah ada di database");
        }


    }
}
