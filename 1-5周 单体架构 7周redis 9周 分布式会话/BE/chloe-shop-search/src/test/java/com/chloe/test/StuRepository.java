package com.chloe.test;

import com.chloe.test.model.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StuRepository extends ElasticsearchRepository<Student, String> {

}
