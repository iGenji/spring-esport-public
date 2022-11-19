package com.otec.demo.player;

import com.otec.demo.exceptions.BadRequestException;
import com.otec.demo.exceptions.ObjectNullException;
import com.otec.demo.exceptions.PlayerNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import com.otec.demo.util.Util;

@AllArgsConstructor
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    /**
     * Function that returns all players from the database.
     *
     * @return List of players
     */
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Function that saves the player into the database.
     *
     * @param player Player
     * @return integer, 1 if the username exists, 2 if the email exists 0 if it succeeds.
     * @throws BadRequestException if the email or username is already taken.
     */
    public void addPlayer(Player player) throws ObjectNullException {
        Util.checkObject(player);

        if(checkPlayerUsernameExist(player.getUsername())){
            throw new BadRequestException(player.getUsername()+" is already taken");
        }else if(checkPlayerEmailExists(player.getEmail())){
            throw new BadRequestException(player.getEmail()+" is already taken");
        }

        playerRepository.save(player);
    }

    /**
     * Function that deletes the player from the database if the id is in it.
     *
     * @param playerId ID of the player
     */
    public void deletePlayer(Long playerId) {
        if(!ifPlayerExistById(playerId)){
            throw new PlayerNotFoundException("Player doesn't exist");
        }

        playerRepository.deleteById(playerId);
    }

    /**
     * This function checks if the player exist, throw an exception if not found
     *
     * @param id Long, id of the player.
     * @return true if it was found, false otherwise.
     */
    public boolean ifPlayerExistById(Long id) {
        if (!playerRepository.existsById(id)) {
            System.out.println("Player not found");
            return false;
        }
        return true;
    }

    /**
     * This functions checks if the player username exists.
     *
     * @param playerUsername String of the username.
     * @return true if it exists false otherwise.
     */
    private boolean checkPlayerUsernameExist(String playerUsername) {
        if (playerRepository.existsPlayerByUsername(playerUsername)) {
            System.out.println("username exists");
            return true;
        }
        return false;
    }

    /**
     * This functions check if the player email.
     *
     * @param playerEmail String of the email.
     * @return true if it contains the email false otherwise.
     */
    private boolean checkPlayerEmailExists(String playerEmail) {
        if(playerRepository.existsPlayerByEmail(playerEmail)) {
            System.out.println("Email exist");
            return true;
        }
        return false;
    }
}
