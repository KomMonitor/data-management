package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitJoinEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionDetailType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewElementType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewSpatialUnitElementType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewType;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.ResourceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link OrganizationalUnitManager#getOrganizationalUnitPermissionsById} covering the
 * per-dataset grouping and the organizational-unit filtering that back the
 * {@code GET /organizationalUnits/{id}/permissions} endpoint.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrganizationalUnitPermissionsTest {

    private static final String OU_ID = "9617b330-bd96-43b3-882c-2b26fbd8e9ef";
    private static final String OTHER_OU_ID = "11111111-1111-1111-1111-111111111111";

    @Mock
    private OrganizationalUnitRepository organizationalUnitRepository;

    @Mock
    private GeoresourcesMetadataRepository georesourcesMetadataRepository;

    @Mock
    private SpatialUnitsMetadataRepository spatialUnitsMetadataRepository;

    @Mock
    private IndicatorsMetadataRepository indicatorsMetadataRepository;

    @Mock
    private IndicatorSpatialUnitsRepository indicatorSpatialUnitsRepository;

    @InjectMocks
    private OrganizationalUnitManager manager;

    @Test
    public void missingOrganizationalUnitThrowsResourceNotFound() {
        when(organizationalUnitRepository.findByOrganizationalUnitId(OU_ID)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> manager.getOrganizationalUnitPermissionsById(OU_ID, null));
    }

    @Test
    public void groupsMatchingGrantsPerDatasetAndExcludesOtherOrganizationalUnits() throws Exception {
        stubOuFound();

        String datasetId = UUID.randomUUID().toString();
        String editorId = UUID.randomUUID().toString();
        String viewerId = UUID.randomUUID().toString();
        String foreignId = UUID.randomUUID().toString();

        // One dataset with two grants for our OU plus one grant for a different OU that must be dropped.
        MetadataGeoresourcesEntity dataset = georesource(datasetId, "Schulen",
                permission(editorId, PermissionLevelType.EDITOR, OU_ID),
                permission(viewerId, PermissionLevelType.VIEWER, OU_ID),
                permission(foreignId, PermissionLevelType.EDITOR, OTHER_OU_ID));
        when(georesourcesMetadataRepository.findAll()).thenReturn(List.of(dataset));
        when(spatialUnitsMetadataRepository.findAll()).thenReturn(List.of());
        when(indicatorsMetadataRepository.findAll()).thenReturn(List.of());
        when(indicatorSpatialUnitsRepository.findAll()).thenReturn(List.of());

        OrganizationalUnitPermissionOverviewType overview =
                manager.getOrganizationalUnitPermissionsById(OU_ID, null);

        List<OrganizationalUnitPermissionOverviewElementType> georesources =
                overview.getPermissions().getGeoresources();
        assertEquals(1, georesources.size(), "exactly one element per dataset");

        OrganizationalUnitPermissionOverviewElementType element = georesources.get(0);
        assertEquals(UUID.fromString(datasetId), element.getId());
        assertEquals("Schulen", element.getName());

        assertEquals(2, element.getPermissions().size(), "only the two grants for this OU are nested");
        Set<String> permissionIds = element.getPermissions().stream()
                .map(d -> d.getPermissionId().toString())
                .collect(Collectors.toSet());
        assertEquals(Set.of(editorId, viewerId), permissionIds);
        assertTrue(permissionIds.stream().noneMatch(foreignId::equals),
                "a grant owned by another OU must not appear");

        Set<String> levels = element.getPermissions().stream()
                .map(OrganizationalUnitPermissionDetailType::getPermissionLevel)
                .collect(Collectors.toSet());
        assertEquals(Set.of("editor", "viewer"), levels);
    }

    @Test
    public void datasetWithoutMatchingGrantIsOmitted() throws Exception {
        stubOuFound();

        MetadataGeoresourcesEntity dataset = georesource(UUID.randomUUID().toString(), "Foreign only",
                permission(UUID.randomUUID().toString(), PermissionLevelType.EDITOR, OTHER_OU_ID));
        when(georesourcesMetadataRepository.findAll()).thenReturn(List.of(dataset));
        when(spatialUnitsMetadataRepository.findAll()).thenReturn(List.of());
        when(indicatorsMetadataRepository.findAll()).thenReturn(List.of());
        when(indicatorSpatialUnitsRepository.findAll()).thenReturn(List.of());

        OrganizationalUnitPermissionOverviewType overview =
                manager.getOrganizationalUnitPermissionsById(OU_ID, null);

        assertTrue(overview.getPermissions().getGeoresources().isEmpty(),
                "a dataset with no grant for the OU yields no element");
    }

    @Test
    public void indicatorSpatialUnitElementsCarrySpatialUnitId() throws Exception {
        stubOuFound();

        String indicatorId = UUID.randomUUID().toString();
        String spatialUnitId = UUID.randomUUID().toString();
        String permissionId = UUID.randomUUID().toString();

        when(georesourcesMetadataRepository.findAll()).thenReturn(List.of());
        when(spatialUnitsMetadataRepository.findAll()).thenReturn(List.of());
        when(indicatorsMetadataRepository.findAll()).thenReturn(List.of());
        IndicatorSpatialUnitJoinEntity joinEntity = join(indicatorId, "Population density", spatialUnitId,
                permission(permissionId, PermissionLevelType.VIEWER, OU_ID));
        when(indicatorSpatialUnitsRepository.findAll()).thenReturn(List.of(joinEntity));

        OrganizationalUnitPermissionOverviewType overview =
                manager.getOrganizationalUnitPermissionsById(OU_ID, null);

        List<OrganizationalUnitPermissionOverviewSpatialUnitElementType> elements =
                overview.getPermissions().getIndicatorspatialunits();
        assertEquals(1, elements.size());

        OrganizationalUnitPermissionOverviewSpatialUnitElementType element = elements.get(0);
        assertEquals(UUID.fromString(indicatorId), element.getId());
        assertEquals("Population density", element.getName());
        assertEquals(UUID.fromString(spatialUnitId), element.getSpatialUnitId());
        assertEquals(1, element.getPermissions().size());
        assertEquals(permissionId, element.getPermissions().get(0).getPermissionId().toString());
        assertEquals("viewer", element.getPermissions().get(0).getPermissionLevel());
    }

    @Test
    public void resourceTypeFilterScopesResultToRequestedGroupOnly() throws Exception {
        stubOuFound();

        MetadataGeoresourcesEntity dataset = georesource(UUID.randomUUID().toString(), "Schulen",
                permission(UUID.randomUUID().toString(), PermissionLevelType.EDITOR, OU_ID));
        when(georesourcesMetadataRepository.findAll()).thenReturn(List.of(dataset));

        OrganizationalUnitPermissionOverviewType overview =
                manager.getOrganizationalUnitPermissionsById(OU_ID, ResourceType.GEORESOURCES);

        assertEquals(1, overview.getPermissions().getGeoresources().size());
        assertTrue(overview.getPermissions().getSpatialunits().isEmpty());
        assertTrue(overview.getPermissions().getIndicators().isEmpty());
        assertTrue(overview.getPermissions().getIndicatorspatialunits().isEmpty());

        // The non-requested groups must not even be queried.
        org.mockito.Mockito.verifyNoInteractions(
                spatialUnitsMetadataRepository, indicatorsMetadataRepository, indicatorSpatialUnitsRepository);
    }

    @Test
    public void overviewCarriesOrganizationalUnitHeader() throws Exception {
        stubOuFound();
        when(georesourcesMetadataRepository.findAll()).thenReturn(List.of());
        when(spatialUnitsMetadataRepository.findAll()).thenReturn(List.of());
        when(indicatorsMetadataRepository.findAll()).thenReturn(List.of());
        when(indicatorSpatialUnitsRepository.findAll()).thenReturn(List.of());

        OrganizationalUnitPermissionOverviewType overview =
                manager.getOrganizationalUnitPermissionsById(OU_ID, null);

        assertEquals(OU_ID, overview.getOrganizationalUnitId());
        assertEquals("OU " + OU_ID, overview.getName());
    }

    private OrganizationalUnitEntity ou(String id) {
        OrganizationalUnitEntity ou = mock(OrganizationalUnitEntity.class);
        when(ou.getOrganizationalUnitId()).thenReturn(id);
        when(ou.getName()).thenReturn("OU " + id);
        when(ou.getContact()).thenReturn("contact@example.org");
        when(ou.getDescription()).thenReturn("description");
        return ou;
    }

    private PermissionEntity permission(String permissionId, PermissionLevelType level, String ownerOuId) {
        OrganizationalUnitEntity owner = ou(ownerOuId);
        PermissionEntity p = mock(PermissionEntity.class);
        when(p.getPermissionId()).thenReturn(permissionId);
        when(p.getPermissionLevel()).thenReturn(level);
        when(p.getOrganizationalUnit()).thenReturn(owner);
        return p;
    }

    private MetadataGeoresourcesEntity georesource(String datasetId, String name, PermissionEntity... perms) {
        MetadataGeoresourcesEntity dataset = mock(MetadataGeoresourcesEntity.class);
        when(dataset.getDatasetId()).thenReturn(datasetId);
        when(dataset.getDatasetName()).thenReturn(name);
        when(dataset.getPermissions()).thenReturn(new HashSet<>(Set.of(perms)));
        return dataset;
    }

    private MetadataSpatialUnitsEntity spatialUnit(String datasetId, String name, PermissionEntity... perms) {
        MetadataSpatialUnitsEntity dataset = mock(MetadataSpatialUnitsEntity.class);
        when(dataset.getDatasetId()).thenReturn(datasetId);
        when(dataset.getDatasetName()).thenReturn(name);
        when(dataset.getPermissions()).thenReturn(new HashSet<>(Set.of(perms)));
        return dataset;
    }

    private MetadataIndicatorsEntity indicator(String datasetId, String name, PermissionEntity... perms) {
        MetadataIndicatorsEntity dataset = mock(MetadataIndicatorsEntity.class);
        when(dataset.getDatasetId()).thenReturn(datasetId);
        when(dataset.getDatasetName()).thenReturn(name);
        when(dataset.getPermissions()).thenReturn(new HashSet<>(Set.of(perms)));
        return dataset;
    }

    private IndicatorSpatialUnitJoinEntity join(String indicatorId, String indicatorName,
                                                String spatialUnitId, PermissionEntity... perms) {
        IndicatorSpatialUnitJoinEntity join = mock(IndicatorSpatialUnitJoinEntity.class);
        when(join.getIndicatorMetadataId()).thenReturn(indicatorId);
        when(join.getIndicatorName()).thenReturn(indicatorName);
        when(join.getSpatialUnitId()).thenReturn(spatialUnitId);
        when(join.getPermissions()).thenReturn(new HashSet<>(Set.of(perms)));
        return join;
    }

    private void stubOuFound() {
        OrganizationalUnitEntity ouEntity = ou(OU_ID);
        when(organizationalUnitRepository.findByOrganizationalUnitId(OU_ID)).thenReturn(ouEntity);
    }
}
