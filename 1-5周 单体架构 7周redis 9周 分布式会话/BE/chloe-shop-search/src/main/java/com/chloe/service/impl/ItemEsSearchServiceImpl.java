package com.chloe.service.impl;

import com.chloe.common.utils.PagedGridResult;
import com.chloe.model.Items;
import com.chloe.service.ItemEsSearchService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemEsSearchServiceImpl implements ItemEsSearchService {
    @Resource
    private ElasticsearchRestTemplate template;

    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        String preTag = "<font color='red'>";
        String postTag = "</font>";

        Pageable pageRequest = PageRequest.of(page, pageSize, Sort.by("id").ascending());

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("itemName", keywords))
                .withHighlightFields(new HighlightBuilder.Field("itemName").preTags(preTag).postTags(postTag))
                .withPageable(pageRequest).build();

        SearchHits<Items> search = template.search(nativeSearchQuery, Items.class, IndexCoordinates.of("chloe"));

        List<Items> items = search.map(itemSearchHit -> {
            String itemName = itemSearchHit.getHighlightField("itemName").stream().findFirst().get();
            Items content = itemSearchHit.getContent();
            content.setItemName(itemName);
            return content;
        }).toList();

        PagedGridResult result = new PagedGridResult();

        result.setRows(items);
        result.setRecords(search.getTotalHits());
        result.setPage(page + 1);

        return null;
    }
}
