SDDef {

	//

	var synthDef; //SynthDef.new
	var synth; //Synth.new

	// list of effects and their corrsponding templates
	var fxList;

	// serial
	var port;
	var serialRoutine;

	// input from serial port
	var knob0;
	var knob1;
	var knob2;
	var knob3;
	var knob4;
	var knob5;
	var knob6;
	var knob7;

	// collect and send a list of predefined effects
	collect_and_send_fx_list {
		var oscmsg;
		fxList = [
			"Reverb_slider2d",
			"DelayQuant_slider2d",
			"DelayMS_slider2d",
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
				synthDef.synthGUICtlSet(synth, msg);
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

		//serial
		port = SerialPort(
			"//dev/ttyACM0",    //edit to match your port. SerialPort.listDevices
			baudrate: 115200,    //check that baudrate is the same as in arduino sketch
			crtscts: true);


		serialRoutine = Routine.new({
			var knob, val;
			{
				knob = port.read;
				val = port.read;

				switch (knob,

					0,   { knob0 = \knob0;},
					1,   { knob1 = \knob1;},
					2,   { knob2 = \knob2;},
					3,   { knob3 = \knob3;},
					4,   { knob4 = \knob4;},
					5,   { knob5 = \knob5;},
					6,   { knob6 = \knob6;},
					7,   { knob7 = \knob7;},

				);

				val = val.asFloat / 255.0;

				synthDef.synthKnobCtlSet(synth, knob, val);

			}.loop;
		});

		serialRoutine.play;
	}

	//SynthDef factory method
	getSynthDef { | synthDefName |
		Server.hardFreeAll;

		switch (synthDefName,

			\Reverb_slider2d,   {^SDReverb.new()},
			\DelayQuant_slider2d, {^SDDelayQuant.new()},
			\DelayMS_slider2d, {^SDDelayMS.new()},
			\Distortion_slider2d, {^SDReverb.new()},
			\Flanger_slider2d,   {^SDReverb.new()},
		)
	}
}
