package com.techlooper.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(classes = { VietnamworksJobStatisticControllerTest.class })
public class VietnamworksJobStatisticControllerTest {

   // @Value("classpath:json/VietnamworksJobStatisticControllerTest.json")
   // private Resource jsonResource;
   //
   // @Mock
   // private JobStatisticService vietnamWorksJobStatisticService;
   //
   // @InjectMocks
   // private VietnamworksJobStatisticController controller;
   //
   // private MockMvc mockMvc;

   @Before
   public void setup() {
      // MockitoAnnotations.initMocks(this);
      // mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
   }

   @Test
   public void testCountTechnicalJobs() throws Exception {
      // for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
      // Mockito.when(vietnamWorksJobStatisticService.count(term)).thenReturn(2L);
      // }
      //
      // String expectedJson =
      // Files.readAllLines(Paths.get(jsonResource.getURI())).stream()
      // .collect(Collectors.joining(""));
      // mockMvc.perform(get("/technical-job")).andExpect(status().isOk())
      // .andExpect(content().contentType("application/json;charset=UTF-8")).andExpect(content().json(expectedJson));
      //
      // for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
      // Mockito.verify(vietnamWorksJobStatisticService,
      // Mockito.times(1)).count(term);
      // }
   }
}
