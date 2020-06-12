package lt.galdebar.vilniustemp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.galdebar.vilniustemp.context.ClimaCellRemoteAPIConfig;
import lt.galdebar.vilniustemp.persistence.domain.TemperatureDTO;
import lt.galdebar.vilniustemp.persistence.domain.TemperatureEntity;
import lt.galdebar.vilniustemp.persistence.repositories.TemperatureRepo;
import lt.galdebar.vilniustemp.testhelpers.TestContainersConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static lt.galdebar.vilniustemp.testhelpers.EntityCreatorHelper.createEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {TestContainersConfig.Initializer.class})
@TestPropertySource(properties = "app.scheduling.enable=false")
class TemperatureControllerTest {
    private static final int DEFAULT_NUM_OF_READINGS_PER_DAY = 24;
    private static final int DEFAULT_HISTORY_DAYS = 1;

    @Autowired
    private TemperatureController controller;

    @Value("${climacell.refresh_period}")
    private String refreshPeriod;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TemperatureRepo repo;

    @Autowired
    private ClimaCellRemoteAPIConfig config;

    @BeforeTestExecution
    void beforeExecution() {
        config.setMaxHistoryDays(DEFAULT_HISTORY_DAYS); //reduce number of history days to limit API calls.
    }

    @BeforeEach
    void beforeEach() {
        repo.deleteAll();
    }

    @Test
    void contextLoads() {
        assertNotNull(controller);
    }

    @Test
    void whenGetCurrentTemperature_thenReturnSingleTemperatureDTO() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        TemperatureEntity tempEntity1 = createEntity(22f, now.minusHours(2));
        TemperatureEntity tempEntity2 = createEntity(12f, now.minusHours(1));

        repo.save(tempEntity1);
        repo.save(tempEntity2);

        String response = mockMvc.perform(get("/current"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertNotNull(response);
        assertFalse(response.trim().isEmpty());

        TemperatureDTO actualTemperature = objectMapper.readValue(response, TemperatureDTO.class);

        assertNotNull(actualTemperature);
        assertEquals(tempEntity2.getObservationTime().truncatedTo(ChronoUnit.MINUTES),
                actualTemperature.getObservationTime().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    void whenGetNextUpdateTime_thenReturnLocalDateTime() throws Exception {
        CronSequenceGenerator cronGenerator = new CronSequenceGenerator(refreshPeriod);
        LocalDateTime expectedTime = cronGenerator.next(new Date())
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        String response = mockMvc.perform(get("/nextupdate"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LocalDateTime actualTime = objectMapper.readValue(response, LocalDateTime.class);

        assertNotNull(response);
        assertTrue(response.contains(expectedTime.toString()));
        assertEquals(expectedTime, actualTime);
    }

    @Test
    void givenNoParameters_whenGetHistory_thenReturnTemperatureDTOList() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        TemperatureEntity tempEntity1 = createEntity(22f, now.minusHours(2));
        TemperatureEntity tempEntity2 = createEntity(12f, now.minusHours(1));

        repo.save(tempEntity1);
        repo.save(tempEntity2);

        String response = mockMvc.perform(get("/history"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<TemperatureDTO> actualList = objectMapper.readValue(
                response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, TemperatureDTO.class)
        );


        assertNotNull(actualList);
        assertEquals(repo.findAll().size(), actualList.size());
    }

    @Test
    void givenValidParameter_whenGetHistory_thenReturnTemperatureDTOList() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        int daysBack = 1;
        TemperatureEntity tempEntity1 = createEntity(22f, now.minusDays(5));
        TemperatureEntity tempEntity2 = createEntity(12f, now.minusHours(1));

        repo.save(tempEntity1);
        repo.save(tempEntity2);

        String response = mockMvc.perform(get("/history?days=" + daysBack))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<TemperatureDTO> actualList = objectMapper.readValue(
                response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, TemperatureDTO.class)
        );



        assertNotNull(actualList);
        assertEquals(2,actualList.size());
        assertEquals(tempEntity2.getObservationTime().truncatedTo(ChronoUnit.MINUTES),
                actualList.get(0).getObservationTime().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    void givenInvalidParameter_whenGetHistory_thenReturnBadRequest() throws Exception {
        int daysBack = -20;
        mockMvc.perform(get("/history?days=" + daysBack))
                .andExpect(status().isBadRequest());
    }
}