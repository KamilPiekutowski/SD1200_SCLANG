SDDef {

	//

	var synthDef; //SynthDef.new
	var synth; //Synth.new

	// list of effects and their corrsponding templates
	var fxList;

	// input from serial port
	var knob0;
	var knob1;
	var knob2;
	var knob3;
	var knob4;
	var knob5;
	var knob6;
	var knob7;


    //synthDef {
    //    ^synthDef
    //}

	// collect and send a list of predefined effects
	collect_and_send_fx_list {
		var oscmsg;
		fxList = [
			"Reverb_slider2d",
			"DelayQuant_slider2d",
			"DelayMs_slider2d",
			"Distortion_slider2d",
			"Flanger_slide2d",
		];
		oscmsg = NetAddr.new("127.0.0.1", 7770);
		fxList.do {arg x; oscmsg.sendMsg("/fx_list", x)};
		oscmsg.sendMsg("/done");
    }

	*new { | newValue |
        ^super.new.init(newValue)
    }

	init { | synthdef |
		"init".postln;
		//get the fx list and send it back
		OSCdef.new(
			\button,
			{
				arg msg, time, addr, recvPort;
				"sending fx list".postln;
				this.collect_and_send_fx_list();
			},
			'/get_fx_list'
		);

		OSCdef.new(
			\qml_gui_ctrl,
			{
				arg msg, time, addr, recvPort;
				msg.postln;
				synthDef.name.postln;
				synthDef.synthSet(synth, msg);
				//synth.set(
				//	\sig, msg[3].asInteger,
				//	\freqL, msg[2],
				//	\freqR, msg[1],
				//);
			},
			'/qml_gui_ctrl'
		);

		OSCdef.new(
			\qml_set_synthdef,
			{
				arg msg, time, addr, recvPort;
				"setting sunthdef".postln;
				//msg[1].postln;
				synthDef = this.getSynthDef(msg[1]);
				synthDef.add;
				//synthDef.allControlNames[0].asString.split($ )[4].postln;
				//msg.do { arg x, y; synth.set(ctlName[y], x)};
				synthDef.name.postln;
				Routine({
					"before wait".postln;
					0.1.wait;
					synth = Synth.new(synthDef.name);
					"after wait".postln;
				}).play;
			},
			'/qml_set_synthdef'
		);
    }

	//SynthDef factory method
	getSynthDef { | synthDefName |
		Server.hardFreeAll;

		switch (synthDefName,

			\Reverb_slider2d,   {^SDReverb.new()},
			\DelayQuant_slider2d, {^SDDelayQuant.new()},
			\DelayMs_slider2d, {^SDDelayMS.new()},
			\Distortion_slider2d, {^SDReverb.new()},
			\Flanger_slider2d,   {^SDReverb.new()},
		)
	}
}
