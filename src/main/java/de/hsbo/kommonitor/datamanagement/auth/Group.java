package de.hsbo.kommonitor.datamanagement.auth;

public class Group {

    private Group parentGroup;

    private String name;

    public Group(String name) {
        this.name = name;
        this.parentGroup = null;
    }

    public Group(String name, Group parentGroup) {
        this.name = name;
        this.parentGroup = parentGroup;
    }

    public Group getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasParent() {
        return parentGroup != null;
    }
}
