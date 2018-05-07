package com.jahia.elasticsearch.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for Application
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SampleElasticsearchAppTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void verifyGetAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/account/abc123")).andExpect(status().isOk());
    }
}
