package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.util.ArrayList;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.UuidGenerator;

/**
 * A named spatial-unit hierarchy owned by a mandant (an {@link OrganizationalUnitEntity}
 * with {@code isMandant == true}). A mandant may own one or more hierarchies; each hierarchy groups spatial
 * units of that mandant into an ordered sequence via {@link SpatialUnitHierarchyMembershipEntity}.
 */
@Entity(name = "SpatialUnitHierarchies")
@Table(name = "spatialunithierarchies")
public class SpatialUnitHierarchyEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private String id = null;

    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mandant_organizationalunitid")
    private OrganizationalUnitEntity mandant;

    @Column(name = "ispublic", nullable = false)
    private boolean isPublic = false;

    @OneToMany(mappedBy = "hierarchy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SpatialUnitHierarchyMembershipEntity> memberships = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrganizationalUnitEntity getMandant() {
        return mandant;
    }

    public void setMandant(OrganizationalUnitEntity mandant) {
        this.mandant = mandant;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public List<SpatialUnitHierarchyMembershipEntity> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<SpatialUnitHierarchyMembershipEntity> memberships) {
        this.memberships = memberships;
    }
}
