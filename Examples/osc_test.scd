

NetAddr.langPort;

(
OSCdef.new(
	\button,
	{
		arg msg, time, addr, recvPort;
		msg.postln;

		b = NetAddr.new("127.0.0.1", 7770);
		a = List.new();
		30.do {arg x; a.add("EFFECT" + x)};
		a.do {arg x; b.sendMsg("/fx_list", x)}
;		b.sendMsg("/done");

	},
    '/get_fx_list'
);
)






