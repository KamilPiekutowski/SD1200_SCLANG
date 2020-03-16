SDDelayMS
{
	var synthDef;

	//getter/setter
	var <>name;

	*new {
		^super.new.init()
    }

	init {
		synthDef = SynthDef.new(\sinosc, {
			arg freqL = 0.5, freqR = 0.5, t_gate = 0,
			knob0 = 1.00, knob1 = 0.08, knob2 = 1.00, knob3 = 0.00,
			knob4 = 0.00, knob5 = 0.00, knob6 = 0.00, knob7 = 0.00;
			var sig, env;
			env = EnvGen.kr(Env.perc, t_gate);
			sig = SinOsc.ar([freqL * 5000, freqR * 5000]) * env * 0.1;
			sig = DelayC.ar(sig, knob0, knob1, knob2, sig);
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
			\t_gate, msg[3].asInteger,
			\freqL, msg[2],
			\freqR, msg[1],
		);
	}

	synthKnobCtlSet { | synth, knob, val |

		("knob: " + knob + " val: " + val).postln;
		synth.set(knob, val);
	}
}

