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


    synthDef {
        ^synthDef
    }

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
				var ctlName = ["freqL","freqR"];
				msg.postln;
				msg.do { arg x, y; synth.set(ctlName[y], x)};
			},
			'/qml_gui_ctrl'
		);

		OSCdef.new(
			\qml_set_synthdef,
			{
				arg msg, time, addr, recvPort;
				"setting sunthdef".postln;
				msg[1].postln;
				synthDef = this.getSynthDef(msg[1]);
				synthDef.add;
				synthDef.allControlNames[0].asString.split($ )[4].postln;
				//msg.do { arg x, y; synth.set(ctlName[y], x)};
				synth = Synth.new(synthDef.name);
			},
			'/qml_set_synthdef'
		);
    }

	//SynthDef factory method
	getSynthDef { | synthDefName |
		switch (synthDefName,

			\Reverb_slider2d,   {^SDReverb.new()},
			\DelayQuant_slider2d, {^SDReverb.new()},
			\DelayMs_slider2d, {^SDReverb.new()},
			\Distortion_slider2d, {^SDReverb.new()},
			\Flanger_slide2d,   {^SDReverb.new()},
		)
	}
}
