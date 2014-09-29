import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestResourceTest extends SecuredResourceTestCase {

    @Autowired
    private MockRestlet mockRestlet;

    @Test
    public void authorizedEntry() {
        mockRestlet.get("/service/test")
                .expectStatusToBe(200);
    }

}
