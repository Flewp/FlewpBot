package com.flewp.flewpbot.event;

import com.github.philippheuer.events4j.domain.Event;
import com.github.twitch4j.streamlabs4j.api.domain.StreamlabsDonationsData;

public class NewDonationEvent extends Event {
    private StreamlabsDonationsData.StreamlabsDonation donation;

    public NewDonationEvent(StreamlabsDonationsData.StreamlabsDonation donation) {
        this.donation = donation;
    }

    public StreamlabsDonationsData.StreamlabsDonation getDonation() {
        return donation;
    }
}
