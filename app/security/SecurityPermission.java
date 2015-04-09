package security;

import be.objectify.deadbolt.core.models.Permission;

/**
 * Created by antonkw on 07.04.2015.
 */
public class SecurityPermission implements Permission {
    private String value;

    public SecurityPermission(String permissionValue) {
        value = permissionValue;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
