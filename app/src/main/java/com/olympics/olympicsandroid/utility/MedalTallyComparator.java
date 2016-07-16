package com.olympics.olympicsandroid.utility;

import com.olympics.olympicsandroid.model.MedalTallyOrganization;
import com.olympics.olympicsandroid.model.Organization;

import java.util.Comparator;

/**
 * Created by tkmagz4 on 7/15/16.
 */
public class MedalTallyComparator implements Comparator<MedalTallyOrganization> {

    private Organization userSelectedOrganization;

    public MedalTallyComparator() {
        this.userSelectedOrganization = null;
    }

    public MedalTallyComparator(Organization userSelectedOrganization) {
        this.userSelectedOrganization = userSelectedOrganization;
    }

    @Override
    public int compare(MedalTallyOrganization lhs, MedalTallyOrganization rhs) {
        try {
            if (userSelectedOrganization != null && userSelectedOrganization.getId() != null) {
                if(userSelectedOrganization.getId().equalsIgnoreCase(lhs.getId()) &&
                        userSelectedOrganization.getId().equalsIgnoreCase(rhs.getId())) {
                    return Integer.parseInt(rhs.getTotal()) - Integer.parseInt(lhs.getTotal());
                } else if (userSelectedOrganization.getId().equalsIgnoreCase(lhs.getId())) {
                    return -1;
                } else if (userSelectedOrganization.getId().equalsIgnoreCase(rhs.getId())) {
                    return 1;
                } else {
                    return Integer.parseInt(rhs.getTotal()) - Integer.parseInt(lhs.getTotal());
                }
            } else {
                return Integer.parseInt(rhs.getTotal()) - Integer.parseInt(lhs.getTotal());
            }

        } catch (Exception ex) {
            return 0;
        }
    }
}
