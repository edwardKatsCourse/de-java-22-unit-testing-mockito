package com.example.employeecompany.repository.impl;

import com.example.employeecompany.repository.MyRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MyRepositoryImpl implements MyRepository {

    @Override
    public String findById(Long id) {
        return String.valueOf(id);
    }
}
