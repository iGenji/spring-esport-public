package com.otec.demo.player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void checkIfPlayerByUsernameExists() {
        //given
        String username = "Azula";
        Player player = new Player(username,"azula@firenation.com",Gender.FEMALE,0,0);
        underTest.save(player);
        //when
        boolean expected = underTest.existsPlayerByUsername(username);
        //then
        assertThat(expected).isTrue();
    }

    @Test
    void checkIfPlayerByEmailExists() {
        //given
        String email = "azula@firenation.com";
        Player player = new Player("Azula",email,Gender.FEMALE,0,0);
        underTest.save(player);
        //when
        boolean expected = underTest.existsPlayerByEmail(email);
        //then
        assertThat(expected).isTrue();
    }

    @Test
    void checkIfPlayerByEmailDontExists(){
        //given
        String email = "azula@firenation.com";
        Player player = new Player("Azula","test@gmail.com",Gender.FEMALE,0,0);
        underTest.save(player);
        //when
        boolean expected = underTest.existsPlayerByEmail(email);
        //then
        assertThat(expected).isFalse();
    }

    @Test
    void checkIfPlayerByUsernameDontExists(){
        //given
        String username = "Azula";
        Player player = new Player("Sei","sei@dojoacerecords.com",Gender.MALE,10000,95);
        underTest.save(player);
        //when
        boolean expected = underTest.existsPlayerByUsername(username);
        //then
        assertThat(expected).isFalse();
    }
}