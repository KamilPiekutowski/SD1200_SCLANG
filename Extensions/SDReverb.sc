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
			arg freqL, freqR;
			var sig;
			sig = SinOsc.ar([freqL * 5000, freqR * 5000]);
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
