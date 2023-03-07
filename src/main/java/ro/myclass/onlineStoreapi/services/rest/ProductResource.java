package ro.myclass.onlineStoreapi.services.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.myclass.onlineStoreapi.dto.CancelOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderResponse;
import ro.myclass.onlineStoreapi.dto.ProductDTO;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.services.CustomerService;
import ro.myclass.onlineStoreapi.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Slf4j
public class ProductResource {

    CustomerService customerService;
    private ProductService productService;

    public ProductResource(ProductService productService,CustomerService customerService) {
        this.productService = productService;
        this.customerService = customerService;
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = this.productService.showProducts();
        log.info("REST request to get all products {}",products);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO){
        log.info("REST request to add product {}",productDTO);
        this.productService.addProduct(productDTO);

        return new ResponseEntity<>("Product was added!",HttpStatus.OK);

    }

    @DeleteMapping("/deleteProduct/{name}")
    public ResponseEntity<String> deleteProduct(@PathVariable String name){
        log.info("REST request to remove product by name",name);
        this.productService.deleteProduct(name);

        return new ResponseEntity<>("Product was deleted!",HttpStatus.OK);
    }

    @GetMapping("/getProductByName")
    public ResponseEntity<Product> getProductByName(@RequestParam String name){

        log.info("REST request to get product by name {}",name);

        Product product = this.productService.getProductbyName(name);

        return new ResponseEntity<>(product,HttpStatus.OK);

    }

    @DeleteMapping(path = "/cancelOrder")
    public ResponseEntity<CreateOrderResponse> deleteOrder(@RequestBody CancelOrderRequest orderRequest){
        this.customerService.cancelOrder(orderRequest);

        return new ResponseEntity<>(new CreateOrderResponse("sters cu succes!"),HttpStatus.OK);


    }


}
