package com.example.demo.Service;

import com.example.demo.Entity.Detail;
import com.example.demo.Repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailService {

    @Autowired
    private DetailRepository detailRepository;

    public List<Detail> getAll() {
        return detailRepository.findAll();
    }

    public Detail save(Detail detail) {
        return detailRepository.save(detail);
    }

    public void delete(Long id) {
        detailRepository.deleteById(id);
    }

    public void find(Long id) {
        detailRepository.findById(id);
    }

}
