/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here

package com.example.player.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.player.repository.PlayerRepository;
import com.example.player.model.*;

@Service
public class PlayerH2Service implements PlayerRepository {
    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Player> getAllPlayers() {
        List<Player> playerList = db.query("select * from TEAM", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerList);
        return players;
    }

    @Override
    public Player addPlayer(Player player) {
        db.update("insert into TEAM(playerName, jerseyNumber, role) values(?, ?, ?)", player.getPlayerName(), player.getJerseyNumber(), player.getRole());
        Player savedPlayer = db.queryForObject("select * from TEAM where playerName = ? and jerseyNumber = ?", new PlayerRowMapper(), player.getPlayerName(), player.getJerseyNumber());
        return savedPlayer;
    }

    @Override
    public Player getPlayer(int playerId) {
        try {
            Player player = db.queryForObject("select * from TEAM where playerId = ?", new PlayerRowMapper(), playerId);
            return player;
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Player updatePlayer(int playerId, Player player) {
        if (player.getPlayerName() != null) db.update("update TEAM set playerName = ? where playerId = ?", player.getPlayerName(), playerId);
        if (player.getJerseyNumber() != 0) db.update("update TEAM set jerseyNumber = ? where playerId = ?", player.getJerseyNumber(), playerId);
        if (player.getRole() != null) db.update("update TEAM set role = ? where playerId = ?", player.getRole(), playerId);

        return getPlayer(playerId);
    }

    public void deletePlayer(int playerId) {
        db.update("delete from TEAM where playerId = ?", playerId);
    }
}
