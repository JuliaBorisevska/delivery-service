package search;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHit;

import play.Logger;
import play.Logger.ALogger;
import entity.Contact;


public class ContactSearchService implements SearchService<Contact, ContactSearchBean> {

	private static ALogger logger = Logger.of(ContactSearchService.class);
	public static final String INDEX_NAME = "delivery";
	public static final String TYPE_NAME  = "contact";
	public static final String FIRST_NAME_FIELD = "first_name";
	public static final String LAST_NAME_FIELD  = "last_name";
	public static final String MIDDLE_NAME_FIELD  = "middle_name";
	public static final String DATE_FIELD = "birth_date";
	public static final String PRICE_FIELD  = "total_price";
	public static final String COMPANY_ID_FIELD = "company_id";
	public static final String TOWN_FIELD  = "town";
	public static final String STREET_FIELD  = "street";
	public static final String HOUSE_FIELD  = "house";
	public static final String FLAT_FIELD  = "flat";
	public static final String TEMPLATE = "*%s*";

	
	@Override
	public Map<String, Object> putJsonDocument(Contact contact) {
		Map<String, Object> jsonDocument = new HashMap<String, Object>();

		jsonDocument.put(FIRST_NAME_FIELD, contact.getFirstName());
		jsonDocument.put(LAST_NAME_FIELD, contact.getLastName());
		jsonDocument.put(MIDDLE_NAME_FIELD, contact.getMiddleName());
		jsonDocument.put(DATE_FIELD, contact.getBirthday());
		jsonDocument.put(COMPANY_ID_FIELD, contact.getCompanyByCompanyId().getId());
		jsonDocument.put(TOWN_FIELD, contact.getTown());
		jsonDocument.put(STREET_FIELD, contact.getStreet());
		jsonDocument.put(HOUSE_FIELD, contact.getHouse());
		jsonDocument.put(FLAT_FIELD, contact.getFlat());

		return jsonDocument;
	}

	@Override
	public void search(ContactSearchBean searchBean) {
		try {
			String statement;
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
					.must(QueryBuilders.termQuery(COMPANY_ID_FIELD, searchBean.getCompanyId()));
			if (searchBean.getHouse()!=null){
				queryBuilder.must(QueryBuilders.termQuery(HOUSE_FIELD, searchBean.getHouse()));    
			}
			if (searchBean.getFlat()!=null){
				queryBuilder.must(QueryBuilders.termQuery(FLAT_FIELD, searchBean.getFlat()));    
			}
			if (searchBean.getFirstName()!=null){
				statement = String.format(TEMPLATE, searchBean.getFirstName().trim());
				queryBuilder.must(QueryBuilders.queryStringQuery(statement).field(FIRST_NAME_FIELD));     
			}
			if (searchBean.getLastName()!=null){
				statement = String.format(TEMPLATE, searchBean.getLastName().trim());
				queryBuilder.must(QueryBuilders.queryStringQuery(statement).field(LAST_NAME_FIELD));      
			}
			if (searchBean.getMiddleName()!=null){
				statement = String.format(TEMPLATE, searchBean.getMiddleName().trim());
				queryBuilder.must(QueryBuilders.queryStringQuery(statement).field(MIDDLE_NAME_FIELD));    
			}
			if (searchBean.getTown()!=null){
				statement = String.format(TEMPLATE, searchBean.getTown().trim());
				queryBuilder.must(QueryBuilders.queryStringQuery(statement).field(TOWN_FIELD));    
			}
			if (searchBean.getStreet()!=null){
				statement = String.format(TEMPLATE, searchBean.getStreet().trim());
				queryBuilder.must(QueryBuilders.queryStringQuery(statement).field(STREET_FIELD));    
			}
			if (searchBean.getDateMin()!=null){
				queryBuilder.must(QueryBuilders.rangeQuery(DATE_FIELD).from(searchBean.getDateMin()).includeLower(true));    
			}
			if (searchBean.getDateMax()!=null){
				queryBuilder.must(QueryBuilders.rangeQuery(DATE_FIELD).to(searchBean.getDateMax()).includeUpper(true));    
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
