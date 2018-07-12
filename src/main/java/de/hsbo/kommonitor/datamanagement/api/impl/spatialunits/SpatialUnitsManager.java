package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;



import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
@Component
public class SpatialUnitsManager {
	
	
	private static Logger logger = LoggerFactory.getLogger(SpatialUnitsManager.class);

	/**
	*
	*/
//	@PersistenceContext
//	EntityManager em;
	
	@Autowired
	SpatialUnitsRepository spatialUnitsRepo;
	
	
	//TODO: Methoden zum handling der SpatialUnits
	
}
