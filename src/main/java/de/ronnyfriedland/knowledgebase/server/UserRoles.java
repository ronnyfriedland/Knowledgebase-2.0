package de.ronnyfriedland.knowledgebase.server;

/**
 * @author ronnyfriedland
 */
public enum UserRoles {

    USER("user"), ADMIN("admin");

    UserRoles(final String roleName) {
        this.roleName = roleName;
    }

    private String roleName;

    public String getRoleName() {
        return roleName;
    }
}
