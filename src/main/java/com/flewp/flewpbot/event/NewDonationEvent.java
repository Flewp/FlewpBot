package com.flewp.flewpbot.event;

import com.flewp.flewpbot.model.streamlabs.StreamlabsDonation;
import com.github.philippheuer.events4j.domain.Event;

public class NewDonationEvent extends Event {
    private StreamlabsDonation donation;

    public NewDonationEvent(StreamlabsDonation donation) {
        this.donation = donation;
    }

    public StreamlabsDonation getDonation() {
        return donation;
    }

    @Override
    public String toString() {
        return "NewDonationEvent [ " + donation.toString() + " ]";
    }
}
