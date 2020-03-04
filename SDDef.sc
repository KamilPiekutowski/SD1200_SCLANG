SDDef {

	var synthDef;
	var oscname;

    synthDef {
        ^synthDef
    }

	collect_and_send_fx_list {
		var oscmsg;
		oscmsg = NetAddr.new("127.0.0.1", 7770);
		30.do {arg x; oscmsg.sendMsg("/fx_list", "EFFECT" + x)};
		oscmsg.sendMsg("/done");
    }

	*new { | newValue |
        ^super.new.init(newValue)
    }

	init { | synthdef |
        // do initiation here
		synthDef = synthdef;
		"init".postln;
		synthDef.name.postln;

		//get the fx list and send it back
		OSCdef.new(
			\button,
			{
				arg msg, time, addr, recvPort;
				"sending fx list".postln;
				this.send_fx_list();
			},
			'/get_fx_list'
		);

		OSCdef.new(
			\qml_gui_ctrl,
			{
				arg msg, time, addr, recvPort;
				"sending fx list".postln;
				this.collect_and_send_fx_list();
			},
			'/qml_gui_ctrl'
		);

    }
}
