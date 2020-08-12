package com.flewp.flewpbot;

import javax.sound.midi.MidiMessage;
import java.util.List;

public class FlewpBotMIDIReceiver {
    private MIDIMessageCallback midiMessageCallback;
    private List<String> names;
    private boolean matchExact;

    public FlewpBotMIDIReceiver(MIDIMessageCallback midiMessageCallback, List<String> names, boolean matchExact) {
        this.midiMessageCallback = midiMessageCallback;
        this.names = names;
        this.matchExact = matchExact;
    }

    public MIDIMessageCallback getMidiMessageCallback() {
        return midiMessageCallback;
    }

    public List<String> getNames() {
        return names;
    }

    public boolean isMatchExact() {
        return matchExact;
    }

    public interface MIDIMessageCallback {
        void onMIDIMessage(String deviceName, MidiMessage midiMessage, long timestamp);
    }
}
