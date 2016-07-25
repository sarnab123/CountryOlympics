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
                if (userSelectedOrganization.getId().equalsIgnoreCase(lhs.getId()) &&
                        userSelectedOrganization.getId().equalsIgnoreCase(rhs.getId())) {
                    return getMedalcountDifference(lhs, rhs);
                } else if (userSelectedOrganization.getId().equalsIgnoreCase(lhs.getId())) {
                    return -1;
                } else if (userSelectedOrganization.getId().equalsIgnoreCase(rhs.getId())) {
                    return 1;
                } else {
                    return getMedalcountDifference(lhs, rhs);
                }
            } else {
                return getMedalcountDifference(lhs, rhs);
            }

        } catch (Exception ex) {
            return 0;
        }
    }

    private int getMedalcountDifference(MedalTallyOrganization lhs, MedalTallyOrganization rhs) {

        try {

            int medalCountDifference = Integer.parseInt(rhs.getGold()) - Integer.parseInt(lhs
                    .getGold());
            if (medalCountDifference == 0) {
                medalCountDifference = Integer.parseInt(rhs.getSilver()) - Integer.parseInt(lhs
                        .getSilver());
                if (medalCountDifference == 0) {
                    medalCountDifference = Integer.parseInt(rhs.getBronze()) - Integer.parseInt
                            (lhs.getBronze());
                }
            }
            return medalCountDifference;
        } catch (Exception ex) {
            return 0;
        }
    }
}
