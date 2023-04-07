package ro.myclass.onlineStoreapi.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ro.myclass.onlineStoreapi.OnlineStoreApiApplication;
import ro.myclass.onlineStoreapi.dto.ProductDTO;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OnlineStoreApiApplication.class)
@Transactional
class ProductRepoTest {

    @Autowired
    ProductRepo productRepo;


    @BeforeEach
    public void clean(){
        productRepo.deleteAll();
    }

    @Test
    public void getProductByName(){


        Product product = Product.builder().name("casti gaming razer")
                .price(400)
                .image(new byte[23])
                .stock(500)
                .build();

        productRepo.save(product);

        assertEquals(product,this.productRepo.getProductByName("casti gaming razer").get());
    }

    @Test
    public void getAllProducts(){
        Product product =  Product.builder().name("casti gaming steelseries").price(400).stock(500).build();
        Product product1 = Product.builder().name("tastatura gaming steelseries").price(900).stock(900).build();
        Product product2 = Product.builder().name("mouse gaming Razer").price(150).stock(40).build();
        Product product3 = Product.builder().name("televizor samsung 40 inch ").price(1500).stock(472).build();

        List<Product> list = new ArrayList<>();
        list.add(product);
        list.add(product1);
        list.add(product2);
        list.add(product3);

        productRepo.save(product);
        productRepo.save(product1);
        productRepo.save(product2);
        productRepo.save(product3);
        assertEquals(list,this.productRepo.getAllProducts());

    }

    @Test
    public void getOrderDetailByProductIdAndPrice(){
        Product product = Product.builder().name("casti gaming HyperX")
                .stock(400)
                .image(new byte[23])
                .price(500)
                .build();

        productRepo.save(product);

        assertEquals(product,productRepo.getProductById(1).get());


    }

}
