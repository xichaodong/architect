package com.chloe.test;

import com.chloe.Application;
import com.chloe.test.model.Student;
import org.elasticsearch.action.update.UpdateAction;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {
    @Resource
    private ElasticsearchRestTemplate template;
    @Resource
    private ElasticsearchOperations operations;
    @Resource
    private StuRepository repository;

    @Test
    public void createStuIndex() {
        template.putMapping(Student.class);
    }

    @Test
    public void deleteStuIndex() {
        template.deleteIndex(Student.class);
    }

    @Test
    public void create() {
        Student student = new Student();
        student.setId(1003L);
        student.setAge(18);
        student.setName("chloe");

        template.save(student);
    }

    @Test
    public void update() {
        Map<String, Object> params = new HashMap<>();
        params.put("age", 23);

        template.update(UpdateQuery.builder("1002").withDocument(Document.from(params)).build(), IndexCoordinates.of("index_stu"));
    }

    @Test
    public void get() {
        Student student = template.get("1003", Student.class);
        System.out.println(student.toString());
    }

    @Test
    public void search() {
        Criteria criteria = new Criteria();
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by("id"));

        SearchHits<Student> search = template.search(new CriteriaQuery(criteria, pageRequest), Student.class, IndexCoordinates.of("index_stu"));
        search.forEach(studentSearchHit -> System.out.println(studentSearchHit.getContent().toString()));
    }

    @Test
    public void highLightSearch() {
        String preTag = "<font color='red'>";
        String postTag = "</font>";

        Pageable pageRequest = PageRequest.of(0, 1, Sort.by("id").ascending());
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", "chloe"))
                .withHighlightFields(new HighlightBuilder.Field("name").preTags(preTag).postTags(postTag))
                .withPageable(pageRequest).build();

        SearchHits<Student> search = template.search(nativeSearchQuery, Student.class, IndexCoordinates.of("index_stu"));
        search.forEach(studentSearchHit -> {
            String desc = studentSearchHit.getHighlightField("name").stream().findFirst().get();
            Student content = studentSearchHit.getContent();
            content.setName(desc);
            System.out.println(content.toString());
        });

    }

    @Test
    public void delete() {
        template.delete("1002", IndexCoordinates.of("index_stu"));
    }
}
