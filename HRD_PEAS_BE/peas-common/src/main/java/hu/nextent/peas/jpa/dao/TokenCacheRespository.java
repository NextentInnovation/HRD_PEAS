package hu.nextent.peas.jpa.dao;

import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.entity.TokenCache;

@Transactional
public interface TokenCacheRespository 
extends DaoRepository<TokenCache, Long>
{

}
