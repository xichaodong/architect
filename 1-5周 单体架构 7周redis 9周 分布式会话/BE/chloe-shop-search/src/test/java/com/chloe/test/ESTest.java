package com.chloe.test;

import com.chloe.Application;
import com.chloe.test.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {
    @Resource
    private ElasticsearchRestTemplate template;
    @Resource
    private StuRepository repository;

    @Test
    public void createStuIndex() {
        Student student = new Student();
        student.setId(1001L);
        student.setAge(18);
        student.setName("chloe");
        
        template.putMapping(Student.class);
//        repository.save(student);
    }
}
