s.waitForBoot {
(

a = SDDef.new(SynthDef.new(\sinosc, {
	arg freqL, freqR;
	var sig;
	sig = SinOsc.ar([freqL, freqR]);
	Out.ar(0,sig);
}));

//a.synthDef.add;

)
};

