/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hsbo.kommonitor.datamanagement.auth;

import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;
import jakarta.annotation.PostConstruct;

import de.hsbo.kommonitor.datamanagement.api.impl.users.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * provides metadata repositories and AuthInfoProviderFactory to unmanaged classes
 * @author Arne
 */
@Service
public final class AuthHelperService {
 
    private static AuthHelperService Instance;
    
    @Autowired
    private GeoresourcesMetadataRepository georesourceRepository;
    
    @Autowired
    private IndicatorsMetadataRepository indicatorRepository;
    
    @Autowired
    private SpatialUnitsMetadataRepository spatialunitRepository;
    
    @Autowired
    private IndicatorSpatialUnitsRepository indicatorspatialUnitsRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;
    
    @Autowired
    private AuthInfoProviderFactory authInfoProviderFactory;
    
    @PostConstruct
    private void init(){
        AuthHelperService.Instance = this;
    }
    
    private AuthHelperService(){}
    
    public AuthInfoProviderFactory getAuthInfoProviderFactory(){
        return this.authInfoProviderFactory;
    }
    
    public GeoresourcesMetadataRepository getGeoresourceRepository(){
        return this.georesourceRepository;
    } 
    
    public IndicatorsMetadataRepository getIndicatorRepository(){
        return this.indicatorRepository;
    }
    
    public SpatialUnitsMetadataRepository getSpatialunitsRepository(){
        return this.spatialunitRepository;
    }
    
    public IndicatorSpatialUnitsRepository getIndicatorSpatialunitsRepository(){
        return this.indicatorspatialUnitsRepository;
    }

    public UserInfoRepository getUserInfoRepository(){
        return this.userInfoRepository;
    }
    
    /**
     * @return singleton instance of AuthHelperService
     */
    public static AuthHelperService GetInstance(){
        if(AuthHelperService.Instance != null){
            return AuthHelperService.Instance;
        }else{
            throw new IllegalStateException("AuthHelperService instance is not initialized");
        }
    }
    
}
