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

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderDetailRepo orderDetailRepo;

    @Autowired
    CustomerRepo customerRepo;

    @BeforeEach
    public void clean(){
        productRepo.deleteAll();
    }

    @Test
    public void getProductByName(){
        ProductDTO productDTO = new ProductDTO("casti gaming HyperX",400,"https://wwww.picsart.ro/casti-gaming-hyperx",500);

        Product product = Product.builder().name(productDTO.getName())
                .image(productDTO.getImage())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .build();

        productRepo.save(product);

        assertEquals(product,this.productRepo.getProductByName("casti gaming HyperX").get());
    }

    @Test
    public void getAllProducts(){
        ProductDTO productDTO = new ProductDTO("casti gaming HyperX",400,"https://wwww.picsart.ro/casti-gaming-hyperx",500);
        ProductDTO productDTO1 = new ProductDTO("tastatura gaming steelseries",900,"https://wwww.picsart.ro/tastatura-gaming-steelseries",900);
        ProductDTO productDTO2 = new ProductDTO("mouse gaming Razer",150,"https://wwww.picsart.ro/mouse-gaming-razer",40);
        ProductDTO productDTO3 = new ProductDTO("televizor samsung 40 inch ",1500,"https://wwww.picsart.ro/televizor-samsung-s2h31",472);

        Product product = Product.builder().name(productDTO.getName()).stock(productDTO.getStock()).image(productDTO.getImage()).build();
        Product product1 = Product.builder().name(productDTO1.getName()).stock(productDTO1.getStock()).price(productDTO1.getPrice()).image(productDTO1.getImage()).build();
        Product product2 = Product.builder().name(productDTO2.getName()).stock(productDTO2.getStock()).price(productDTO2.getPrice()).image(productDTO2.getImage()).build();
        Product product3 = Product.builder().name(productDTO3.getName()).stock(productDTO3.getStock()).price(productDTO3.getPrice()).image(productDTO3.getImage()).build();
        productRepo.save(product);
        productRepo.save(product1);
        productRepo.save(product2);
        productRepo.save(product3);
        List<Product> list = new ArrayList<>();

        list.add(product);
        list.add(product1);
        list.add(product2);
        list.add(product3);

        assertEquals(list,this.productRepo.getAllProducts());

    }

    @Test
    public void getProductById(){
        ProductDTO productDTO = new ProductDTO("casti gaming HyperX",400,"https://wwww.picsart.ro/casti-gaming-hyperx",500);
        Product product = Product.builder().name(productDTO.getName()).
                stock(productDTO.getStock())
                .image(productDTO.getImage())
                .build();

        productRepo.save(product);

        assertEquals(product,productRepo.getProductById(1).get());


    }


    @Test
    public void getAllProductsFromOrder(){

        ProductDTO productDTO = new ProductDTO("casti gaming HyperX",400,"https://wwww.picsart.ro/casti-gaming-hyperx",500);
        Product product = Product.builder().name(productDTO.getName()).
                stock(productDTO.getStock())
                .image(productDTO.getImage())
                .build();

        productRepo.save(product);

        LocalDate localDate = LocalDate.now();

        Customer customer = new Customer("eduard.mocanu@gmail.com","eduard.mocanu@gmail.com2022","Eduard Mocanu");

        customerRepo.save(customer);
        Order order = new Order((long) 1 , localDate,customer);

        orderRepo.save(order);

        OrderDetail orderDetail = new OrderDetail((long)1,500,4,order,product);

        orderDetailRepo.save(orderDetail);

       List<Product> products = new ArrayList<>();
       products.add(product);

       assertEquals(products,this.productRepo.getAllProductsFromOrder(1));


    }
}
