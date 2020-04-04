package com.flewp.flewpbot;

import javax.sound.midi.MidiMessage;

public class FlewpBotMIDIReceiver {
    private MIDIMessageCallback midiMessageCallback;
    private String name;
    private boolean matchExact;

    public FlewpBotMIDIReceiver(MIDIMessageCallback midiMessageCallback, String name, boolean matchExact) {
        this.midiMessageCallback = midiMessageCallback;
        this.name = name;
        this.matchExact = matchExact;
    }

    public MIDIMessageCallback getMidiMessageCallback() {
        return midiMessageCallback;
    }

    public String getName() {
        return name;
    }

    public boolean isMatchExact() {
        return matchExact;
    }

    public interface MIDIMessageCallback {
        void onMIDIMessage(String deviceName, MidiMessage midiMessage, long timestamp);
    }
}
