package integrationtest;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.test.application.TestHelper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-servlet.xml", "classpath:jpaContext.xml"}) // we would like to run with the real spring context
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class })
@WebAppConfiguration
public class UserRestTest
{
    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    //******************************************************************************************************************
    //** FindAll
    //******************************************************************************************************************

    @Test
    @DatabaseSetup("init-users-data.xml") // setups the database with data from the configuration file.
    //@DatabaseTearDown("clear-users-data.xml") // clears the inserted data
    //@ExpectedDatabase("init-users-data.xml") // verify that no changes are made to the database.
    public void testFindAll() throws Exception
    {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/users/"));

        // perform common verifications
        TestHelper.performCommonVerifications(resultActions);

        // Get the collection of user entries by using the JsonPath expression $ and ensure that that
        // no user entries are returned.
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

        // Verify JSON content in response using JsonPath
        verifyUsersInJsonResponse(resultActions);
    }

    public void verifyUsersInJsonResponse(ResultActions resultActions) throws Exception
    {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date dennisDate = formatter.parse("05/10/69");
        Date robertDate = formatter.parse("10/29/73");

        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[0].userName", Matchers.is("dennisb")));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", Matchers.is("dennis")));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName", Matchers.is("bergkamp")));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[0].emailAddress", Matchers.is("dennis@gunners.com")));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth", Matchers.is(dennisDate.getTime())));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[1].userName", Matchers.is("robertp")));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName", Matchers.is("robert")));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName", Matchers.is("pires")));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[1].emailAddress", Matchers.is("robert@gunners.com")));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$[1].dateOfBirth", Matchers.is(robertDate.getTime())));
    }


    //******************************************************************************************************************
    //** Common
    //******************************************************************************************************************

}
