package hu.nextent.peas.utils;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

import hu.nextent.peas.cache.ServiceCaches;

public class LabelMapProxy 
implements Map<String, String> 
{

	private String language;
	private ServiceCaches serviceCaches;
	
	
	public LabelMapProxy(String language, ServiceCaches serviceCaches) {
		this.language = language;
		this.serviceCaches = serviceCaches;
	}


	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String get(Object key) {
		return serviceCaches.getLabel((String)key, language);
	}


	@Override
	public String put(String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Collection<String> values() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Set<Entry<String, String>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
	


}
