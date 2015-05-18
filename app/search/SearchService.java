package search;

import java.util.Map;


public interface SearchService<T, P extends SearchBean> {
	
	Map<String, Object> putJsonDocument(T entity);
	
	void search(P searchBean);
	
}
