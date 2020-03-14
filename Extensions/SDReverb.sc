SDReverb
{
	*new {
        ^SynthDef.new(\sinosc, {
			arg freqL, freqR;
			var sig;
			sig = SinOsc.ar([freqL * 5000, freqR * 5000]);
			Out.ar(0,sig);
		})
    }
}