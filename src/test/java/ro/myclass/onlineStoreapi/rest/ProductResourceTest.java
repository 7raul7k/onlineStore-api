package ro.myclass.onlineStoreapi.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.myclass.onlineStoreapi.dto.CancelOrderRequest;
import ro.myclass.onlineStoreapi.dto.ProductDTO;
import ro.myclass.onlineStoreapi.exceptions.ListEmptyException;
import ro.myclass.onlineStoreapi.exceptions.ProductNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.ProductWasFoundException;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.services.CustomerService;
import ro.myclass.onlineStoreapi.services.ProductService;


import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductResourceTest {

    @Mock
    private ProductService productService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private ProductResource productResource;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc restMockMvc;

    @BeforeEach
    void initialConfig(){ restMockMvc = MockMvcBuilders.standaloneSetup(productResource).build();}

    @Test
    public void getAllProducts() throws Exception{
        Faker faker = new Faker();

        List<Product> productList = new ArrayList<>();

        for(int i = 0 ; i < 10;i++){
          productList.add(Product.builder().id((long) i).name(faker.lorem().sentence()).price(faker.number().numberBetween(100,650)).build());
        }

        doReturn(productList).when(productService).showProducts();

        restMockMvc.perform(get("/api/v1/product/getAllProducts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productList)))
                .andExpect(status().isOk());


    }

    @Test
    public void getAllProductsBadRequest() throws Exception{
        doThrow(ListEmptyException.class).when(productService).showProducts();

        restMockMvc.perform(get("/api/v1/product/getAllProducts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addProduct() throws Exception {
        Faker faker = new Faker();
        ProductDTO productDTO = ProductDTO.builder().name("casti gaming hyperX").stock(250).price(500).build();

        doNothing().when(productService).addProduct(productDTO);

        restMockMvc.perform(post("/api/v1/product/addProduct")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk());
    }


    @Test
    public void addProductBadRequest() throws Exception{
        doThrow(ProductWasFoundException.class).when(productService).addProduct(new ProductDTO());

        restMockMvc.perform(post("/api/v1/product/addProduct")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ProductDTO())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeProduct() throws Exception{
        Faker faker = new Faker();
        Product product = Product.builder().id(1L).stock(500).name("product").price(500).build();

        doNothing().when(productService).deleteProduct("product");

        restMockMvc.perform(delete("/api/v1/product/deleteProduct/?name=product")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());



    }

    @Test
    public void removeProductBadRequest() throws Exception{
        doThrow(ProductNotFoundException.class).when(productService).deleteProduct("test");

        restMockMvc.perform(delete("/api/v1/product/deleteProduct/?name=test")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status()
                        .isBadRequest());
    }

    @Test
    public void getProductByName() throws Exception {
        Faker faker = new Faker();
        Product product = Product.builder().name("telefon").price(500).id(1L).stock(500).build();

        doReturn(product).when(productService).getProductbyName("telefon");

        restMockMvc.perform(get("/api/v1/product/getProductByName/?name=telefon")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(product)))
                        .andExpect(status().isOk());




}

    @Test
    public void getProductByNameBadRequest() throws Exception {
        doThrow(ProductNotFoundException.class).when(productService).getProductbyName("test");

        restMockMvc.perform(get("/api/v1/product/getProductByName/?name=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }



}