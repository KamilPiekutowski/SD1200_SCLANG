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
		//setting up the port
		port = SerialPort(
			"/dev/ttyACM0",    //edit to match your port. SerialPort.listDevices
			baudrate: 9600,    //check that baudrate is the same as in arduino sketch
			crtscts: true);
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

		serialRoutine = Routine.new({
			var knob, value;
			{
				knob = port.read;
				value = port.read;
				/*
				switch (knob - 54,

				0,   { knob = \knob0;},
				1,   { knob = \knob1;},
				2,   { knob = \knob2;},
				3,   { knob = \knob3;},
				4,   { knob = \knob4;},
				5,   { knob = \knob5;},
				6,   { knob = \knob6;},
				7,   { knob = \knob7;},

				);*/

				//val = val.asFloat / 255.0;
				//knob.postln;
				("knob: " + knob).postln;
				("value: " + value).postln;
				//1.wait;
				//synthDef.synthKnobCtlSet(synth, knob, val);

			}.loop;
		});

		serialRoutine.play;
	}

	//SynthDef factory method
	getSynthDef { | synthDefName |
		Server.hardFreeAll;

		switch (synthDefName,

			\Reverb_slider2d,   {^SDReverb.new()},
			\DelayQuant_slider2d, {^SDReverb.new()},
			\DelayMS_slider2d, {^SDReverb.new()},
			\Distortion_slider2d, {^SDReverb.new()},
			\Flanger_slider2d,   {^SDReverb.new()},
		)
	}
}

