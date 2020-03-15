SDReverb
{
	var synthDef;

	//getter/setter
	var <>name;

	*new {
		^super.new.init()
    }

	init {
		synthDef = SynthDef.new(\sinosc, {
			arg freqL = 0.5, freqR = 0.5, sig = 0, mix = 0.25, room = 0.15, damp = 0.5;
			sig = SinOsc.ar([freqL * 5000, freqR * 5000]) * sig * 0.80;
			sig = FreeVerb.ar(
				Pan2.ar(
					Decay.ar(sig, 0.25, LFCub.ar(1200, 0, 0.1)),
					LFNoise1.ar(1).range(-1, 1)
				),
				mix,
				room,
				damp
			)
			Out.ar(0,sig);
		});

		name = synthDef.name;
	}

	add {
		synthDef.add;
	}
	synthSet { | synth, msg |
		var ctlName = ["freqL", "freqR", "sig"];
		msg.postln;
		synth.set(
			\sig, msg[3].asInteger,
			\freqL, msg[2],
			\freqR, msg[1],
		);
	}

}
