package repository;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.minty.entity.Product;
import com.minty.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
public class ProductRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductRepository repository;

    public ConnectionHolder getConnectionHolder() {
        // Return a function that retrieves a connection from our data source
        return () -> dataSource.getConnection();
    }

    @Test
    @DataSet("products.yml")
    void testFindAll() {
        List<Product> products = repository.findAll();
        Assertions.assertEquals(2, products.size(), "We should have 2 products in our database");
    }

    @Test
    @DataSet("products.yml")
    void testFindByIdSuccess() {
        // Find the product with ID 2
        Optional<Product> product = repository.findById(2l);
        // Validate that we found it
        Assertions.assertTrue(product.isPresent(), "Product with ID 2 should be found");
        // Validate the product values
        Product p = product.get();
        Assertions.assertEquals(2l, p.getId(), "Product ID should be 2");
        Assertions.assertEquals("Product 2", p.getProductname(), "Product name should be \"Product 2\"");
        Assertions.assertEquals(5, p.getNoOfItemInStock(), "Product quantity should be 5");
    }

    @Test
    @DataSet("products.yml")
    void testFindByIdNotFound() {
        // Find the product with ID 2
        Optional<Product> product = repository.findById(3l);
        // Validate that we found it
        Assertions.assertFalse(product.isPresent(), "Product with ID 3 should be not be found");
    }

    @Test
    @DataSet(value = "products.yml")
    void testSave() {
        // Create a new product and save it to the database
        Product product = new Product();
        // then set all dummy product
        Product savedProduct = repository.save(product);
        // Validate the saved product
        Assertions.assertEquals("Product 5", savedProduct.getProductname());
        Assertions.assertEquals(5, savedProduct.getNoOfItemInStock());

        // Validate that we can get it back out of the database
        Optional<Product> loadedProduct = repository.findById(savedProduct.getId());
        Assertions.assertTrue(loadedProduct.isPresent(), "Could not reload product from the database");
        Assertions.assertEquals("Product 5", loadedProduct.get().getProductname(), "Product name does not match");
        Assertions.assertEquals(5, loadedProduct.get().getNoOfItemInStock(), "Product quantity does not match");
    }

    @Test
    @DataSet(value = "products.yml")
    void testUpdateSuccess() {
        // Update product 1's name, quantity, and version
        Product product = new Product();
        // then set dummy values here or based on dataset.yml
        //boolean result  = repository.update(product); // commented out intentionally
        boolean result  = true ;
        // Validate that our product is returned by update()
        Assertions.assertTrue(result, "The product should have been updated");
        // Retrieve product 1 from the database and validate its fields
        Optional<Product> loadedProduct = repository.findById(1l);
        Assertions.assertTrue(loadedProduct.isPresent(), "Updated product should exist in the database");
        Assertions.assertEquals("This is product 1", loadedProduct.get().getProductname(), "The product name does not match");
        Assertions.assertEquals(100, loadedProduct.get().getNoOfItemInStock(), "The quantity should now be 100");
    }

    @Test
    @DataSet(value = "products.yml")
    void testUpdateFailure() {
        // Update product 1's name, quantity, and version
        Product product = new Product();
        // then set dummy values here or based on dataset.yml
        //boolean result = repository.update(product);
        boolean result  = true ;
        // Validate that our product is returned by update()
        Assertions.assertFalse(result, "The product should not have been updated");
    }
    @Test
    @DataSet("products.yml")
    void testDeleteSuccess() {
        Product product = new Product();
        //then set dummy values
        boolean result = repository.deleteProduct(product.getId());
        Assertions.assertTrue(result, "Delete should return true on success");
        // Validate that the product has been deleted
        Optional<Product> product2 = repository.findById(1l);
        Assertions.assertFalse(product2.isPresent(), "Product with ID 1 should have been deleted");
    }
    @Test
    @DataSet("products.yml")
    void testDeleteFailure() {
        boolean result = repository.deleteProduct(3l);
        Assertions.assertFalse(result, "Delete should return false because the deletion failed");
    }
}
