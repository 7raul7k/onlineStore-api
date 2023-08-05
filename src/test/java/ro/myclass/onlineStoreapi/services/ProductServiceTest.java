package ro.myclass.onlineStoreapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.myclass.onlineStoreapi.dto.ProductDTO;
import ro.myclass.onlineStoreapi.exceptions.ListEmptyException;
import ro.myclass.onlineStoreapi.exceptions.ProductNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.ProductWasFoundException;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.repo.ProductRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepo productRepo;

    @InjectMocks
    ProductService productService;

    @Captor
    ArgumentCaptor<Product> argumentCaptor;

    @Test
    public void showProductsOk(){
        Product product = Product.builder().id(1L).name("iphone 14").price(5000).stock(500).build();
        Product product1 = Product.builder().id(2L).name("Samsung galaxy s23").price(4500).stock(430).build();
        Product product2 = Product.builder().id(3L).name("HDD Samsung 2TB").stock(700).price(400).build();

        productRepo.save(product);
        productRepo.save(product1);
        productRepo.save(product2);

        List<Product> list = new ArrayList<>();
        list.add(product);
        list.add(product1);
        list.add(product2);

        doReturn(list).when(productRepo).getAllProducts();

        assertEquals(list,this.productService.showProducts());
    }

    @Test
    public void showProductsException(){
        doReturn(new ArrayList<>()).when(productRepo).getAllProducts();

        assertThrows(ListEmptyException.class,()->{
            this.productService.showProducts();
        });
    }


    @Test
    public void addProductOk(){

        ProductDTO productDTO = ProductDTO.builder().name("Microphone trust gxt").stock(198).price(650).build();


        Product m = Product.builder().id(1L).name(productDTO.getName())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .build();
        this.productService.addProduct(productDTO);

        verify(productRepo,times(1)).save(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue(),m);


    }

    @Test
    public void addProductException(){
        doReturn(Optional.of(new Product())).when(productRepo).getProductByName("gaming");

        assertThrows(ProductWasFoundException.class,()->{
           this.productService.addProduct(ProductDTO.builder().name("gaming").build());
        });
    }



    @Test
    public void deleteProductOk(){
        Product product = Product.builder().id(1L).price(640).name("Razer Microphone for streaming").image(new byte[23]).stock(400).build();
        productRepo.save(product);


        doReturn(Optional.of(product)).when(productRepo).getProductByName("Razer Microphone for streaming");

        this.productService.deleteProduct(product.getName());
        verify(productRepo,times(1)).delete(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue(),product);

    }

    @Test
    public void deleteProductException(){
        doReturn(Optional.empty()).when(productRepo).getProductByName("iphone");

        assertThrows(ProductNotFoundException.class,()->{
            this.productService.deleteProduct("iphone");
        });
    }

    @Test
    public void getProductbyNameOk(){
        Product product= Product.builder().id(1L).name("Seagate Portable 2TB External Hard Drive HDD ").price(300).image(new byte[23]).stock(600).build();

        productRepo.save(product);

        doReturn(Optional.of(product)).when(productRepo).getProductByName("Seagate Portable 2TB External Hard Drive HDD ");

        assertEquals(product,this.productService.getProductbyName("Seagate Portable 2TB External Hard Drive HDD "));

    }

    @Test
    public void getProductByNameException(){
        doReturn(Optional.empty()).when(productRepo).getProductByName("");

        assertThrows(ProductNotFoundException.class,()->{
            this.productService.getProductbyName("");
        });
    }




}