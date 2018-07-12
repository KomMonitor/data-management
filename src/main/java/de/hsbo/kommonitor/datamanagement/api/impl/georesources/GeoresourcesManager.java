package de.hsbo.kommonitor.datamanagement.api.impl.georesources;



import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsRepository;

@Transactional
@Repository
@Component
public class GeoresourcesManager {
	
	
	private static Logger logger = LoggerFactory.getLogger(GeoresourcesManager.class);

	/**
	*
	*/
//	@PersistenceContext
//	EntityManager em;
	
	@Autowired
	GeoresourcesRepository georesourcesRepo;
	
	
	//TODO: Methoden zum handling der Georesourcen
	
}
