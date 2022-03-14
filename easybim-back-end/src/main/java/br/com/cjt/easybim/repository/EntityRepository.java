package br.com.cjt.easybim.repository;

public interface EntityRepository<T> {
	
	public long generateSequence(String seqName);
	
	<S extends T> S save(S entity);	
}
