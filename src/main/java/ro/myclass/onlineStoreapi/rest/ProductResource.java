package ro.myclass.onlineStoreapi.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.myclass.onlineStoreapi.dto.ProductDTO;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductResource {

    private ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = this.productService.showProducts();

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO){
        this.productService.addProduct(productDTO);

        return new ResponseEntity<>("Product was added!",HttpStatus.OK);

    }

    @DeleteMapping("/deleteProduct/{name}")
    public ResponseEntity<String> deleteProduct(@PathVariable String name){
        this.productService.deleteProduct(name);

        return new ResponseEntity<>("Product was deleted!",HttpStatus.OK);
    }

    @GetMapping("/getProductByName")
    public ResponseEntity<Product> getProductByName(@RequestParam String name){

        Product product = this.productService.getProductbyName(name);

        return new ResponseEntity<>(product,HttpStatus.OK);

    }
}
