package ru.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.server.entity.Score;
import ru.server.exeptions.ScoreNotFoundException;
import ru.server.repository.ScoreCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ScoreService {
    private ScoreCrudRepository scoreCrudRepository;

    public List<Score> getAllScores(){
        List<Score> list = new ArrayList<>();
        scoreCrudRepository.findAll().forEach(list::add);
        return list;
    }
    public Score save(Score score){
        return scoreCrudRepository.save(score);
    }

    public Optional<Score> findByCardNumber(String cardNumber){
        return scoreCrudRepository.findByCardNumber(cardNumber);
    }
}
