package com.otec.demo.player;

import com.otec.demo.exceptions.BadRequestException;
import com.otec.demo.exceptions.PlayerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;
    private PlayerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PlayerService(playerRepository);
    }


    @Test
    void canGetAllPlayers() {
        //when
        underTest.getAllPlayers();
        //then
        verify(playerRepository).findAll();
    }


    @Test
    void canAddPlayer() {
        //given
        Player player = new Player("Azula", "azula@firenation.com", Gender.FEMALE, 0, 0);
        //when
        underTest.addPlayer(player);
        //then
        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepository).save(playerArgumentCaptor.capture());

        Player capturedPlayer = playerArgumentCaptor.getValue();
        assertThat(capturedPlayer).isEqualTo(player);
    }

    @Test
    void canAddPlayerWillItThrowEmailTaken() {
        //given
        Player player = new Player("Azula", "azula@firenation.com", Gender.FEMALE, 0, 0);

        given(playerRepository.existsPlayerByEmail(player.getEmail()))
                .willReturn(true);
        //when
        //then
        assertThatThrownBy(() -> underTest.addPlayer(player))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(player.getEmail() + " is already taken");
    }

    @Test
    void canAddPlayerWillItThrowEmailUsername() {
        //given
        Player player = new Player("Azula", "azula@firenation.com", Gender.FEMALE, 0, 0);

        given(playerRepository.existsPlayerByUsername(player.getUsername()))
                .willReturn(true);
        //when
        //then
        assertThatThrownBy(() -> underTest.addPlayer(player))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(player.getUsername() + " is already taken");

        verify(playerRepository, never()).save(any()); // check if the playerRepository.save(player) method is never executed

    }

    @Test
    void cantFindPlayerByIdWillThrowPlayerNotFound() {
        //given
        Player player = new Player("Azula", "azula@firenation.com", Gender.FEMALE, 0, 0);
        long idPlayer = player.getId();
        //when
        //then
        assertThatThrownBy(() -> underTest.deletePlayer(idPlayer))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessageContaining("Player doesn't exist");
    }

    @Test
    void canFindPlayerById() {
        //given
        long idPlayer = 2;
        //then
        given(playerRepository.existsById(idPlayer))
                .willReturn(true);

        assertThat(underTest.ifPlayerExistById(idPlayer)).isTrue();
    }

    @Test
    void canDeletePlayer() {
        //given
        long id = 2;
        given(playerRepository.existsById(id)).willReturn(true);
        //when
        underTest.deletePlayer(id);
        //then
        verify(playerRepository).deleteById(id);
    }
}