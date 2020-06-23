SELECT i.id              AS itemId,
       i.updated_time    AS updatedTime,
       i.item_name       AS itemName,
       i.sell_counts     AS sellCounts,
       ii.url            AS imgUrl,
       ts.price_discount AS priceDiscount
FROM items i
         LEFT JOIN items_img ii ON i.id = ii.item_id
         LEFT JOIN (
    SELECT item_id, MIN(price_discount) AS price_discount FROM items_spec GROUP BY item_id
) ts ON i.id = ts.item_id
WHERE ii.is_main = 1
  AND i.updated_time > :sql_last_value
