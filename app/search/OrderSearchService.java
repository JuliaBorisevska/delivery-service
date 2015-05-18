package search;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHit;

import play.Logger;
import play.Logger.ALogger;
import entity.Order;

public class OrderSearchService implements SearchService<Order, OrderSearchBean> {
	private static ALogger logger = Logger.of(OrderSearchService.class);
	public static final String INDEX_NAME = "delivery";
	public static final String TYPE_NAME  = "order";
	public static final String CUSTOMER_FIELD = "customer";
	public static final String RECIPIENT_FIELD  = "recipient";
	public static final String DATE_FIELD = "order_date";
	public static final String PRICE_FIELD  = "total_price";
	public static final String COMPANY_ID_FIELD = "company_id";
	public static final String STATUS_FIELD  = "status_title";

	
	@Override
	public Map<String, Object> putJsonDocument(Order order) {
		Map<String, Object> jsonDocument = new HashMap<String, Object>();

		jsonDocument.put(CUSTOMER_FIELD, order.getCustomerByContactId().getLastName());
		jsonDocument.put(RECIPIENT_FIELD, order.getRecipientByContactId().getLastName());
		jsonDocument.put(DATE_FIELD, order.getOrderDate());
		jsonDocument.put(PRICE_FIELD, order.getTotalPrice());
		jsonDocument.put(STATUS_FIELD, order.getStatusByStatusId().getTitle());
		jsonDocument.put(COMPANY_ID_FIELD, order.getRecipientByContactId().getCompanyByCompanyId().getId());

		return jsonDocument;
	}

	@Override
	public void search(OrderSearchBean searchBean) {
		try {
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
					.must(QueryBuilders.termQuery(COMPANY_ID_FIELD, searchBean.getCompanyId()));
			if (searchBean.getCustomer()!=null){
				queryBuilder.must(QueryBuilders.prefixQuery(CUSTOMER_FIELD, searchBean.getCustomer().toLowerCase()));    
			}
			if (searchBean.getRecipient()!=null){
				queryBuilder.must(QueryBuilders.prefixQuery(RECIPIENT_FIELD, searchBean.getRecipient().toLowerCase()));    
			}
			if (searchBean.getStatusTitleList()!=null){
				StringBuilder sb = new StringBuilder();
				for (String status : searchBean.getStatusTitleList()){
					sb.append(status);
				}
				queryBuilder.must(QueryBuilders.queryStringQuery(sb.toString()).field(STATUS_FIELD));    
			}		
			if (searchBean.getDateMin()!=null){
				queryBuilder.must(QueryBuilders.rangeQuery(DATE_FIELD).from(searchBean.getDateMin()).includeLower(true));    
			}
			if (searchBean.getDateMax()!=null){
				queryBuilder.must(QueryBuilders.rangeQuery(DATE_FIELD).to(searchBean.getDateMax()).includeUpper(true));    
			}
			if (searchBean.getPriceMin()!=null){
				queryBuilder.must(QueryBuilders.rangeQuery(PRICE_FIELD).from(searchBean.getPriceMin()).includeLower(true));    
			}
			if (searchBean.getPriceMax()!=null){
				queryBuilder.must(QueryBuilders.rangeQuery(PRICE_FIELD).to(searchBean.getPriceMax()).includeUpper(true));    
			}
            SearchResponse response = ClientProvider.instance().getClient()
                    .prepareSearch(INDEX_NAME)
                    .setTypes(TYPE_NAME)
                    .setQuery(queryBuilder)
                    .setFrom((searchBean.getPageNumber()-1)*searchBean.getPageSize())
                    .setSize(searchBean.getPageSize()).setExplain(true)
                    .execute()
                    .actionGet();

            searchBean.getIds().clear();
            searchBean.setTotalPages(response.getHits().getTotalHits());
            if (response != null) {
                for (SearchHit hit : response.getHits()) {
                	searchBean.getIds().add(Long.parseLong(hit.getId()));
                }
            }
            logger.info("SEARSH REQUEST - {}, TOTAL - {}, RESULT - {}", queryBuilder.toString(), response.getHits().getTotalHits(), searchBean.getIds());
        } catch (IndexMissingException ex){
            logger.error("Exception in search: {}", ex);
        }
		
	}

}
