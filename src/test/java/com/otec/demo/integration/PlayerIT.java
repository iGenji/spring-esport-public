package com.otec.demo.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.otec.demo.player.Gender;
import com.otec.demo.player.Player;
import com.otec.demo.player.PlayerRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class PlayerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlayerRepository playerRepository;

    private final Faker faker = new Faker();


    @Test
    void cantRegisterNewPlayer() throws Exception {
        //given
        Player player = new Player("Azula", "azula@firenation.com", Gender.MALE, 0, 0);
        //when
        mockMvc.perform(post("/api/v1/players").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(player)));
        //then
        //resultActions.andExpect(status().isOk());
        List<Player> listPlayers = playerRepository.findAll();
        assertThat(listPlayers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(player);
    }

    @Test
    void canRegisterNewPlayer() throws Exception {
        //given
        String fakeUsername = String.format("%s",faker.name().username());
        String fakeEmail = String.format("%s@gmail.com",fakeUsername);
        Player player = new Player(fakeUsername, fakeEmail, Gender.MALE, 0, 0);
        //when
        assertThatThrownBy(() -> mockMvc.perform(post("/api/v1/players").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(player))))
                .isExactlyInstanceOf(NestedServletException.class);
        //then
        List<Player> listPlayers = playerRepository.findAll();
        assertThat(listPlayers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").doesNotContain(player);
    }

    @Test
    void canDeletePlayer() throws Exception {
        String fakeUsername = String.format("%s",faker.name().username());
        String fakeEmail = String.format("%s@gmail.com",fakeUsername);
        Player player = new Player(fakeUsername, fakeEmail, Gender.MALE, 0, 0);

        mockMvc.perform(post("/api/v1/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isOk());

        MvcResult getPlayersResult = mockMvc.perform(get("/api/v1/players")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getPlayersResult
                .getResponse()
                .getContentAsString();

        List<Player> players = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        long id = players.stream()
                .filter(s -> s.getEmail().equals(player.getEmail()))
                .map(Player::getId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "student with email: " + fakeEmail + " not found"));

        // when
        ResultActions resultActions = mockMvc
                .perform(delete("/api/v1/players/" + id));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = playerRepository.existsById(id);
        assertThat(exists).isFalse();

    }

}
