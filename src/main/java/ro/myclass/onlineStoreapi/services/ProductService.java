package ro.myclass.onlineStoreapi.services;



import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import ro.myclass.onlineStoreapi.dto.ProductDTO;
import ro.myclass.onlineStoreapi.exceptions.ListEmptyException;
import ro.myclass.onlineStoreapi.exceptions.ProductNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.ProductWasFoundException;
import ro.myclass.onlineStoreapi.images.ImageUtil;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.repo.ProductRepo;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepo productRepo;

    private final String FOLDER_PATH = "C:\\mycode\\backend\\onlineStore-api\\src\\main\\java\\ro\\myclass\\onlineStoreapi\\images";

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> showProducts(){
        List<Product> products = this.productRepo.getAllProducts();

        if(products.isEmpty()){
            throw new ListEmptyException();
        }else{
            return products;
        }
    }

    @Transactional
    public void addProduct(ProductDTO productDTO){
        Optional<Product> product = this.productRepo.getProductByName(productDTO.getName());

        if(product.isEmpty()){
            Product m = Product.builder().name(productDTO.getName())
                    .price(productDTO.getPrice())
                    .image(productDTO.getImage())
                    .stock(productDTO.getStock())
                    .build();

            this.productRepo.save(m);

        }else{
            throw new ProductWasFoundException();
        }

    }

    @Transactional
    @Modifying
    public void deleteProduct(String name){
        Optional<Product> product = this.productRepo.getProductByName(name);

        if(product.isEmpty()){
            throw new ProductNotFoundException();
        }else{
            this.productRepo.delete(product.get());

        }
    }

    public Product getProductbyName(String name){

        Optional<Product> product = this.productRepo.getProductByName(name);
        if(product.isEmpty()){
            throw new ProductNotFoundException();
        }
        return product.get();

    }

    public void uploadImage(String productName,MultipartFile file) throws IOException {

        Optional<Product> product = this.productRepo.getProductByName(productName);

        if (product.isEmpty()) {
            throw new ProductNotFoundException();
        }

        Product product1 = product.get();

        product1.setImage(file.getBytes());

        productRepo.saveAndFlush(product1);
    }

}
