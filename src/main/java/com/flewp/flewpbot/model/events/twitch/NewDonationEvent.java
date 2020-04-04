package com.flewp.flewpbot.model.events.twitch;

import com.flewp.flewpbot.model.events.BaseEvent;
import com.flewp.flewpbot.model.streamlabs.StreamlabsDonation;

public class NewDonationEvent extends BaseEvent {
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
