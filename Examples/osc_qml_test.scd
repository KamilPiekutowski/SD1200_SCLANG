(
a = SDDef.new(SynthDef.new(\sinosc, {
	var sig;
	sig = SinOsc.ar;
	Out.ar(0,sig);
}));
)

a.synthDef.add;

(
b = SynthDef.new(\sinosc, {
	var sig;
	sig = SinOsc.ar;
	Out.ar(0,sig);
});
)

a.synthDef.name;

(
OSCdef.new(\fxlist,
			{
				arg msg, time, addr, port;
		        msg[1].postln;
			},
			'/get_fx_list'
		);
)

NetAddr.langPort;

